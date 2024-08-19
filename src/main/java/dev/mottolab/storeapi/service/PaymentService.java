package dev.mottolab.storeapi.service;

import com.google.zxing.NotFoundException;
import dev.mottolab.storeapi.entity.OrderEntity;
import dev.mottolab.storeapi.entity.PaymentEntity;
import dev.mottolab.storeapi.events.OrderQueueEvent;
import dev.mottolab.storeapi.exception.PaymentProceedFail;
import dev.mottolab.storeapi.exception.QRCodeNotExist;
import dev.mottolab.storeapi.exception.SlipNotValid;
import dev.mottolab.storeapi.provider.chillpay.ChillpayProvider;
import dev.mottolab.storeapi.provider.chillpay.exception.ChillpayCreatePaymentFail;
import dev.mottolab.storeapi.provider.chillpay.response.PaymentCreateURLResult;
import dev.mottolab.storeapi.provider.promptpay.PromptpayProvider;
import dev.mottolab.storeapi.provider.qrcode.QRCodeProvider;
import dev.mottolab.storeapi.provider.rdcw.slipverify.SlipverifyProvider;
import dev.mottolab.storeapi.provider.rdcw.slipverify.exception.SlipVerifyError;
import dev.mottolab.storeapi.provider.rdcw.slipverify.response.SlipVerifyResponse;
import dev.mottolab.storeapi.provider.scbopenapi.SCBAPIProvider;
import dev.mottolab.storeapi.provider.scbopenapi.response.PromptpayCreateResult;
import dev.mottolab.storeapi.provider.truemoney.voucher.TruemoneyVoucherProvider;
import dev.mottolab.storeapi.provider.truemoney.voucher.excpetion.TmnVoucherError;
import dev.mottolab.storeapi.provider.truemoney.voucher.response.TmnVoucherResponse;
import dev.mottolab.storeapi.repository.PaymentRepository;
import dev.mottolab.storeapi.service.utils.ValidatorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class PaymentService {
    private final ApplicationEventPublisher event;
    private final SCBAPIProvider scb;
    private final TruemoneyVoucherProvider tmnVoucher;
    private final PromptpayProvider pp;
    private final QRCodeProvider qrcode;
    private final SlipverifyProvider slip;
    private final ChillpayProvider chillpay;

    private final PaymentRepository repo;

    // Promptpay
    @Value("${config.payment.promptpay.type}")
    private String ppMethod;
    @Value("${config.payment.promptpay.value}")
    private String ppValue;
    // Bank account
    @Value("${config.payment.bank.bankAccount}")
    private String bankAccount;
    @Value("${config.payment.bank.bankCode}")
    private String bankCode;

    public PaymentService(
            ApplicationEventPublisher applicationEventPublisher,
            SCBAPIProvider scb,
            PaymentRepository repo,
            TruemoneyVoucherProvider tmnVoucher,
            PromptpayProvider pp,
            QRCodeProvider qrcode,
            SlipverifyProvider slip,
            ChillpayProvider cp
    ) {
        this.event = applicationEventPublisher;
        this.scb = scb;
        this.repo = repo;
        this.tmnVoucher = tmnVoucher;
        this.pp = pp;
        this.qrcode = qrcode;
        this.slip = slip;
        this.chillpay = cp;
    }

    public void doCompletePayment(PaymentEntity paymentEntity){
        this.event.publishEvent(new OrderQueueEvent(this, paymentEntity));
    }

    public TmnVoucherResponse doProceedViaTruemoneyVoucher(String url, OrderEntity order) throws TmnVoucherError, PaymentProceedFail {
        TmnVoucherResponse result = this.tmnVoucher.verifyVoucher(url);

        if(result != null){
            // Check amount and available
            if(result.getData().getVoucher().available != 1){
                throw new PaymentProceedFail("Voucher available must be only one person redeem");
            }
            if(!Objects.equals(Double.parseDouble(result.getData().getVoucher().amountBaht), order.getPayment().getAmount())){
                throw new PaymentProceedFail("Amount insufficient for this order");
            }

            // Redeem it
            return this.tmnVoucher.redeemVoucher(url);
        }

        throw new PaymentProceedFail("Invalid URL");

    }

    public SlipVerifyResponse doProceedViaSlipVerifyByPromptpay(byte[] file, OrderEntity order) throws IOException, NotFoundException, SlipVerifyError {
        SlipVerifyResponse slip = getSlipInformationByImage(file);
        SlipVerifyResponse.Data data = slip.getData();

        String recieveProxyType = data.receiver.proxy.type;
        String recieveAccount = data.receiver.proxy.value;

        if(
                !(recieveProxyType != null && recieveProxyType.equalsIgnoreCase(this.ppMethod))||
                ValidatorService.checkMatchString(this.ppValue, recieveAccount) < 3
        ){
            throw new PaymentProceedFail("Slip receiver incorrect.");
        }

        if(!Objects.equals(data.getAmount(), order.getPayment().getAmount())){
            throw new PaymentProceedFail("Amount insufficient for this order");
        }

        return slip;
    }

    public SlipVerifyResponse doProceedViaSlipVerifyByBankAccount(byte[] file, OrderEntity order) throws IOException, NotFoundException, SlipVerifyError {
        SlipVerifyResponse slip = getSlipInformationByImage(file);
        SlipVerifyResponse.Data data = slip.getData();

        String recieveAccount = data.receiver.account.value;
        String recieveBank = data.receivingBank;
        if(
               !(recieveBank != null && !Objects.equals(recieveBank, this.bankCode)) ||
                        ValidatorService.checkMatchString(bankAccount, recieveAccount) < 3
        ){
            throw new PaymentProceedFail("Slip receiver incorrect.");
        }

        if(!Objects.equals(data.getAmount(), order.getPayment().getAmount())){
            throw new PaymentProceedFail("Amount insufficient for this order");
        }

        return slip;
    }

    public PromptpayCreateResult generatePromtpayQRCodeBySCB(PaymentEntity payment)  {
        // Create class
        SCBAPIProvider.GeneratePromptpayQrCode pp = new SCBAPIProvider.GeneratePromptpayQrCode();
        pp.setAmount(payment.getAmount());
        pp.setRef1(payment.getTransactionId());
        pp.setRef2(payment.getTransactionId());

        // Generate payment
        return this.scb.generatePromptpayQrCode(pp);
    }

    public PaymentCreateURLResult generateTruemoneyPaymentURLByChillPay(PaymentEntity payment) throws ChillpayCreatePaymentFail {
        // Create class
        ChillpayProvider.CreatePaymentURL cp = new ChillpayProvider.CreatePaymentURL();
        cp.setAmount(payment.getAmount());
        cp.setOrderId(payment.getTransactionId());
        cp.setCustomerId(payment.getTransactionId());
        cp.setChannelCode(ChillpayProvider.PaymentMethod.TRUEMONEY);
        cp.setIpAddress("127.0.0.1");

        // Request it
        return this.chillpay.createPaymentURL(cp);
    }

    public Boolean verifyChecksumByChillpay(String raw, String checksum){
        return this.chillpay.verifyChecksum(raw, checksum);
    }

    public String generatePromptpayQrCodeWithMSISDN(Double amount) {
        return this.pp.generateQR29(amount);
    }

    public void updatePayment(PaymentEntity payment){
        this.repo.save(payment);
    }

    public Optional<PaymentEntity> getPaymentByTransactionId(String transactionId){
        return this.repo.findByTransactionId(transactionId);
    }

    private SlipVerifyResponse getSlipInformationByImage(byte[] image) throws IOException, NotFoundException, SlipVerifyError {
        // Scan QRCode
        String value =  this.qrcode.decodeQr(image);
        if(value.isEmpty()) throw new QRCodeNotExist();
        // Get slip result
        SlipVerifyResponse slip = this.slip.requestSlipVerify(value);
        if(!slip.isValid()){
            throw new SlipNotValid();
        }

        // Get data
        return slip;
    }
}
