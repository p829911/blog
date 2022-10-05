package me.p829911.blog.response;

import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

/**
 * {
 *   "code": "400",
 *   "message": "잘못된 요청입니다."
 * }
 */
@Getter
//@JsonInclude(value = Include.NON_EMPTY)
public class ErrorResponse {

  private final String code;
  private final String message;
  private final Map<String, String> validation = new HashMap<>();

  @Builder
  public ErrorResponse(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public void addValidation(String field, String message) {
    validation.put(field, message);
  }
}
