package com.infosys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// 
/**
 * The Class CorsConfig.
 *
 * @author icets
 */
@Configuration
public class CorsConfig {

	@Value("${spring.cors.allowedOriginPatterns}") 
	private String allowedOriginPatterns;
	
	@Value("${spring.cors.allowedHeaders}") 
	private String allowedHeader;
	
	@Value("${spring.cors.allowedMethods}") 
	private String allowedMethod;
   /**
    * Cors filter.
    *
    * @return the cors filter
    */
   @Bean
   public CorsFilter corsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowCredentials(true);
      config.addAllowedOriginPattern(allowedOriginPatterns);       
      config.addAllowedHeader(allowedHeader);
      config.addAllowedMethod(allowedMethod);
      source.registerCorsConfiguration("/api/*", config);
      return new CorsFilter(source);
   }

}