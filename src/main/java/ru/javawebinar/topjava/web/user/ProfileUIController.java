package ru.javawebinar.topjava.web.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.web.I18nMessageResolver;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
public class ProfileUIController extends AbstractUserController {

    @GetMapping
    public String profile() {
        return "profile";
    }

    @PostMapping
    public String updateProfile(@Valid UserTo userTo, BindingResult bindingResult, SessionStatus status) {
        if (bindingResult.hasErrors()) {
            return "profile";
        } else {
            try {
                super.update(userTo, SecurityUtil.authUserId());
                SecurityUtil.get().setTo(userTo);
                status.setComplete();
                return "redirect:/meals";
            } catch (DataIntegrityViolationException e) {
                bindingResult.rejectValue("email",
                        I18nMessageResolver.EXCEPTION_DUPLICATE_EMAIL, "user such email exist");
                return "profile";
            }
        }
    }

    @GetMapping("/register")
    public String register(ModelMap model) {
        model.addAttribute("userTo", new UserTo());
        model.addAttribute("register", true);
        return "profile";
    }

    @PostMapping(value = "/register")
    public String saveRegister(@Valid UserTo userTo, BindingResult bindingResult, SessionStatus status, ModelMap model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("register", true);
            return "profile";
        } else {
            try {
                super.create(userTo);
                status.setComplete();
                return "redirect:/login?message=app.registered&username=" + userTo.getEmail();
            } catch (DataIntegrityViolationException e) {
                model.addAttribute("register", true);
                bindingResult.rejectValue("email",
                        I18nMessageResolver.EXCEPTION_DUPLICATE_EMAIL, "user such email exist");
                return "profile";
            }
        }
    }
}