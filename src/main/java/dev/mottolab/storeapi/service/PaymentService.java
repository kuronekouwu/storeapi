package dev.mottolab.storeapi.service;

import dev.mottolab.storeapi.entity.OrderEntity;
import dev.mottolab.storeapi.entity.PaymentEntity;
import dev.mottolab.storeapi.events.OrderQueueEvent;
import dev.mottolab.storeapi.exception.PaymentCreateFail;
import dev.mottolab.storeapi.exception.PaymentProceedFail;
import dev.mottolab.storeapi.provider.scbopenapi.SCBAPIProvider;
import dev.mottolab.storeapi.provider.scbopenapi.response.PromptpayCreateResult;
import dev.mottolab.storeapi.provider.truemoney.voucher.TruemoneyVoucherProvider;
import dev.mottolab.storeapi.provider.truemoney.voucher.excpetion.TmnVoucherError;
import dev.mottolab.storeapi.provider.truemoney.voucher.response.TmnVoucherResponse;
import dev.mottolab.storeapi.repository.PaymentRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {
    private final ApplicationEventPublisher event;
    private final SCBAPIProvider scb;
    private final TruemoneyVoucherProvider tmnVoucher;
    private final PaymentRepository repo;

    public PaymentService(
            ApplicationEventPublisher applicationEventPublisher,
            SCBAPIProvider scb,
            PaymentRepository repo,
            TruemoneyVoucherProvider tmnVoucher
    ) {
        this.event = applicationEventPublisher;
        this.scb = scb;
        this.repo = repo;
        this.tmnVoucher = tmnVoucher;
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
            if(Double.parseDouble(result.getData().getVoucher().amountBaht) != order.getTotal()){
                throw new PaymentProceedFail("Amount insufficient for this order");
            }

            // Redeem it
            return this.tmnVoucher.redeemVoucher(url);
        }

        throw new PaymentProceedFail("Invalid URL");

    }

    public PromptpayCreateResult generatePromtpayQRCode(OrderEntity order, String transactionId) throws PaymentCreateFail {
        // Create class
        SCBAPIProvider.GeneratePromptpayQrCode pp = new SCBAPIProvider.GeneratePromptpayQrCode();
        pp.setAmount(order.getTotal());
        pp.setRef1(transactionId);
        pp.setRef2(transactionId);

        // Generate payment
        PromptpayCreateResult ppResult =  this.scb.generatePromptpayQrCode(pp);
        if(ppResult == null){
            throw new PaymentCreateFail();
        }

        return ppResult;
    }

    public PaymentEntity updatePayment(PaymentEntity payment){
        return this.repo.save(payment);
    }

    public Optional<PaymentEntity> getPaymentByTransactionId(String transactionId){
        return this.repo.findByTransactionId(transactionId);
    }
}
