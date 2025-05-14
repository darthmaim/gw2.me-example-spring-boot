package me.gw2.example.web;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Map;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@RestController
class HomeController {
    @Autowired
    private RestClient restClient;

    @GetMapping("/")
    public ModelAndView home(@AuthenticationPrincipal @Nullable OAuth2User user) {
        if(user == null) {
            return new ModelAndView("home");
        }

        // get accounts
        var accountsResponse = this.restClient.get()
                        .uri("https://gw2.me/api/accounts")
                        .attributes(clientRegistrationId("gw2me"))
                        .retrieve()
                        .body(Gw2MeAccounts.class);

        // build model with user attributes and accounts
        var model = Map.of(
                "attributes", user.getAttributes(),
                "accounts", accountsResponse.accounts
        );

        return new ModelAndView("home", model);
    }

    @GetMapping("/account")
    public void account(@RequestParam String id, HttpServletResponse response) throws IOException {
        // get subtoken
        var subtokenResponse = this.restClient.get()
                .uri("https://gw2.me/api/accounts/%s/subtoken".formatted(id))
                .attributes(clientRegistrationId("gw2me"))
                .retrieve()
                .body(Gw2MeSubtokenResponse.class);

        // redirect to official GW2 API
        response.sendRedirect(
                "https://api.guildwars2.com/v2/account?access_token=%s".formatted(subtokenResponse.subtoken));
    }

    record Gw2MeAccounts(Gw2MeAccount[] accounts) {}
    record Gw2MeAccount(String id, String name, Boolean shared) {}

    record Gw2MeSubtokenResponse(String subtoken) {}
}
