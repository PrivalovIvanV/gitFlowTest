package com.example.demo.config;




import com.example.demo.servises.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PersonService provider;


/////////////////////////////////////////////////////////////////////////////
///Тут происходит настройка доступа к страниичкам////////////////////////////
/////////////////////////////////////////////////////////////////////////////
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/auth/**", "/catalog/**", "/static/**").permitAll()
                .antMatchers("/myLibrary", "/account/**", "/logout").hasRole("USER")
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/**").permitAll()
                .and()
                .formLogin().loginPage("/auth/login")
                .loginProcessingUrl("/log_in")
                .defaultSuccessUrl("/", true)
                .failureUrl("/auth/login?error")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");
    }















    //Настраивает конфигурацию
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(provider).passwordEncoder(getPasswordEncoder());
    }
    @Bean
    public PasswordEncoder getPasswordEncoder(){return new BCryptPasswordEncoder();
    }
}
