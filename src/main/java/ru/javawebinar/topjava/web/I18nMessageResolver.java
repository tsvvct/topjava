package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

@Component
public class I18nMessageResolver {
    public static final String EXCEPTION_DUPLICATE_EMAIL = "err.duplicated.email";
    public static final String EXCEPTION_DUPLICATE_DATETIME = "err.duplicated.datetime";
    private static final Map<String, String> constrains_i18n_map = Map.of(
            "users_unique_email_idx", EXCEPTION_DUPLICATE_EMAIL,
            "meals_unique_user_datetime_idx", EXCEPTION_DUPLICATE_DATETIME);

    private final MessageSource messageSource;

    @Autowired
    public I18nMessageResolver(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String resolveI18nMessage(Locale locale, String errMessage) {
        String result = errMessage;
        String lowerCaseMsg = errMessage.toLowerCase();
        for (Map.Entry<String, String> entry : constrains_i18n_map.entrySet()) {
            if (lowerCaseMsg.contains(entry.getKey())) {
                result = getMessage(entry.getValue(), locale);
            }
        }
        return result;
    }

    public String getMessage(String msgCode) {
        return messageSource.getMessage(msgCode, null, Locale.getDefault());
    }

    public String getMessage(String msgCode, Locale locale) {
        return messageSource.getMessage(msgCode, null, locale);
    }
}
