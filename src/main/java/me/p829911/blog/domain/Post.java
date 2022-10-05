package me.p829911.blog.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  @Lob
  private String content;

  @Builder
  public Post(String title, String content) {
    this.title = title;
    this.content = content;
  }

  private static final int EDS = 30;

  public void updateTitle(String title) {
    this.title = title;
  }

  public void updateContent(String content) {
    this.content = content;
  }

  public PostEditor.PostEditorBuilder toEditor() {
    return PostEditor.builder()
        .title(title)
        .content(content);
  }

  public void edit(PostEditor postEditor) {
    title = postEditor.getTitle();
    content = postEditor.getContent();
  }
}
