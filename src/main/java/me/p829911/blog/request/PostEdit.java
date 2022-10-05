package me.p829911.blog.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PostEdit {

  private final String title;

  private final String content;

  @Builder
  public PostEdit(String title, String content) {
    this.title = title;
    this.content = content;
  }
}
