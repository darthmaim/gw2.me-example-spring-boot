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
        // If the user is not logged in, we just show the anonymous home page
        if(user == null) {
            return new ModelAndView("home");
        }

        // If the user is logged in...
        // ... get the GW2 accounts from the gw2.me API
        var accountsResponse = this.restClient.get()
                        .uri("https://gw2.me/api/accounts")
                        .attributes(clientRegistrationId("gw2me"))
                        .retrieve()
                        .body(Gw2MeAccounts.class);

        // Build model to be passed to the view including the user attributes and accounts
        var model = Map.of(
                "attributes", user.getAttributes(),
                "accounts", accountsResponse.accounts
        );

        return new ModelAndView("home", model);
    }

    @GetMapping("/account")
    public void account(@RequestParam String id, HttpServletResponse response) throws IOException {
        // Get GW2 API subtoken for the passed account id from the gw2.me API
        var subtokenResponse = this.restClient.get()
                .uri("https://gw2.me/api/accounts/%s/subtoken".formatted(id))
                .attributes(clientRegistrationId("gw2me"))
                .retrieve()
                .body(Gw2MeSubtokenResponse.class);

        // Redirect to official GW2 API
        response.sendRedirect(
                "https://api.guildwars2.com/v2/account?access_token=%s".formatted(subtokenResponse.subtoken));
    }

    // These records model responses from the gw2.me API. Note that some responses only contain relevant properties.
    // Please consult the gw2.me documentation for full response schemas.
    // In the future, these might be published as official package so they don't have to be declared in every project.
    record Gw2MeAccounts(Gw2MeAccount[] accounts) {}
    record Gw2MeAccount(String id, String name, Boolean shared) {}

    record Gw2MeSubtokenResponse(String subtoken) {}
}
