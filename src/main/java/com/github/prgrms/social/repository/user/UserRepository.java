package com.github.prgrms.social.repository.user;

import com.github.prgrms.social.model.commons.Id;
import com.github.prgrms.social.model.user.ConnectedUser;
import com.github.prgrms.social.model.user.Email;
import com.github.prgrms.social.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

  User insert(User user);

  void update(User user);

  Optional<User> findById(Id<User, Long> userId);

  Optional<User> findByEmail(Email email);

  List<ConnectedUser> findAllConnectedUser(Id<User, Long> userId);

  List<Id<User, Long>> findConnectedIds(Id<User, Long> userId);

}
