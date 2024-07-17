package com.example.GuviAssessmentLoginPage.controller;

import com.example.GuviAssessmentLoginPage.entity.User;
import com.example.GuviAssessmentLoginPage.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "This email is already registered.");
            return "register";
        }
        else {
            userService.save(user);
            model.addAttribute("success", "Registration successful. Please log in.");
            return "login";
        }

    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }


    @PostMapping("/login")
    public String loginUser(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (userService.authenticate(email, password)) {
            HttpSession session = request.getSession();
            session.setAttribute("email", email);
            return "profile";
        } else {
            model.addAttribute("error", "Invalid email or password.");
            return "login";
        }
    }

    @GetMapping("/profile")
    public String showProfile(Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }
        User user = userService.findByEmail(email);
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User user) {
        userService.update(user);
        return "redirect:/profile";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }
}

