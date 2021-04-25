package com.github.prgrms.social.service.post;

import com.github.prgrms.social.error.NotFoundException;
import com.github.prgrms.social.model.commons.Id;
import com.github.prgrms.social.model.post.Post;
import com.github.prgrms.social.model.user.User;
import com.github.prgrms.social.repository.post.PostRepository;
import com.github.prgrms.social.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

@Service
public class PostService {

  private final UserRepository userRepository;

  private final PostRepository postRepository;

  public PostService(UserRepository userRepository, PostRepository postRepository) {
    this.userRepository = userRepository;
    this.postRepository = postRepository;
  }

  @Transactional
  public Post write(Post post) {
    return insert(post);
  }

  @Transactional
  public Post modify(Post post) {
    update(post);
    return post;
  }

  @Transactional(readOnly = true)
  public Optional<Post> findById(Id<Post, Long> postId) {
    checkArgument(postId != null, "postId must be provided.");

    return postRepository.findById(postId);
  }

  @Transactional(readOnly = true)
  public List<Post> findAll(Id<User, Long> userId) {
    checkArgument(userId != null, "userId must be provided.");

    userRepository.findById(userId)
      .orElseThrow(() -> new NotFoundException(User.class, userId));
    return postRepository.findAll(userId);
  }

  private Post insert(Post post) {
    return postRepository.insert(post);
  }

  private void update(Post post) {
    postRepository.update(post);
  }

}
