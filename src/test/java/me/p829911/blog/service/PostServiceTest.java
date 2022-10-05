package me.p829911.blog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import me.p829911.blog.domain.Post;
import me.p829911.blog.exception.PostNotFound;
import me.p829911.blog.repository.PostRepository;
import me.p829911.blog.request.PostCreate;
import me.p829911.blog.request.PostEdit;
import me.p829911.blog.request.PostSearch;
import me.p829911.blog.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostServiceTest {

  @Autowired
  private PostService postService;

  @Autowired
  private PostRepository postRepository;

  @BeforeEach
  void clean() {
    postRepository.deleteAll();
  }

  @Test
  @DisplayName("글 작성")
  void test() {
    // given
    PostCreate request = PostCreate.builder()
        .title("제목입니다.")
        .content("내용입니다.")
        .build();

    // when
    postService.write(request);

    // then
    assertEquals(1L, postRepository.count());
    Post post = postRepository.findAll().get(0);
    assertThat(post.getTitle()).isEqualTo(request.getTitle());
    assertThat(post.getContent()).isEqualTo(request.getContent());
  }

  @Test
  @DisplayName("글 한 개 조회")
  void test2() {
    // given
    Post requestPost = Post.builder()
        .title("foo")
        .content("bar")
        .build();
    postRepository.save(requestPost);

    // when
    PostResponse response = postService.get(requestPost.getId());

    // then
    assertNotNull(requestPost);
    assertEquals(requestPost.getId(), response.getId());
    assertThat("foo").isEqualTo(requestPost.getTitle());
    assertThat("bar").isEqualTo(requestPost.getContent());
  }

  @Test
  @DisplayName("글 1페이지 조회")
  void test3() {
    // given
    List<Post> requestPosts = IntStream.range(1, 21)
        .mapToObj(i -> Post.builder()
            .title("foo " + i)
            .content("bar " + i)
            .build()).collect(Collectors.toList());

    postRepository.saveAll(requestPosts);

    PostSearch postSearch = new PostSearch();

    // when
    List<PostResponse> posts = postService.getList(postSearch);

    // then
    assertEquals(10, posts.size());
    assertEquals("foo 20", posts.get(0).getTitle());
  }

  @Test
  @DisplayName("글 제목 수정")
  void test4() {
    // given
    Post post = Post.builder()
        .title("호돌맨")
        .content("반포자이")
        .build();

    postRepository.save(post);

    PostEdit postEdit = PostEdit.builder()
        .title("foo")
        .build();

    // when
    postService.edit(post.getId(), postEdit);

    // then
    Post changePost = postRepository.findById(post.getId())
        .orElseThrow(PostNotFound::new);

    assertEquals("foo", changePost.getTitle());
    assertEquals("반포자이", changePost.getContent());
  }

  @Test
  @DisplayName("게시글 삭제")
  void test5() {
    // given
    Post post = Post.builder()
        .title("호돌맨")
        .content("반포자이")
        .build();

    postRepository.save(post);

    // when
    postService.delete(post.getId());

    // then
    assertEquals(0, postRepository.count());
  }

  @Test
  @DisplayName("글 1개 조회 실패")
  void test6() {
    // given
    Post post = Post.builder()
        .title("foo")
        .content("bar")
        .build();

    postRepository.save(post);

    // expected
    PostNotFound e = assertThrows(PostNotFound.class,
        () -> postService.get(post.getId() + 1));
    assertEquals("존재하지 않는 글입니다.", e.getMessage());
  }

}