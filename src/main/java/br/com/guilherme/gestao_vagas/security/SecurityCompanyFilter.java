package br.com.guilherme.gestao_vagas.security;

import br.com.guilherme.gestao_vagas.providers.JWTCompanyProvider;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class SecurityCompanyFilter extends OncePerRequestFilter {

    /*
    * O JWTCompanyProvider disponibiliza o serviço de validação de token
    */
    @Autowired
    private JWTCompanyProvider jwtCompanyProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

//        SecurityContextHolder.getContext().setAuthentication(null);

        String token = request.getHeader("Authorization");

        if (request.getRequestURI().startsWith("/company")){
            if (token != null){
                DecodedJWT decodedToken = jwtCompanyProvider.validateToken(token);

                if (decodedToken == null){
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                List<String> roles = decodedToken.getClaim("roles").asList(String.class);
                List<SimpleGrantedAuthority> grants = roles.stream().map(
                        role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())
                ).toList();

                request.setAttribute("company_id", decodedToken.getSubject());

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(decodedToken.getSubject(), null, grants);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }



        filterChain.doFilter(request, response);

    }
}
