package ru.pnck.wishlist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.pnck.wishlist.dto.UserDto;
import ru.pnck.wishlist.service.UserLogService;
import ru.pnck.wishlist.service.UserService;

import javax.validation.Valid;

@Controller
public class SecurityController {
    private UserService userService;

    private UserLogService userLogService;

    @Autowired
    public SecurityController(UserService userService, UserLogService userLogService) {
        this.userService = userService;
        this.userLogService = userLogService;
    }

    @GetMapping("/index")
    public String home(@AuthenticationPrincipal UserDetails userDetails) {
        userLogService.addLog(userDetails == null ? null : userDetails.getUsername(), "load /index");
        return "redirect:/list";
    }

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal UserDetails userDetails) {
        userLogService.addLog(userDetails == null ? null : userDetails.getUsername(), "load /login");
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        userLogService.addLog(userDetails == null ? null : userDetails.getUsername(), "load /register");
        var userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(
            @Valid @ModelAttribute("user") UserDto userDto,
            BindingResult result,
            Model model) {
        var existingUser = userService.findUserByEmail(userDto.getEmail());
        if (existingUser != null) {
            result.rejectValue("email", null, "Пользователь с таким email уже зарегистрирован");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);

        userLogService.addLog(userDto.getEmail(), "User registered");
        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String users(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        userLogService.addLog(userDetails == null ? null : userDetails.getUsername(), "load /users");
        var users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }
}
