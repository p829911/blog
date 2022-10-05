package me.p829911.blog.repository;

import java.util.List;
import me.p829911.blog.domain.Post;
import me.p829911.blog.request.PostSearch;

public interface PostRepositoryCustom {

  List<Post> getList(PostSearch postSearch);
}
