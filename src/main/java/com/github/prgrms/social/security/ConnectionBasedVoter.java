package com.github.prgrms.social.security;

import com.github.prgrms.social.model.commons.Id;
import com.github.prgrms.social.model.user.User;
import com.github.prgrms.social.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static org.apache.commons.lang3.ClassUtils.isAssignable;

public class ConnectionBasedVoter implements AccessDecisionVoter<FilterInvocation> {

  private UserService userService;

  private final RequestMatcher requestMatcher;

  private final Function<String, Id<User, Long>> idExtractor;


  public ConnectionBasedVoter(RequestMatcher requestMatcher, Function<String, Id<User, Long>> idExtractor) {
    Assert.notNull(requestMatcher, "requiresAuthorizationRequestMatcher must be provided.");
    Assert.notNull(idExtractor, "idExtractor must be provided.");
    this.requestMatcher = requestMatcher;
    this.idExtractor = idExtractor;
  }

  @Override
  public int vote(Authentication authentication, FilterInvocation fi, Collection<ConfigAttribute> attributes) {
    HttpServletRequest request = fi.getRequest();
    // TODO 접근 대상 리소스가 본인 또는 친구관계인지 확인하고 접근 혀용/거절 처리 구현
    // 감시 대상 URL인가?
    if (!requestMatcher.matches(request)) {
      return ACCESS_GRANTED;
    }
    // 처리할수 있는 인증정보인가?
    if (!isAssignable(JwtAuthenticationToken.class, authentication.getClass())) {
      return ACCESS_ABSTAIN;
    }
    // 인증정보와 URL에서 대상 ID를 추출하고..
    JwtAuthentication jwtAuth = (JwtAuthentication) authentication.getPrincipal();
    Id<User, Long> targetId = idExtractor.apply(request.getServletPath());
    // 대상 ID가 본인 자신인가?
    if (jwtAuth.id.equals(targetId)) {
      return ACCESS_GRANTED;
    }
    // 친구IDs에 대상 ID가 포함되 있나?
    List<Id<User, Long>> connectedIds = userService.findConnectedIds(jwtAuth.id);
    if (connectedIds.contains(targetId)) {
      return ACCESS_GRANTED;
    }
    return ACCESS_DENIED;
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