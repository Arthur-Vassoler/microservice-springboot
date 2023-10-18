package br.com.microlog.user.communs;

import br.com.microlog.user.models.UserModel;
import br.com.microlog.user.repositories.UserRepository;
import br.com.microlog.user.services.AuthorizationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
  private final JwtUtils jwtUtils;
  private final AuthorizationService authorizationService;

  public AuthTokenFilter(JwtUtils jwtUtils, AuthorizationService authorizationService) {
    super();
    this.jwtUtils = jwtUtils;
    this.authorizationService = authorizationService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {

    try {
      String jwt = jwtUtils.getJwtFromRequest(request);

      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        String username = jwtUtils.getEmailFromJwtToken(jwt);
        UserModel user = (UserModel) authorizationService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(
            user, null, user.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception ignored) {}

    filterChain.doFilter(request, response);
  }
}