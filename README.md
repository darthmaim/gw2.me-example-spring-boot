# gw2.me-example-spring-boot

This is an example **Spring Boot** project to demonstrate how **[gw2.me](https://gw2.me)** can be integrated as 
authentication provider using **Spring Security**.

**[gw2.me](https://gw2.me)** is a OAuth2 compatible authentication provider specifically aimed at **Guild Wars 2**
projects.

## Running this project

1. Go to https://gw2.me/dev/applications and create a new application.
2. Enter the `client_id` and `client_secret` in the 
   [application.properties](./src/main/resources/application.properties).
3. Install dependencies and start the application


## Structure

### application.properties

The [application.properties](./src/main/resources/application.properties) file contains all the config necessary to use
gw2.me as Spring Security authentication provider.

You can configure your `client_id`, `client_secret`, as well as the requested scopes in this file.

### SecurityConfiguration

The [SecurityConfiguration.java](./src/main/java/me/gw2/example/configuration/SecurityConfiguration.java) holds the
Spring Security configuration. This can be used to further configure the OAuth2 client, for example by always enabling
PKCE or passing additional parameters to the gw2.me authorization endpoint.

### RestClientConfig

[RestClientConfig.java](./src/main/java/me/gw2/example/configuration/RestClientConfig.java) configures the Spring
`RestClient` to be able to pass the access token to the gw2.me API.

### HomeController

In the [HomeController.java](./src/main/java/me/gw2/example/web/HomeController.java) are two example endpoints.

The main `/` endpoint gets the current user (if logged in) and loads the users GW2 accounts.

The `/account` endpoint (which can be accessed from the home page when logged in) fetches a GW2 API subtoken and
demonstrates how it can be used to access the Guild Wars 2 API. Take a look at 
[GW2ToolBelt/GW2APIClient](https://github.com/GW2ToolBelt/GW2APIClient) for a Guild Wars 2 API client library.


## Support

Please refer to the [gw2.me documentation](https://gw2.me/dev/docs) to learn about gw2.me.

If you need help using gw2.me in your application, please contact me on
- [gw2.me repository discussions](https://github.com/GW2Treasures/gw2.me/discussions)
- [Discord (`#gw2treasures` channel)](https://discord.gg/gvx6ZSE)
- [BlueSky @gw2treasures.com](https://bsky.app/profile/gw2treasures.com)
- or per email [support@gw2.me](mailto:support@gw2.me)


## License
**gw2.me-example-spring-boot** is licensed under the [MIT License](./LICENSE).
