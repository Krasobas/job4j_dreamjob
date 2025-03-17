package ru.job4j.dreamjob.utils;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import ru.job4j.dreamjob.model.User;

public class SessionUtils {
    public static void insertUserToModel(Model model, HttpSession session) {
        var user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
    }
}
