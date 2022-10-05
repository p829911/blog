package me.p829911.blog.request;

import lombok.Getter;

@Getter
public class PostSearch {

  private static final int MAX_SIZE = 2000;

  private int page = 1;

  private int size = 10;

  public long getOffset() {
    return (long) (Math.max(page, 1) - 1) * Math.min(size, MAX_SIZE);
  }

  public PostSearch() {

  }

  public PostSearch(int page, int size) {
    this.page = page;
    this.size = size;
  }
}
