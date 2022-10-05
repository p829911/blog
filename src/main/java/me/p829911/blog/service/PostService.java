package me.p829911.blog.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.p829911.blog.domain.Post;
import me.p829911.blog.domain.PostEditor.PostEditorBuilder;
import me.p829911.blog.exception.PostNotFound;
import me.p829911.blog.repository.PostRepository;
import me.p829911.blog.request.PostCreate;
import me.p829911.blog.request.PostEdit;
import me.p829911.blog.request.PostSearch;
import me.p829911.blog.response.PostResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;

  public void write(PostCreate postCreate) {
    Post post = Post.builder()
        .title(postCreate.getTitle())
        .content(postCreate.getContent())
        .build();

    postRepository.save(post);
  }

  public PostResponse get(Long id) {
    Post post = postRepository.findById(id)
        .orElseThrow(PostNotFound::new);

    return PostResponse.builder()
        .id(post.getId())
        .title(post.getTitle())
        .content(post.getContent())
        .build();
  }

  // 글이 너무 많은 경우 -> 비용이 너무 많이 든다.
  // 글이 1억개 정도 있으면 DB에서 글을 모두 조회하는 경우, DB가 뻗을 수 있다.
  // DB에서 애플리케이션 서버로 전달하는 시간, 트래픽 비용 등이 많이 발생할 수 있다.
  public List<PostResponse> getList(PostSearch postSearch) {
    // web -> page 1 -> 0
//    Pageable pageable = PageRequest.of(page, 5, Sort.by("id").descending());
    return postRepository.getList(postSearch).stream()
        .map(PostResponse::new)
        .collect(Collectors.toList());
  }

  @Transactional
  public PostResponse edit(Long id, PostEdit postEdit) {
    Post post = postRepository.findById(id)
        .orElseThrow(PostNotFound::new);

    PostEditorBuilder editorBuilder = post.toEditor();

    if (postEdit.getTitle() != null) {
      editorBuilder.title(postEdit.getTitle());
    }

    if (postEdit.getContent() != null) {
      editorBuilder.content(postEdit.getContent());
    }

    post.edit(editorBuilder.build());

    return new PostResponse(post);
  }

  public void delete(Long id) {
    Post post = postRepository.findById(id)
        .orElseThrow(PostNotFound::new);
    postRepository.delete(post);
  }
}
