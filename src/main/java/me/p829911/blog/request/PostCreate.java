package me.p829911.blog.request;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PostCreate {

  @NotBlank(message = "타이틀을 입력해주세요.")
  private String title;

  @NotBlank(message = "콘텐츠를 입력해주세요.")
  private String content;

  @Builder
  public PostCreate(String title, String content) {
    this.title = title;
    this.content = content;
  }
}
