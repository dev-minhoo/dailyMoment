package com.cNerds.dailyMoment.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	/*
	 * @Autowired LoginIdPwValidator loginIdPwValidator;
	 */

	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/user/**").permitAll()    // LoadBalancer Chk
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/user/login")
                    .loginProcessingUrl("/user/loginAuth")
                    .usernameParameter("id")
                    .passwordParameter("pwd")
                    .defaultSuccessUrl("/family/1", true)
                    .permitAll()
                .and()
                    .logout();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/js/**","/static/css/**","/static/img/**","/static/frontend/**");
    }
    
	/*
	 * @Override public void configure(AuthenticationManagerBuilder auth) throws
	 * Exception { auth.userDetailsService(loginIdPwValidator); }
	 */
}
