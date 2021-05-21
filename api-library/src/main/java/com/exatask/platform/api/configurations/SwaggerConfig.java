package com.exatask.platform.api.configurations;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.tags.Tag;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {

  @Autowired
  private ApiServiceConfig serviceConfig;

  @Autowired
  private ApiSwaggerConfig swaggerConfig;

  private ApiResponse getApiFailureResponse() {

    Schema<Boolean> statusSchema = new Schema<>();
    statusSchema.type("boolean")
        .setDefault(false);

    Schema<String> messageTypeSchema = new Schema<>();
    messageTypeSchema.type("string")
        .setDefault("ERROR");

    Schema<Object> messageSchema = new Schema<>();
    messageSchema.type("object")
        .addProperties("type", messageTypeSchema)
        .addProperties("text", (new Schema<>()).type("string"))
        .addProperties("error_code", (new Schema<>()).type("string"));

    Schema<Object> stackTraceSchema = new Schema<>();
    stackTraceSchema.type("object")
        .addProperties("file", (new Schema<>()).type("string"))
        .addProperties("class", (new Schema<>()).type("string"))
        .addProperties("method", (new Schema<>()).type("string"))
        .addProperties("line", (new Schema<>()).type("integer"));

    Schema<Object> apiFailure = new Schema<>();
    apiFailure.type("object")
        .addProperties("status", statusSchema)
        .addProperties("message", messageSchema)
        .addProperties("invalid_attributes", (new Schema<>()).type("object"))
        .addProperties("extra_params", (new Schema<>()).type("object"))
        .addProperties("stack_trace", (new ArraySchema()).items(stackTraceSchema))
        .addProperties("exception_cause", (new Schema<>()).type("string"));

    Content responseContent = new Content();
    responseContent.addMediaType(org.springframework.http.MediaType.APPLICATION_JSON.toString(), (new MediaType()).schema(apiFailure));
    return (new ApiResponse()).content(responseContent);
  }

  private ApiResponse getApiSuccessResponse() {

    Schema<Boolean> statusSchema = new Schema<>();
    statusSchema.type("boolean")
        .setDefault(true);

    List<String> messageTypeEnum = new ArrayList<>();
    messageTypeEnum.add("SUCCESS");
    messageTypeEnum.add("WARNING");

    Schema<String> messageTypeSchema = new Schema<>();
    messageTypeSchema.type("string")
        .setDefault("SUCCESS");
    messageTypeSchema.setEnum(messageTypeEnum);

    Schema<Object> messageSchema = new Schema<>();
    messageSchema.type("object")
        .addProperties("type", messageTypeSchema)
        .addProperties("text", (new Schema<>()).type("string"));

    Schema<Object> apiSuccess = new Schema<>();
    apiSuccess.type("object")
        .addProperties("status", statusSchema)
        .addProperties("message", messageSchema);

    Content responseContent = new Content();
    responseContent.addMediaType(org.springframework.http.MediaType.APPLICATION_JSON.toString(), (new MediaType()).schema(apiSuccess));
    return (new ApiResponse()).content(responseContent);
  }

  private Components getComponents() {

    Components components = swaggerConfig.getComponents();
    if (ObjectUtils.isEmpty(components)) {
      components = new Components();
    }

    return components
        .addResponses("ApiFailure", getApiFailureResponse())
        .addResponses("ApiSuccess", getApiSuccessResponse());
  }

  private Info getInfo() {

    return new Info().title(serviceConfig.getName())
        .description(serviceConfig.getDescription())
        .version(serviceConfig.getVersion())
        .license((new License()).name(serviceConfig.getLicense()))
        .contact(swaggerConfig.getContact());
  }

  private List<Tag> getTags() {

    List<Tag> tags = swaggerConfig.getTags();
    if (CollectionUtils.isEmpty(tags)) {
      tags = new ArrayList<>();
    }

    tags.add(new Tag().name("monitoring").description("Operations in context to application monitoring"));
    return tags;
  }

  @Bean
  public OpenAPI documentation() {

    return new OpenAPI()
        .openapi("3.0.0")
        .components(getComponents())
        .info(getInfo())
        .tags(getTags());
  }
}
