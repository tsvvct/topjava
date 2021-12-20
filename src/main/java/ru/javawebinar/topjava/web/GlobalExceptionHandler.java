package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.ValidationUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView dataIntegrityViolationErrorHandler(HttpServletRequest req, Exception e) throws Exception {

        log.error("Exception at request " + req.getRequestURL(), e);
        if (req.getRequestURL().toString().contains("topjava/profile")) {
            String id = req.getParameter("id");
            String caloriesPerDay = req.getParameter("caloriesPerDay");
            UserTo userTo = new UserTo((id == null) ? null : Integer.parseInt(id),
                    req.getParameter("name"),
                    req.getParameter("email"),
                    req.getParameter("password"),
                    (caloriesPerDay == null || caloriesPerDay.isEmpty()) ? 0 : Integer.parseInt(caloriesPerDay));
            BeanPropertyBindingResult errors = new BeanPropertyBindingResult(userTo, userTo.getClass().getName());
            errors.rejectValue("email", I18nMessageResolver.EXCEPTION_DUPLICATE_EMAIL, "user such email exist");
            ModelAndView mav = new ModelAndView();
            mav.addObject("userTo", userTo);
            mav.addObject("org.springframework.validation.BindingResult.userTo", errors);
            if (req.getRequestURL().toString().endsWith("register")) {
                mav.addObject("register", true);
            }
            mav.setViewName("profile");
            return mav;
        } else {
            return defaultErrorHandler(req, e);
        }

    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        log.error("Exception at request " + req.getRequestURL(), e);
        Throwable rootCause = ValidationUtil.getRootCause(e);
        String details = ExceptionInfoHandler.getErrMessageDetails(req, e, rootCause);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ModelAndView mav = new ModelAndView("exception",
                Map.of("exception", rootCause, "message", details, "status", httpStatus));
        mav.setStatus(httpStatus);

        // Interceptor is not invoked, put userTo
        AuthorizedUser authorizedUser = SecurityUtil.safeGet();
        if (authorizedUser != null) {
            mav.addObject("userTo", authorizedUser.getUserTo());
        }
        return mav;
    }
}
