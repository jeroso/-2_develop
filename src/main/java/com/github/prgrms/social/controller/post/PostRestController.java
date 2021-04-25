package com.github.prgrms.social.controller.post;

import com.github.prgrms.social.controller.ApiResult;
import com.github.prgrms.social.model.commons.Id;
import com.github.prgrms.social.model.post.Writer;
import com.github.prgrms.social.model.user.User;
import com.github.prgrms.social.security.JwtAuthentication;
import com.github.prgrms.social.service.post.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.github.prgrms.social.controller.ApiResult.OK;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("api")
public class PostRestController {

  private final PostService postService;

  public PostRestController(PostService postService) {
    this.postService = postService;
  }

  @PostMapping(path = "post")
  public ApiResult<PostDto> posting(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @RequestBody PostingRequest request
  ) {
    return OK(
      new PostDto(
        postService.write(
          request.newPost(authentication.id, new Writer(authentication.email))
        )
      )
    );
  }

  @GetMapping(path = "user/{userId}/post/list")
  public ApiResult<List<PostDto>> posts(@PathVariable Long userId) {
    return OK(
      postService.findAll(Id.of(User.class, userId)).stream()
        .map(PostDto::new)
        .collect(toList())
    );
  }

}