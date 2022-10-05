package me.p829911.blog.controller;

import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.p829911.blog.request.PostCreate;
import me.p829911.blog.request.PostEdit;
import me.p829911.blog.request.PostSearch;
import me.p829911.blog.response.PostResponse;
import me.p829911.blog.service.PostService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @PostMapping("/posts")
  public void post(@RequestBody @Valid PostCreate request) {
    // Case1. 저장한 데이터 Entity -> response로 응답하기
    // Case2. 저장한 데이터의 primary id -> response로 응답하기
    // Client 에서는 수신한 id를 글 조회 API를 통해서 데이터를 수신받음
    // Case3. 응답 필요 없음 -> 클라이언트에서 모든 POST(글) 데이터 context를 잘 관리함
    postService.write(request);
  }

  @GetMapping("/posts/{postId}")
  public PostResponse get(@PathVariable Long postId) {
    return postService.get(postId);
  }

  @GetMapping("/posts")
  public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
    return postService.getList(postSearch);
  }

  @PatchMapping("/posts/{postId}")
  public PostResponse updatePost(@PathVariable Long postId, @RequestBody PostEdit request) {
    return postService.edit(postId, request);
  }

  @DeleteMapping("/posts/{postId}")
  public void deletePost(@PathVariable Long postId) {
    postService.delete(postId);
  }
}
