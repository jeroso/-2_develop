package com.github.prgrms.social.model.post;

import com.github.prgrms.social.model.user.Email;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Optional.ofNullable;

public class Writer {

  private final Email email;

  private final String name;

  public Writer(Email email) {
    this(email, null);
  }

  public Writer(Email email, String name) {
    checkArgument(email != null, "email must be provided.");

    this.email = email;
    this.name = name;
  }

  public Email getEmail() {
    return email;
  }

  public Optional<String> getName() {
    return ofNullable(name);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("email", email)
      .append("name", name)
      .toString();
  }

}