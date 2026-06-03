package backend.nemra.config;

import backend.nemra.shared.utils.jwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtConfig extends OncePerRequestFilter {

    private final jwtUtils jwtHelper;

    public JwtConfig(jwtUtils token) {
        this.jwtHelper = token;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtHelper.resolveToken(request);

        if  (token != null && jwtHelper.validateToken(token)) {
            String uuid = jwtHelper.getUuid(token);
            String role = jwtHelper.getRole(token);

            List<GrantedAuthority> authorities = new ArrayList<>();
            if (role != null && !role.isEmpty()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }

            UsernamePasswordAuthenticationToken auth  = new UsernamePasswordAuthenticationToken(
                    uuid,
                    null,
                    authorities
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}