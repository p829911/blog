package me.p829911.blog.response;

import lombok.Builder;
import lombok.Getter;
import me.p829911.blog.domain.Post;

/**
 * 서비스 정책에 맞는 클래스
 */
@Getter
public class PostResponse {

  private final Long id;
  private final String title;
  private final String content;

  public PostResponse(Post post) {
    this.id = post.getId();
    this.title = post.getTitle();
    this.content = post.getContent();
  }

  @Builder
  public PostResponse(Long id, String title, String content) {
    this.id = id;
    this.title = title.substring(0, Math.min(title.length(), 10));
    this.content = content;
  }
}
