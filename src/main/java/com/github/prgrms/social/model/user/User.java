package com.github.prgrms.social.model.user;

import com.github.prgrms.social.security.Jwt;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

public class User {

  private final Long seq;

  private final Email email;

  // TODO 이름 프로퍼티 추가
  private final String name;

  private String password;

  private int loginCount;

  private LocalDateTime lastLoginAt;

  private final LocalDateTime createAt;

  public User(Email email, String password, String name) {
    this(null, email, name, password, 0, null, null);
  }

  public User(Long seq, Email email, String name, String password, int loginCount, LocalDateTime lastLoginAt, LocalDateTime createAt) {
    checkArgument(email != null, "email must be provided.");
    checkArgument(password != null, "password must be provided.");

    this.seq = seq;
    this.email = email;
    this.name = name;
    this.password = password;
    this.loginCount = loginCount;
    this.lastLoginAt = lastLoginAt;
    this.createAt = defaultIfNull(createAt, now());
  }

  public void login(PasswordEncoder passwordEncoder, String credentials) {
    if (!passwordEncoder.matches(credentials, password))
      throw new IllegalArgumentException("Bad credential");
  }

  public void afterLoginSuccess() {
    loginCount++;
    lastLoginAt = now();
  }

  public String newApiToken(Jwt jwt, String[] roles) {
    // TODO jwt 토큰에 이름 프로퍼티 추가
    Jwt.Claims claims = Jwt.Claims.of(seq, email, roles);
    return jwt.newToken(claims);
  }

  public Long getSeq() {
    return seq;
  }

  public Email getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }

  public int getLoginCount() {
    return loginCount;
  }

  public Optional<LocalDateTime> getLastLoginAt() {
    return ofNullable(lastLoginAt);
  }

  public LocalDateTime getCreateAt() {
    return createAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(seq, user.seq);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seq);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("seq", seq)
      .append("email", email)
      .append("name", name)
      .append("password", "[PROTECTED]")
      .append("loginCount", loginCount)
      .append("lastLoginAt", lastLoginAt)
      .append("createAt", createAt)
      .toString();
  }

  static public class Builder {
    private Long seq;
    private Email email;
    private String name;
    private String password;
    private int loginCount;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createAt;

    public Builder() {
    }

    public Builder(User user) {
      this.seq = user.seq;
      this.email = user.email;
      this.name = user.name;
      this.password = user.password;
      this.loginCount = user.loginCount;
      this.lastLoginAt = user.lastLoginAt;
      this.createAt = user.createAt;
    }

    public Builder seq(Long seq) {
      this.seq = seq;
      return this;
    }

    public Builder email(Email email) {
      this.email = email;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Builder loginCount(int loginCount) {
      this.loginCount = loginCount;
      return this;
    }

    public Builder lastLoginAt(LocalDateTime lastLoginAt) {
      this.lastLoginAt = lastLoginAt;
      return this;
    }

    public Builder createAt(LocalDateTime createAt) {
      this.createAt = createAt;
      return this;
    }

    public User build() {
      return new User(seq, email, name, password, loginCount, lastLoginAt, createAt);
    }
  }

}