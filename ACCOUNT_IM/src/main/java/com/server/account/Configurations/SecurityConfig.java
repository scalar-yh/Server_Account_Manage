package com.server.account.Configurations;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.server.account.Configurations.Handler.CustomAuthFailureHandler;
import com.server.account.Configurations.Handler.LoginSuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig{
	
	private static final int THE_MIN = 1800;
	
	private final LoginSuccessHandler customSuccessHandler;
	private final CustomAuthFailureHandler customFailureHandler;
	
	@Bean
	  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	      CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
	      requestHandler.setCsrfRequestAttributeName("_csrf");
	      return http
	    		  .csrf(csrf -> csrf
	                      .csrfTokenRequestHandler(requestHandler)
	                      .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
	                      .ignoringRequestMatchers("/login", "/logout/**", "/login/**", "/signup/**", "/signup", "/addsystem", "/searchshrs"
	                    		  , "/viewpassword", "/viewpassword/**", "/chpasswd", "/api/v1/**")
	    				  )
	    		  .authorizeHttpRequests(request -> request
	    				  .requestMatchers("/main").hasRole("ADMIN")
	    				  .requestMatchers("/signup").hasRole("ADMIN")
	    				  .requestMatchers("/addsystem").hasRole("ADMIN")
	    				  .requestMatchers("/addsystem").hasRole("ADMIN")
	    				  .requestMatchers("/viewpassword").hasRole("ADMIN")
	    				  .requestMatchers("/viewpassword/**").hasRole("ADMIN")
	    				  .requestMatchers("/api/v1/**").permitAll()
	                      .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
	                      .anyRequest().permitAll()
	    				  )
	              .formLogin((formLogin) -> formLogin
	                      .loginPage("/login")
	                      .loginProcessingUrl("/login")
	                      .usernameParameter("username")
	                      .passwordParameter("password")
	                      .defaultSuccessUrl("/main")
	                      .successHandler(customSuccessHandler)
	                      .failureHandler(customFailureHandler)
	            		  )
	              .sessionManagement((auth)->auth
	            		  .maximumSessions(1)
	            		  .maxSessionsPreventsLogin(true)
	            		  )
	              .logout((logout) -> logout
	                      .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	                      .logoutSuccessUrl("/login")
	                      .clearAuthentication(true)
	                      .invalidateHttpSession(true)
	                      .deleteCookies("JSESSIONID")
	                      .deleteCookies("XSRF-TOKEN")
	            		  )
	      .build();
	}
	
	@Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }// Register HttpSessionEventPublisher
	
	@Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }
	
	@Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
