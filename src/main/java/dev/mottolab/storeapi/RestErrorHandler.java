package dev.mottolab.storeapi;

import com.auth0.jwt.exceptions.JWTDecodeException;
import dev.mottolab.storeapi.dto.exception.AccountAlreadyExistDTO;
import dev.mottolab.storeapi.dto.exception.BadCredentialsDTO;
import dev.mottolab.storeapi.dto.exception.ProductNotExistDTO;
import dev.mottolab.storeapi.dto.exception.ValidateErrorDTO;
import dev.mottolab.storeapi.exception.AccountAlreadyExist;
import dev.mottolab.storeapi.exception.ProductNotExist;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidateErrorDTO invalidDataHandler(MethodArgumentNotValidException ex) {
        ValidateErrorDTO validateErrorDTO = new ValidateErrorDTO();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            validateErrorDTO.addFieldError(((FieldError) error).getField(), error.getDefaultMessage());
        });
        return validateErrorDTO;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BadCredentialsDTO unauthorizedHandler(BadCredentialsException ex) {
        BadCredentialsDTO badCredentialsDTO = new BadCredentialsDTO();
        badCredentialsDTO.setMessage("Invalid username or password");
        return badCredentialsDTO;
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void notFoundHandler() {}

    @ExceptionHandler(AccountAlreadyExist.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public AccountAlreadyExistDTO accountAlreadyExistHandler(AccountAlreadyExist ex) {
        AccountAlreadyExistDTO dto = new AccountAlreadyExistDTO();
        dto.setMessage("Account: " + ex.getAccount() + " already exist.");
        return dto;
    }

    @ExceptionHandler(ProductNotExist.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ProductNotExistDTO productNotExist() {
        ProductNotExistDTO dto = new ProductNotExistDTO();
        dto.setMessage("Product not exist.");
        return dto;
    }

    @ExceptionHandler(JWTDecodeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public void jwtException() {
        return;
    }
}
