package bible.bible.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * CORS 설정 - 프론트엔드(React)와 도메인/포트가 다를 때 API 요청 허용
 */
@Configuration
public class CorsConfig {

    @Value("${cors.allowed-origins:}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
    private String allowedMethods;

    @Value("${cors.allowed-headers:*}")
    private String allowedHeaders;

    @Value("${cors.max-age:3600}")
    private long maxAge;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(originPatterns());
        config.setAllowedMethods(Arrays.asList(allowedMethods.split("\\s*,\\s*")));
        config.setAllowedHeaders(Arrays.asList(allowedHeaders.split("\\s*,\\s*")));
        config.setMaxAge(maxAge);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    private List<String> originPatterns() {
        if (allowedOrigins == null || allowedOrigins.isBlank() || "*".equals(allowedOrigins.trim())) {
            return List.of("*");
        }
        return Arrays.asList(allowedOrigins.split("\\s*,\\s*"));
    }
}
