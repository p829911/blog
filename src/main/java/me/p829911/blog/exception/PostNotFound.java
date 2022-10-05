package me.p829911.blog.exception;

public class PostNotFound extends BaseException {

  private static final String MESSAGE = "존재하지 않는 글입니다.";

  public PostNotFound() {
    super(MESSAGE);
  }

  @Override
  public int getStatusCode() {
    return 404;
  }
}
