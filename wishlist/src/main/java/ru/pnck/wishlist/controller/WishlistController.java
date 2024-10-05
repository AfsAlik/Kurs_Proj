package ru.pnck.wishlist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.pnck.wishlist.entity.User;
import ru.pnck.wishlist.entity.Wish;
import ru.pnck.wishlist.repository.UserRepository;
import ru.pnck.wishlist.repository.WishRepository;
import ru.pnck.wishlist.service.UserLogService;

import java.util.List;

@Controller
public class WishlistController {
    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final UserLogService userLogService;

    @Autowired
    public WishlistController(
            WishRepository wishRepository,
            UserRepository userRepository,
            UserLogService userLogService) {
        this.wishRepository = wishRepository;
        this.userRepository = userRepository;
        this.userLogService = userLogService;
    }

    @GetMapping({"/", "/list"})
    public ModelAndView listUsersWishlists(@AuthenticationPrincipal UserDetails userDetails) {
        userLogService.addLog(userDetails == null ? null : userDetails.getUsername(), "load /list");
        var mav = new ModelAndView("list-wishlists");
        mav.addObject("users", userRepository.findAll());
        return mav;
    }

    @GetMapping("/showWishlist")
    public ModelAndView showUserWishlist(
            @RequestParam long userId,
            @AuthenticationPrincipal UserDetails userDetails) {
        userLogService.addLog(
                userDetails == null ? null : userDetails.getUsername(),
                "load /showWishlist?userId=" + userId);
        var mav = new ModelAndView("show-wishlist");
        var user = userRepository.findById(userId);
        mav.addObject("wishes", user.isPresent() ? wishRepository.findAllByUserId(user.get().getId()) : List.of());
        mav.addObject("userName", user.map(User::getEmail).orElse(null));
        mav.addObject("displayUserName", user.map(User::getName).orElse(""));
        mav.addObject("currentUserName", userDetails.getUsername());
        return mav;
    }

    @GetMapping("/addWish")
    public ModelAndView addWish(@AuthenticationPrincipal UserDetails userDetails) {
        userLogService.addLog(userDetails == null ? null : userDetails.getUsername(), "load /addWish");
        var mav = new ModelAndView("add-wish-form");
        var wish = new Wish();
        mav.addObject("wish", wish);

        return mav;
    }

    @PostMapping("/saveWish")
    public String saveWish(
            @ModelAttribute Wish wish,
            @AuthenticationPrincipal UserDetails userDetails) {
        var user = userRepository.findByEmail(userDetails.getUsername());
        wish.setUser(user);
        wishRepository.save(wish);
        userLogService.addLog(user, "Saved wish " + wish.getName());
        return "redirect:showWishlist?userId=" + user.getId();
    }

    @GetMapping("/deleteWish")
    public String deleteWish(
            @RequestParam Long wishId,
            @AuthenticationPrincipal UserDetails userDetails) {
        var wish = wishRepository.findById(wishId);
        var user = userRepository.findByEmail(userDetails.getUsername());
        if (wish.isPresent() && wish.get().getUser().getId().equals(user.getId())) {
            wishRepository.delete(wish.get());
            userLogService.addLog(user, "Deleted wish " + wish.get().getName());
        }
        return "redirect:showWishlist?userId=" + user.getId();
    }
}
