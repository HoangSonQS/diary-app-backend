package com.mydiary.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService; // Sử dụng UserDetailsService interface

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Lấy JWT token từ request header
        String token = getTokenFromRequest(request);

        // 2. Xác thực token
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            // 3. Nếu token hợp lệ, lấy username từ token
            String username = jwtTokenProvider.getUsernameFromToken(token);

            // 4. Tải thông tin người dùng (UserDetails) từ database
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 5. Tạo một đối tượng Authentication
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null, // Không cần credentials (mật khẩu)
                    userDetails.getAuthorities()
            );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 6. Đặt đối tượng Authentication vào SecurityContext
            // Đây là bước quan trọng nhất, báo cho Spring Security biết user này đã được xác thực
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        // Nếu token không hợp lệ hoặc không có, filter sẽ không làm gì cả,
        // và yêu cầu sẽ đi tiếp. Nếu endpoint yêu cầu xác thực, nó sẽ bị từ chối ở bước sau.

        filterChain.doFilter(request, response);
    }

    // Hàm phụ để trích xuất token từ "Authorization" header
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}