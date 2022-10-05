package me.p829911.blog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import me.p829911.blog.domain.Post;
import me.p829911.blog.repository.PostRepository;
import me.p829911.blog.request.PostCreate;
import me.p829911.blog.request.PostEdit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PostRepository postRepository;

  @BeforeEach
  void clean() {
    postRepository.deleteAll();
  }

  @Test
  @DisplayName("/posts 요청시 Hello World를 출력한다.")
  void test() throws Exception {
    // given
    PostCreate request = PostCreate.builder()
        .title("제목입니다.")
        .content("내용입니다.")
        .build();

    String requestString = objectMapper.writeValueAsString(request);

    // expected
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/posts")
                .contentType(APPLICATION_JSON)
                .content(requestString))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(""))
        .andDo(print());
  }

  @Test
  @DisplayName("/posts 요청시 title 값은 필수다.")
  void test2() throws Exception {
    // given
    PostCreate request = PostCreate.builder()
        .content("내용입니다.")
        .build();

    String requestString = objectMapper.writeValueAsString(request);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/posts")
                .contentType(APPLICATION_JSON)
                .content(requestString))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
        .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
        .andDo(print());
  }

  @Test
  @DisplayName("/posts 요청시 db에 값이 저장된다.")
  void test3() throws Exception {
    // given
    PostCreate request = PostCreate.builder()
        .title("제목입니다.")
        .content("내용입니다.")
        .build();

    String requestString = objectMapper.writeValueAsString(request);

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/posts")
                .contentType(APPLICATION_JSON)
                .content(requestString))
        .andExpect(status().isOk())
        .andDo(print());

    // then
    assertEquals(1L, postRepository.count());
    Post post = postRepository.findAll().get(0);
    assertEquals("제목입니다.", post.getTitle());
    assertEquals("내용입니다.", post.getContent());
  }

  @Test
  @DisplayName("글 한 개 조회")
  void test4() throws Exception {
    // given
    Post post = Post.builder()
        .title("123456789012345")
        .content("bar")
        .build();
    postRepository.save(post);

    // expected ( when + then )`
    mockMvc.perform(get("/posts/{postId}", post.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(post.getId()))
        .andExpect(jsonPath("$.title").value("1234567890"))
        .andExpect(jsonPath("$.content").value("bar"))
        .andDo(print());
  }

  @Test
  @DisplayName("글 여러개 조회")
  void test5() throws Exception {
    // given
    List<Post> requestPosts = IntStream.range(1, 31)
        .mapToObj(i -> Post.builder()
            .title("foo " + i)
            .content("bar " + i)
            .build()).collect(Collectors.toList());

    postRepository.saveAll(requestPosts);

    // expected ( when + then )
    mockMvc.perform(get("/posts?page=1&size=10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(10))
        .andExpect(jsonPath("$[0].title").value("foo 30"))
        .andExpect(jsonPath("$[0].content").value("bar 30"))
        .andDo(print());
  }

  @Test
  @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다.")
  void test6() throws Exception {
    // given
    List<Post> requestPosts = IntStream.range(1, 31)
        .mapToObj(i -> Post.builder()
            .title("foo " + i)
            .content("bar " + i)
            .build()).collect(Collectors.toList());

    postRepository.saveAll(requestPosts);

    // expected ( when + then )
    mockMvc.perform(get("/posts"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(10))
        .andExpect(jsonPath("$[0].title").value("foo 30"))
        .andExpect(jsonPath("$[0].content").value("bar 30"))
        .andDo(print());
  }

  @Test
  @DisplayName("글 제목 수정")
  void test7() throws Exception {
    // given
    Post post = Post.builder()
        .title("호돌맨")
        .content("반포자이")
        .build();

    postRepository.save(post);

    PostEdit postEdit = PostEdit.builder()
        .title("foo")
        .build();

    String request = objectMapper.writeValueAsString(postEdit);

    // expected ( when + then )
    mockMvc.perform(patch("/posts/{postId}", post.getId())
            .contentType(APPLICATION_JSON)
            .content(request)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("foo"))
        .andExpect(jsonPath("$.content").value("반포자이"))
        .andDo(print());
  }

  @Test
  @DisplayName("글 삭제")
  void test8() throws Exception {
    // given
    Post post = Post.builder()
        .title("호돌맨")
        .content("반포자이")
        .build();

    postRepository.save(post);

    // when
    mockMvc.perform(delete("/posts/{postId}", post.getId())
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("존재하지 않는 게시글 조회")
  void test9() throws Exception {
    // expected
    mockMvc.perform(delete("/posts/{postId}", 1L)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andDo(print());
  }
}