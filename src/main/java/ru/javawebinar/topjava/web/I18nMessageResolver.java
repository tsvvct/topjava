package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.util.Map;

@Component
public class I18nMessageResolver {

    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    private static final String EXCEPTION_DUPLICATE_EMAIL = "err.duplicated.email";
    private static final String EXCEPTION_DUPLICATE_DATETIME = "err.duplicated.datetime";
    private static final Map<String, String> constrains_i18n_map = Map.of(
            "users_unique_email_idx", EXCEPTION_DUPLICATE_EMAIL,
            "meals_unique_user_datetime_idx", EXCEPTION_DUPLICATE_DATETIME);

    private static MessageSourceAccessor messageSourceAccessor;

    @Autowired
    public void setMessageSourceAccessor(MessageSource messageSource) {
        I18nMessageResolver.messageSourceAccessor = new MessageSourceAccessor(messageSource);
    }

    public static String resolveMessageFromError(Exception e, Throwable rootCause) {
        String result;
        if (e instanceof BindException) {
            result = ValidationUtil.getErrorResponse(((BindException) e).getBindingResult()).getBody();
        } else if (e instanceof DataIntegrityViolationException) {
            result = rootCause.toString();
            if (result != null) {
                String lowerCaseMsg = result.toLowerCase();
                for (Map.Entry<String, String> entry : constrains_i18n_map.entrySet()) {
                    if (lowerCaseMsg.contains(entry.getKey())) {
                        result = messageSourceAccessor.getMessage(entry.getValue());
                    }
                }
            }
        } else {
            result = rootCause.toString();
        }
        return result;
    }
}
