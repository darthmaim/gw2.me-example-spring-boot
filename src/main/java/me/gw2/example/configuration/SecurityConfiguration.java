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
        // create custom auth resolver
        var resolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);

        // configure
        resolver.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers
                // always enable PKCE, even for confidential clients
                .withPkce()
                // add `include_granted_scopes` parameter
                .andThen(auth -> auth.additionalParameters(params -> params.put("include_granted_scopes", "true"))));

        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .oauth2Login(auth -> auth
                        .authorizationEndpoint(endpoint -> endpoint.authorizationRequestResolver(resolver)))
                .oauth2Client(Customizer.withDefaults());

        return http.build();
    }
}
