package com.github.prgrms.social.security;

import com.github.prgrms.social.error.NotFoundException;
import com.github.prgrms.social.model.user.Email;
import com.github.prgrms.social.model.user.Role;
import com.github.prgrms.social.model.user.User;
import com.github.prgrms.social.service.user.UserService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.apache.commons.lang3.ClassUtils.isAssignable;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

/**
 * {@link org.springframework.security.authentication.ProviderManager#providers} 목록에 포함되 있다.
 * ({@link com.github.prgrms.social.configure.WebSecurityConfigure#configureAuthentication} 에서 추가됨)
 */
public class JwtAuthenticationProvider implements AuthenticationProvider {

  private final Jwt jwt;

  private final UserService userService;

  public JwtAuthenticationProvider(Jwt jwt, UserService userService) {
    this.jwt = jwt;
    this.userService = userService;
  }

  /**
   * {@link org.springframework.security.authentication.ProviderManager#authenticate} 메소드에서 호출된다.
   *
   * true 를 리턴하면 현재 Provider 에서 인증 처리를 할 수 있음을 의미한다.
   * 본 Provider 에서는 {@link JwtAuthenticationToken} 타입의 {@link Authentication} 를 처리할 수 있다.
   */
  @Override
  public boolean supports(Class<?> authentication) {
    return isAssignable(JwtAuthenticationToken.class, authentication);
  }

  /**
   * {@link org.springframework.security.authentication.ProviderManager#authenticate} 메소드에서 호출된다.
   *
   * null 이 아닌 값을 반환하면 인증 처리가 완료된다.
   */
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
    return processUserAuthentication(authenticationToken.authenticationRequest());
  }

  private Authentication processUserAuthentication(AuthenticationRequest request) {
    try {
      User user = userService.login(new Email(request.getPrincipal()), request.getCredentials());
      JwtAuthenticationToken authenticated =
        // 응답용 Authentication 인스턴스를 생성한다.
        // JwtAuthenticationToken.principal 부분에는 JwtAuthentication 인스턴스가 set 된다.
        // 로그인 완료 전 JwtAuthenticationToken.principal 부분은 Email 인스턴스가 set 되어 있었다.
        new JwtAuthenticationToken(new JwtAuthentication(user.getSeq(), user.getEmail()), null, createAuthorityList(Role.USER.value()));
      // JWT 값을 생성한다.
      // 권한은 ROLE_USER 를 부여한다.
      String apiToken = user.newApiToken(jwt, new String[]{Role.USER.value()});
      authenticated.setDetails(new AuthenticationResult(apiToken, user));
      return authenticated;
    } catch (NotFoundException e) {
      throw new UsernameNotFoundException(e.getMessage());
    } catch (IllegalArgumentException e) {
      throw new BadCredentialsException(e.getMessage());
    } catch (DataAccessException e) {
      throw new AuthenticationServiceException(e.getMessage(), e);
    }
  }

}