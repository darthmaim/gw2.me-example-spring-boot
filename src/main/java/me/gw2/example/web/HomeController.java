package me.gw2.example.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;

@RestController
class HomeController {

    @GetMapping("/")
    public ModelAndView home(@AuthenticationPrincipal OAuth2User user) {
        return new ModelAndView("home", Collections.singletonMap("attributes", user.getAttributes()));
    }
}
