package dev.mottolab.storeapi;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.google.zxing.NotFoundException;
import dev.mottolab.storeapi.dto.exception.*;
import dev.mottolab.storeapi.exception.*;
import dev.mottolab.storeapi.provider.rdcw.slipverify.exception.SlipVerifyError;
import dev.mottolab.storeapi.provider.truemoney.voucher.excpetion.TmnVoucherError;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class RestErrorHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidateErrorDTO invalidDataHandler(MethodArgumentNotValidException ex) {
        ValidateErrorDTO validateErrorDTO = new ValidateErrorDTO();
        ex.getBindingResult().getAllErrors().forEach(error -> validateErrorDTO.addFieldError(((FieldError) error).getField(), error.getDefaultMessage()));
        return validateErrorDTO;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseStatusException unauthorizedHandler(BadCredentialsException ex) {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public NoResourceFoundException notFoundHandler(NoResourceFoundException ex) {
        return ex;
    }

    @ExceptionHandler(AccountAlreadyExist.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseStatusException accountAlreadyExistHandler(AccountAlreadyExist ex) {

        return new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
    }

    @ExceptionHandler(ProductNotExist.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseStatusException productNotExist(ProductNotExist ex) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
    }

    @ExceptionHandler(CategoryNotExist.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseStatusException categoryNotExist(CategoryNotExist ex) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
    }

    @ExceptionHandler(OrderNotExist.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseStatusException orderNoteExist(OrderNotExist ex) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
    }

    @ExceptionHandler(OrderAlreadyProceed.class)
    @ResponseStatus(HttpStatus.GONE)
    @ResponseBody
    public ResponseStatusException orderAlreadyProceed(OrderAlreadyProceed ex) {
        return new ResponseStatusException(HttpStatus.GONE, ex.getLocalizedMessage());
    }

    @ExceptionHandler(PaymentCreateFail.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseStatusException paymentCreateFail(PaymentCreateFail ex) {
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
    }

    @ExceptionHandler(PaymentMismatch.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseStatusException paymentMismatch(PaymentMismatch ex) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
    }

    @ExceptionHandler(TmnVoucherError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseStatusException paymentMismatch(TmnVoucherError ex) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
    }

    @ExceptionHandler(PaymentProceedFail.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseStatusException paymentMismatch(PaymentProceedFail ex) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseStatusException qrcodeError() {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "QRCode not exist.");
    }

    @ExceptionHandler(SlipVerifyError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseStatusException slipVerifyError(SlipVerifyError ex) {
        if(ex.getCode() != 40000){
            return new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
        }

        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong. Please try again later");
    }

    @ExceptionHandler(QRCodeNotExist.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseStatusException qrCodeNotexist(QRCodeNotExist ex) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
    }

    @ExceptionHandler(ShippingRateNotExist.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseStatusException shippingRateNotExist(ShippingRateNotExist ex) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
    }

    @ExceptionHandler(AddressNotExist.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseStatusException addressNotExist(AddressNotExist ex) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
    }


    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseStatusException httpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseStatusException methodArgTypeMismatch() {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Malformed body.");
    }

    @ExceptionHandler(JWTDecodeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public JWTDecodeException jwtException(
            JWTDecodeException ex
    ) {
        return ex;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public HttpRequestMethodNotSupportedException httpMethodNotAllowedHandler(HttpRequestMethodNotSupportedException ex) {
        return ex;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public MissingServletRequestParameterException missingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return ex;
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ResponseStatusException missingServletRequestHeaderException(MissingRequestHeaderException ex) {
        return new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                ex.getLocalizedMessage()
        );
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public HandlerMethodValidationException handlerMethodValidationException(HandlerMethodValidationException ex) {
        return ex;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseStatusException httpMessageNotReadableException() {
        return new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid body."
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseBody
    public ResponseStatusException responseStatusException(ResponseStatusException ex) {
        return ex;
    }
}
