package com.example.sarafan.config;

import com.example.sarafan.model.User;
import com.example.sarafan.repo.UserDetailsRepo;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login**", "/js/**", "/error**")
                .permitAll()
                .anyRequest()
                .authenticated().and().logout().logoutSuccessUrl("/").permitAll()
                .and()
                .csrf().disable();
    }
    @Bean
    public PrincipalExtractor principalExtractor(UserDetailsRepo userDetailsRepo){
        return map -> {
            Set<String> strings = map.keySet();
            for (String key:strings
                 ) {
                System.out.println(key + ": " + map.get(key));
            }

            String id = (String)map.get("sub");

           User user = userDetailsRepo.findById(id).orElseGet(() -> {
                User newUser = new User();
                newUser.setId((String) map.get("sub"));
                newUser.setEmail((String) map.get("email"));
                newUser.setName((String) map.get("name"));
                newUser.setGender((String)map.get("gender"));
                newUser.setLocale((String)map.get("locale"));
                newUser.setUserpic((String)map.get("picture"));
                return newUser;
            });
           user.setLastVisit(LocalDateTime.now());
           userDetailsRepo.save(user);
           return user;
        };
    }
}
