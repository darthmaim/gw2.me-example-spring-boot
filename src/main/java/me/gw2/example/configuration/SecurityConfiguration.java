package me.gw2.example.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityWebFilterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository) throws Exception {
        // Create a custom auth resolver, so we can can configure it
        var resolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);

        // Configure the auth resolver
        resolver.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers
                // Spring Security only uses PKCE for public clients.
                // This always enables PKCE even for confidential clients for added security (optional).
                .withPkce()
                // Here additional parameters can be added when requesting the authorization.
                // For example `include_granted_scopes` (see https://gw2.me/dev/docs/access-tokens for more parameters)
                .andThen(auth -> auth.additionalParameters(params -> params.put("include_granted_scopes", "true"))));

        // Configure http security
        http
                // This configures which endpoints require login.
                // In the example, access to all endpoints is permitted.
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                // Enable the OAuth2 login handler with the authorization resoliver configured above
                .oauth2Login(auth -> auth
                        .authorizationEndpoint(endpoint -> endpoint.authorizationRequestResolver(resolver)))
                // Configure the OAuth2 client (this is, among other things, used to handle ClientAuthorizationRequiredException)
                .oauth2Client(Customizer.withDefaults());

        return http.build();
    }
}
