package com.github.prgrms.social.security;

import com.github.prgrms.social.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

import static org.apache.commons.lang3.ClassUtils.isAssignable;

public class ConnectionBasedVoter implements AccessDecisionVoter<FilterInvocation> {

  private UserService userService;

  @Override
  public int vote(Authentication authentication, FilterInvocation fi, Collection<ConfigAttribute> attributes) {
    HttpServletRequest request = fi.getRequest();
    // TODO 접근 대상 리소스가 본인 또는 친구관계인지 확인하고 접근 혀용/거절 처리 구현
    return ACCESS_GRANTED;
  }

  @Override
  public boolean supports(ConfigAttribute attribute) {
    return true;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return isAssignable(FilterInvocation.class, clazz);
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

}