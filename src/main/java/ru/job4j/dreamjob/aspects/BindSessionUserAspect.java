package ru.job4j.dreamjob.aspects;

import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import ru.job4j.dreamjob.model.User;

@Aspect
@Component
public class BindSessionUserAspect {

    @Before(value = "within(@BindSessionUser *) && execution(* *(..)) && args(model, session)", argNames = "model,session")
    public void insertUserToModelAdvice(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
    }
}
