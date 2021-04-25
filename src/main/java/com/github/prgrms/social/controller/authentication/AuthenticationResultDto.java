package com.github.prgrms.social.controller.authentication;

import com.github.prgrms.social.controller.user.UserDto;
import com.github.prgrms.social.security.AuthenticationResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static org.springframework.beans.BeanUtils.copyProperties;

public class AuthenticationResultDto {

  private String apiToken;

  private UserDto user;

  public AuthenticationResultDto(AuthenticationResult source) {
    copyProperties(source, this);

    this.user = new UserDto(source.getUser());
  }

  public String getApiToken() {
    return apiToken;
  }

  public void setApiToken(String apiToken) {
    this.apiToken = apiToken;
  }

  public UserDto getUser() {
    return user;
  }

  public void setUser(UserDto user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("apiToken", apiToken)
      .append("user", user)
      .toString();
  }

}