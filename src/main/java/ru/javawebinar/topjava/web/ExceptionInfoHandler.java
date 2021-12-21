package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.LocaleResolver;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.ErrorType;
import ru.javawebinar.topjava.util.exception.IllegalRequestDataException;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.util.exception.ErrorType.*;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    private static I18nMessageResolver i18nMessageResolver;

    private static LocaleResolver localeResolver;

    @Autowired
    public void setI18nMessageResolver(I18nMessageResolver i18nMessageResolver) {
        ExceptionInfoHandler.i18nMessageResolver = i18nMessageResolver;
    }

    @Autowired
    public void setLocaleResolver(LocaleResolver localeResolver) {
        ExceptionInfoHandler.localeResolver = localeResolver;
    }

    //  http://stackoverflow.com/a/22358422/548473
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, DATA_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        return logAndGetErrorInfo(req, e, true, DATA_ERROR,
                i18nMessageResolver.resolveI18nMessage(localeResolver.resolveLocale(req), rootCause.getMessage()));
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler(BindException.class)
    public List<ErrorInfo> bindException(HttpServletRequest req, Exception e) {
        List<FieldError> errors = ((BindException) e).getBindingResult().getFieldErrors();
        List<ErrorInfo> result = new ArrayList<>();
        for (FieldError err : errors) {
            String objName = (err.getObjectName().equalsIgnoreCase("userto")) ? "user" : err.getObjectName();
            String msg = "[" + i18nMessageResolver.getMessage(objName + "." + err.getField(),
                    localeResolver.resolveLocale(req)) + "]: " + err.getDefaultMessage();
            result.add(logAndGetErrorInfo(req, e, false, VALIDATION_ERROR, msg));
        }
        return result;
//        List<String> msgList = new ArrayList<>();
//        for (FieldError err : errors) {
//            String objName = (err.getObjectName().equalsIgnoreCase("userto")) ? "user" : err.getObjectName();
//            msgList.add("[" + i18nMessageResolver.getMessage(objName + "." + err.getField(),
//                    localeResolver.resolveLocale(req)) + "]: " + err.getDefaultMessage());
//        }
//        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR, msgList.toArray(new String[]{}));
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler({IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class})
    public ErrorInfo illegalRequestDataError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, APP_ERROR);
    }

    //    https://stackoverflow.com/questions/538870/should-private-helper-methods-be-static-if-they-can-be-static
    protected static ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException, ErrorType errorType, String... details) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            log.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        }

        return new ErrorInfo(req.getRequestURL(), errorType,
                Arrays.stream(details).reduce((s1, s2) -> (s1 + "<br>" + s2)).orElse(rootCause.getMessage()));
    }
}