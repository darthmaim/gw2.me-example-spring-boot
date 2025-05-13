package me.gw2.example.web;

import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestController
class HomeController {

    @GetMapping("/")
    public ModelAndView home(@AuthenticationPrincipal @Nullable OAuth2User user) {
        return new ModelAndView("home", user != null ? Map.of("attributes", user.getAttributes()) : null);
    }
}
