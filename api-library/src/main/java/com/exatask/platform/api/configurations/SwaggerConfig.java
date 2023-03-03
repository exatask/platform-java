package com.exatask.platform.api.configurations;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
public class SwaggerConfig {

  private static final String TYPE_STRING = "string";
  private static final String TYPE_OBJECT = "object";

  @Autowired
  private ApiServiceConfig serviceConfig;

  @Autowired(required = false)
  private ApiSwaggerConfig swaggerConfig;

  private ApiResponse getApiFailureResponse() {

    Schema<Boolean> statusSchema = new Schema<>();
    statusSchema.type("boolean")
        .setDefault(false);

    Schema<String> messageTypeSchema = new Schema<>();
    messageTypeSchema.type(TYPE_STRING)
        .setDefault("ERROR");

    Schema<Object> messageSchema = new Schema<>();
    messageSchema.type(TYPE_OBJECT)
        .addProperties("type", messageTypeSchema)
        .addProperties("text", (new Schema<>()).type(TYPE_STRING))
        .addProperties("error_code", (new Schema<>()).type(TYPE_STRING));

    Schema<Object> stackTraceSchema = new Schema<>();
    stackTraceSchema.type(TYPE_OBJECT)
        .addProperties("file", (new Schema<>()).type(TYPE_STRING))
        .addProperties("class", (new Schema<>()).type(TYPE_STRING))
        .addProperties("method", (new Schema<>()).type(TYPE_STRING))
        .addProperties("line", (new Schema<>()).type("integer"));

    Schema<Object> apiFailure = new Schema<>();
    apiFailure.type(TYPE_OBJECT)
        .addProperties("status", statusSchema)
        .addProperties("message", messageSchema)
        .addProperties("invalid_attributes", (new Schema<>()).type(TYPE_OBJECT))
        .addProperties("extra_params", (new Schema<>()).type(TYPE_OBJECT))
        .addProperties("stack_trace", (new ArraySchema()).items(stackTraceSchema))
        .addProperties("exception_cause", (new Schema<>()).type(TYPE_STRING));

    Content responseContent = new Content();
    responseContent.addMediaType(org.springframework.http.MediaType.APPLICATION_JSON.toString(), (new MediaType()).schema(apiFailure));
    return (new ApiResponse()).content(responseContent);
  }

  private SecurityScheme getBasicSecurityScheme() {

    return new SecurityScheme()
        .name("basic-auth")
        .type(SecurityScheme.Type.HTTP)
        .scheme("basic")
        .in(SecurityScheme.In.HEADER);
  }

  private SecurityScheme getBearerSecurityScheme() {

    return new SecurityScheme()
        .name("bearer-auth")
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .in(SecurityScheme.In.HEADER);
  }

  private Components getComponents() {

    Components components = Optional.ofNullable(swaggerConfig)
            .map(ApiSwaggerConfig::getComponents)
            .orElse(new Components());

    return components
        .addSecuritySchemes("BasicAuth", getBasicSecurityScheme())
        .addSecuritySchemes("BearerAuth", getBearerSecurityScheme())
        .addResponses("ApiFailure", getApiFailureResponse());
  }

  private List<SecurityRequirement> getSecurityRequirements() {

    List<SecurityRequirement> requirements = new ArrayList<>();
    requirements.add(new SecurityRequirement().addList("BasicAuth"));
    requirements.add(new SecurityRequirement().addList("BearerAuth"));
    return requirements;
  }

  private Contact getDefaultContact() {

    return new Contact()
            .name("Rohit Aggarwal")
            .email("rohit.aggarwal@exatask.com");
  }

  private Info getInfo() {

    Contact contact = Optional.ofNullable(swaggerConfig)
            .map(ApiSwaggerConfig::getContact)
            .orElse(getDefaultContact());

    return new Info().title(serviceConfig.getName())
        .description(serviceConfig.getDescription())
        .version(serviceConfig.getVersion())
        .license((new License()).name(serviceConfig.getLicense()))
        .contact(contact);
  }

  private List<Tag> getTags() {

    List<Tag> tags = Optional.ofNullable(swaggerConfig)
            .map(ApiSwaggerConfig::getTags)
            .orElse(new ArrayList<>());

    tags.add(new Tag().name("monitoring").description("Operations in context to application monitoring"));
    return tags;
  }

  @Bean
  public OpenAPI documentation() {

    return new OpenAPI()
        .openapi("3.0.0")
        .components(getComponents())
        .security(getSecurityRequirements())
        .info(getInfo())
        .tags(getTags());
  }
}
