package com.github.prgrms.social.security;

import com.github.prgrms.social.model.commons.Id;
import com.github.prgrms.social.model.user.Email;
import com.github.prgrms.social.model.user.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 인증된 사용자를 표현한다.
 */
public class JwtAuthentication {

  public final Id<User, Long> id;

  public final Email email;

  // TODO 이름 프로퍼티 추가
  public final String name;

  JwtAuthentication(Long id, Email email, String name) {
    checkArgument(id != null, "id must be provided.");
    checkArgument(email != null, "email must be provided.");
    checkArgument(name != null, "name must be provided.");

    this.id = Id.of(User.class, id);
    this.email = email;
    this.name = name;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("id", id)
      .append("email", email)
      .append("name", name)
      .toString();
  }

}