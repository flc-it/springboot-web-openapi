# springboot-web-openapi

## Présentation
Le projet *springboot-web-openapi* est la librairie pour la mise en place de la documentation Web Open API dans une API Spring Boot.

## Frameworks
- [Spring boot](https://spring.io/projects/spring-boot) [@2.7.18](https://docs.spring.io/spring-boot/docs/2.7.18/reference/html)

## Dependencies
- [Spring docs](https://springdoc.org)
- [FLC Commons Core](https://github.com/flc-it/springboot-commons-core)

## Packages
**org.flcit.springboot.web.openapi.configuration** => classes de configuration Open API
**org.flcit.springboot.web.openapi.customiser** => classes de customisation Open API

## Configuration

Configuration des informations générales de l'API :

```properties
openapi.info.title=${info.application.name}
openapi.info.description=${info.application.description}<br/>Environnement : ${info.server.environment}
openapi.info.version=${info.application.version}
openapi.info.contact.name=FLC-IT
openapi.info.contact.url=https://github.com/flc-it
openapi.info.contact.email=dev@flcit.org
```

### Documentation Simple
Permet de générer la documentation uniquement pendant la chaine de build et sans interface Web => rien au runtime.

```properties
openapi.simple-docs=true
openapi.oauth2-token-url=https://keycloak.flc.dm.ad/realms/partenaires/protocol/openid-connect/token
openapi.user-roles=access
openapi.admin-roles=admin
```

```xml
<profile>
    <id>doc</id>
    <properties>
        <envClassifier>dev</envClassifier>
    </properties>
</profile>
```

### Documentation Simple Online (Swagger-ui)
Permet d'activer la documentation dans l'interface Web Swagger UI.

```properties
openapi.simple-online=true
openapi.admin-groupes=FLC_ADMIN
```

### Documentation entièrement Customisée
Il est aussi possible d'entièrement surcharger la configuration Open API de base.

```java
@Profile("doc")
@ConditionalOnClass(name = "org.flcit.springboot.web.openapi.configuration.BaseOpenApiConfiguration")
@Configuration(proxyBeanMethods = false)
@ConfigurationProperties("openapi")
public class OpenApiConfiguration extends BaseOpenApiConfiguration {

    private String oauth2TokenUrl;

    public void setOauth2TokenUrl(String oauth2TokenUrl) {
        this.oauth2TokenUrl = oauth2TokenUrl;
    }

    @Bean
    public GroupedOpenApi rmaApi() {
        return GroupedOpenApi.builder()
                .group("1-API-BVA")
                .pathsToMatch("/api/BVA/**")
                .addOpenApiCustomiser(OpenApiUtils.openApiCustomiser(new Oauth2ClientCredentialsOpenApiCustomiser(oauth2TokenUrl, Role.BVA_SEARCH_PP.name(), Role.RMA_READ_CONTRAT_INDIV.name()), globalResponses(true, OpenApiConfiguration::setCryptoParameters)))
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("2-API-Admin")
                .pathsToMatch("/actuator/**")
                .addOpenApiCustomiser(OpenApiUtils.openApiCustomiser(new Oauth2ClientCredentialsOpenApiCustomiser(oauth2TokenUrl, org.flcit.springboot.security.keycloak.api.domain.Role.ADMIN.name()), globalResponses(true)))
                .build();
    }

    @Override
    public boolean isOnlyDocs() {
        return true;
    }

}
```

### Informations importantes
En production la documentation online Swagger-ui est totalement coupée pour des raisons de performance.

## Projets dépendants
- [postgresql-admin-back](https://github.com/flc-it/postgresql-admin-back)