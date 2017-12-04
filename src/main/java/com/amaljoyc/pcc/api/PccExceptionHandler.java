package com.amaljoyc.pcc.api;

import com.amaljoyc.pcc.api.dto.ErrorResponse;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by achemparathy on 04.12.17.
 */
@ControllerAdvice
@RestController
public class PccExceptionHandler {

    private static final Logger LOGGER = Logger.getLogger(PccExceptionHandler.class);

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public ErrorResponse handleException(Exception e) {
        LOGGER.warn(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}
