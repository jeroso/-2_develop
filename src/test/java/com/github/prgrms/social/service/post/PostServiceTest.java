package com.github.prgrms.social.service.post;

import com.github.prgrms.social.model.commons.Id;
import com.github.prgrms.social.model.post.Post;
import com.github.prgrms.social.model.post.Writer;
import com.github.prgrms.social.model.user.Email;
import com.github.prgrms.social.model.user.User;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostServiceTest {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private PostService postService;

  private Id<User, Long> userId;

  @BeforeAll
  void setUp() {
    userId = Id.of(User.class, 1L);
  }

  @Test
  @Order(1)
  void 포스트를_작성한다() {
    Writer writer = new Writer(new Email("test00@gmail.com"));
    String contents = randomAlphabetic(40);
    Post post = postService.write(new Post(userId, writer, contents));
    assertThat(post, is(notNullValue()));
    assertThat(post.getSeq(), is(notNullValue()));
    assertThat(post.getContents(), is(contents));
    log.info("Written post: {}", post);
  }

  @Test
  @Order(2)
  void 포스트를_수정한다() {
    Post post = postService.findById(Id.of(Post.class, 1L)).orElse(null);
    assertThat(post, is(notNullValue()));
    String contents = randomAlphabetic(40);
    post.modify(contents);
    postService.modify(post);
    assertThat(post.getContents(), is(contents));
    log.info("Modified post: {}", post);
  }

  @Test
  @Order(3)
  void 포스트_목록을_조회한다() {
    List<Post> posts = postService.findAll(userId);
    assertThat(posts, is(notNullValue()));
    assertThat(posts.size(), is(4));
  }

}