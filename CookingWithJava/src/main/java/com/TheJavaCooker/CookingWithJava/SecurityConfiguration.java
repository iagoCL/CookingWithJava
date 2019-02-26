package com.TheJavaCooker.CookingWithJava;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

import javax.persistence.OrderBy;

@Configuration
public class SecurityConfiguration extends WebSecurityConfiguration {



    protected void configure(HttpSecurity http) throws Exception {
        //Public pages
        http.authorizeRequests().antMatchers("/").permitAll();
        http.authorizeRequests().antMatchers(("/login")).permitAll();
        http.authorizeRequests().antMatchers(("/index")).permitAll();
        http.authorizeRequests().antMatchers(("/busquedaReceta")).permitAll();
        http.authorizeRequests().antMatchers(("/perfil")).permitAll();
        http.authorizeRequests().antMatchers(("/receta-completa")).permitAll();
        http.authorizeRequests().antMatchers(("/recetas")).permitAll();

        //Private pages
        http.authorizeRequests().anyRequest().authenticated();

        http.formLogin().loginPage("/login");
        http.formLogin().usernameParameter("username");
        http.formLogin().passwordParameter("password");
        http.formLogin().defaultSuccessUrl("/perfil");
        http.formLogin().failureUrl("/loginError");


        http.logout().logoutUrl("/logout");
        http.logout().logoutSuccessUrl("/");

        http.csrf().disable();




    }


    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication().withUser("user").password("pass");
    }



}
