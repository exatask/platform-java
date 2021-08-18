package com.exatask.platform.dto.responses;

import com.exatask.platform.dto.responses.messages.ResponseMessage;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HttpErrorResponse extends AppResponse {

  private ResponseMessage message;

  private Map<String, String> invalidAttributes;

  private Map<String, Object> extraParams;

  private StackTraceElement[] stackTrace;

  private String exceptionCause;
}
