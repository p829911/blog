package me.p829911.blog.repository;

import static me.p829911.blog.domain.QPost.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import me.p829911.blog.domain.Post;
import me.p829911.blog.request.PostSearch;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;


  @Override
  public List<Post> getList(PostSearch postSearch) {
    return jpaQueryFactory.selectFrom(post)
        .limit(postSearch.getSize())
        .offset(postSearch.getOffset())
        .orderBy(post.id.desc())
        .fetch();
  }
}
