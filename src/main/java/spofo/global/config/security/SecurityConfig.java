package spofo.global.config.security;

import static java.util.Arrays.asList;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import spofo.global.config.security.filter.TokenAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 어떤 요청이던 access 클래스로 전달한다.
        http.httpBasic(config -> config.disable())
                .csrf(config -> config.disable())
                .sessionManagement(config -> config.sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated());

        // withDefaults()를 사용하면 corsConfigurationSource 이름을 가진 빈을 찾아서 설정해준다.
        http.cors(withDefaults());
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * https://docs.spring.io/spring-security/reference/servlet/integrations/cors.html
     * @return
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(asList("http://spofo.net", "http://localhost:5173/"));
        configuration.addAllowedHeader("*");
        configuration.setAllowedMethods(asList("*"));
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
