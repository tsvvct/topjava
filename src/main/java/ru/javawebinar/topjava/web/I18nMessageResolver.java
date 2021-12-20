package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.LocaleResolver;
import ru.javawebinar.topjava.util.ValidationUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

@Component
public class I18nMessageResolver {

    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    public static final String EXCEPTION_DUPLICATE_EMAIL = "err.duplicated.email";
    public static final String EXCEPTION_DUPLICATE_DATETIME = "err.duplicated.datetime";
    private static final Map<String, String> constrains_i18n_map = Map.of(
            "users_unique_email_idx", EXCEPTION_DUPLICATE_EMAIL,
            "meals_unique_user_datetime_idx", EXCEPTION_DUPLICATE_DATETIME);

    private static MessageSource messageSource;
    private static LocaleResolver localeResolver;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        I18nMessageResolver.messageSource = messageSource;
    }

    @Autowired
    public void setLocaleResolver(LocaleResolver localeResolver) {
        I18nMessageResolver.localeResolver = localeResolver;
    }

    public static String resolveMessageFromError(HttpServletRequest req, Exception e, Throwable rootCause) {
        String result;
        if (e instanceof BindException) {
            result = ValidationUtil.getErrorResponse(((BindException) e).getBindingResult()).getBody();
        } else if (e instanceof DataIntegrityViolationException) {
            result = rootCause.toString();
            if (result != null) {
                String lowerCaseMsg = result.toLowerCase();
                for (Map.Entry<String, String> entry : constrains_i18n_map.entrySet()) {
                    if (lowerCaseMsg.contains(entry.getKey())) {
                        result = getMessage(entry.getValue(), localeResolver.resolveLocale(req));
                    }
                }
            }
        } else {
            result = rootCause.toString();
        }
        return result;
    }

    public static String getMessage(String msgCode) {
        return messageSource.getMessage(msgCode, null, Locale.getDefault());
    }

    public static String getMessage(String msgCode, Locale locale) {
        return messageSource.getMessage(msgCode, null, locale);
    }
}
