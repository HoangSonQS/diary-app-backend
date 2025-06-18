package com.mydiary.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// @Component để Spring biết đây là một Bean và quản lý nó
@Component
// Kế thừa OncePerRequestFilter để đảm bảo filter này chỉ chạy một lần cho mỗi request
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // Đây là phương thức chính của Filter, nó sẽ được gọi cho mọi request gửi đến
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. Lấy JWT token từ request
        String token = getTokenFromRequest(request);

        // 2. Xác thực token
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            // 3. Lấy username từ token
            String username = jwtTokenProvider.getUsernameFromToken(token);

            // 4. Tải thông tin user từ database
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 5. Tạo một đối tượng Authentication
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null, // Chúng ta không cần credentials (mật khẩu) ở đây
                    userDetails.getAuthorities()
            );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 6. "Set" thông tin cho Security Context -> Báo cho Spring Security biết user này đã được xác thực
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // Chuyển request và response cho filter tiếp theo trong chuỗi
        filterChain.doFilter(request, response);
    }

    // Hàm phụ để lấy token từ "Authorization" header
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // Kiểm tra xem header có chứa token theo dạng "Bearer <token>" không
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Bỏ đi chữ "Bearer " để lấy phần token
        }

        return null;
    }
}