package com.github.prgrms.social.service.user;

import com.github.prgrms.social.error.NotFoundException;
import com.github.prgrms.social.model.commons.Id;
import com.github.prgrms.social.model.user.ConnectedUser;
import com.github.prgrms.social.model.user.Email;
import com.github.prgrms.social.model.user.User;
import com.github.prgrms.social.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
public class UserService {

  private final PasswordEncoder passwordEncoder;

  private final UserRepository userRepository;

  public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @Transactional
  public User join(Email email, String password, String name) {
    checkArgument(isNotEmpty(password), "password must be provided.");
    checkArgument(
      password.length() >= 4 && password.length() <= 15,
      "password length must be between 4 and 15 characters."
    );

    User user = new User(email, passwordEncoder.encode(password), name);
    return insert(user);
  }

  @Transactional
  public User login(Email email, String password) {
    checkArgument(password != null, "password must be provided.");

    User user = findByEmail(email)
      .orElseThrow(() -> new NotFoundException(User.class, email));
    user.login(passwordEncoder, password);
    user.afterLoginSuccess();
    update(user);
    return user;
  }

  @Transactional(readOnly = true)
  public Optional<User> findById(Id<User, Long> userId) {
    checkArgument(userId != null, "userId must be provided.");

    return userRepository.findById(userId);
  }

  @Transactional(readOnly = true)
  public Optional<User> findByEmail(Email email) {
    checkArgument(email != null, "email must be provided.");

    return userRepository.findByEmail(email);
  }

  @Transactional(readOnly = true)
  public List<ConnectedUser> findAllConnectedUser(Id<User, Long> userId) {
    checkArgument(userId != null, "userId must be provided.");

    return userRepository.findAllConnectedUser(userId);
  }

  @Transactional(readOnly = true)
  public List<Id<User, Long>> findConnectedIds(Id<User, Long> userId) {
    checkArgument(userId != null, "userId must be provided.");

    return userRepository.findConnectedIds(userId);
  }

  private User insert(User user) {
    return userRepository.insert(user);
  }

  private void update(User user) {
    userRepository.update(user);
  }

}
