package me.p829911.blog.exception;

public class InvalidRequest extends BaseException {

  private static final String MESSAGE = "잘못된 요청 입니다.";

  public InvalidRequest() {
    super(MESSAGE);
  }

  @Override
  public int getStatusCode() {
    return 400;
  }
}
