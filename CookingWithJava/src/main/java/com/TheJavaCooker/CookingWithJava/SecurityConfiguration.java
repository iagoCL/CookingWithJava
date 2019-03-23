package com.TheJavaCooker.CookingWithJava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import com.TheJavaCooker.CookingWithJava.UserRepositoryAuthenticationProvider;

import javax.persistence.OrderBy;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    public UserRepositoryAuthenticationProvider authenticationProvider;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Public pages
        http.authorizeRequests().antMatchers("/").permitAll();
        http.authorizeRequests().antMatchers(("/index")).permitAll();

        http.authorizeRequests().antMatchers(("/login")).permitAll();
        http.authorizeRequests().antMatchers(("/formulario-login")).permitAll();
        http.authorizeRequests().antMatchers(("/formulario-registro")).permitAll();

        http.authorizeRequests().antMatchers(("/login-error")).permitAll();
        http.authorizeRequests().antMatchers(("/loginError")).permitAll();
        http.authorizeRequests().antMatchers(("/logout-succes")).permitAll();
        http.authorizeRequests().antMatchers(("/logoutSucces")).permitAll();

        http.authorizeRequests().antMatchers(("/usuario-{\\d+}")).permitAll();
        http.authorizeRequests().antMatchers(("/perfil-{\\d+}")).permitAll();

        http.authorizeRequests().antMatchers(("/busqueda")).permitAll();
        http.authorizeRequests().antMatchers(("/buscarReceta")).permitAll();
        http.authorizeRequests().antMatchers(("/buscar-receta")).permitAll();
        http.authorizeRequests().antMatchers(("/formulario-buscar-receta")).permitAll();

        http.authorizeRequests().antMatchers(("/receta-{\\d+}")).permitAll();
        http.authorizeRequests().antMatchers(("/receta-completa-{\\d+}")).permitAll();
        http.authorizeRequests().antMatchers(("/recetaCompleta-{\\d+}")).permitAll();

        http.authorizeRequests().antMatchers(("/recetas")).permitAll();
        http.authorizeRequests().antMatchers(("/recetas-favoritas-{\\d+}")).permitAll();
        http.authorizeRequests().antMatchers(("/recetas-creadas-{\\d+}")).permitAll();


        http.authorizeRequests().antMatchers(("/image/**")).permitAll();
        http.authorizeRequests().antMatchers(("/pdf/**")).permitAll();
        http.authorizeRequests().antMatchers(("/txt/**")).permitAll();

        http.authorizeRequests().antMatchers(("/css/**")).permitAll();
        http.authorizeRequests().antMatchers(("/fonts/**")).permitAll();
        http.authorizeRequests().antMatchers(("/icon-fonts/**")).permitAll();
        http.authorizeRequests().antMatchers(("/img/**")).permitAll();
        http.authorizeRequests().antMatchers(("/js/**")).permitAll();


        //Private pages
        http.authorizeRequests().anyRequest().authenticated();

        http.formLogin().loginPage("/login").permitAll();
        http.formLogin().loginProcessingUrl("/formulario-login").permitAll();
        http.formLogin().usernameParameter("nickLogin");
        http.formLogin().passwordParameter("contrasenaLogin");
        http.formLogin().defaultSuccessUrl("/perfil");
        http.formLogin().failureUrl("/loginError");

        http.logout().logoutUrl("/logout");
        http.logout().logoutSuccessUrl("/logout-succes");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }


}