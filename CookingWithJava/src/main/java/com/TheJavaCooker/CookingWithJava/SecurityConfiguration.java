package com.TheJavaCooker.CookingWithJava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import com.TheJavaCooker.CookingWithJava.UserRepositoryAuthenticationProvider;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    public UserRepositoryAuthenticationProvider authenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Public pages
        http.authorizeRequests().antMatchers("/").permitAll();
        http.authorizeRequests().antMatchers(("/index")).permitAll();

        http.authorizeRequests().antMatchers(("/cache")).permitAll();
        http.authorizeRequests().antMatchers(("/cache/**")).permitAll();

        http.authorizeRequests().antMatchers(("/login")).permitAll();
        http.authorizeRequests().antMatchers(("/form-login")).permitAll();
        http.authorizeRequests().antMatchers(("/form-sign")).permitAll();

        http.authorizeRequests().antMatchers(("/login-error")).permitAll();
        http.authorizeRequests().antMatchers(("/loginError")).permitAll();
        http.authorizeRequests().antMatchers(("/logout-Success")).permitAll();
        http.authorizeRequests().antMatchers(("/logoutSuccess")).permitAll();

        http.authorizeRequests().antMatchers(("/user-{\\d+}")).permitAll();
        http.authorizeRequests().antMatchers(("/profile-{\\d+}")).permitAll();

        http.authorizeRequests().antMatchers(("/search")).permitAll();
        http.authorizeRequests().antMatchers(("/searchRecipe")).permitAll();
        http.authorizeRequests().antMatchers(("/search-recipe")).permitAll();
        http.authorizeRequests().antMatchers(("/form-search-recipe")).permitAll();

        http.authorizeRequests().antMatchers(("/recipe-{\\d+}")).permitAll();
        http.authorizeRequests().antMatchers(("/complete-recipe-{\\d+}")).permitAll();
        http.authorizeRequests().antMatchers(("/completeRecipe-{\\d+}")).permitAll();

        http.authorizeRequests().antMatchers(("/recipes")).permitAll();
        http.authorizeRequests().antMatchers(("/favorite-recipes-{\\d+}")).permitAll();
        http.authorizeRequests().antMatchers(("/created-recipes-{\\d+}")).permitAll();

        http.authorizeRequests().antMatchers(("/image/**")).permitAll();
        http.authorizeRequests().antMatchers(("/pdf/**")).permitAll();
        http.authorizeRequests().antMatchers(("/txt/**")).permitAll();

        http.authorizeRequests().antMatchers(("/css/**")).permitAll();
        http.authorizeRequests().antMatchers(("/fonts/**")).permitAll();
        http.authorizeRequests().antMatchers(("/icon-fonts/**")).permitAll();
        http.authorizeRequests().antMatchers(("/img/**")).permitAll();
        http.authorizeRequests().antMatchers(("/js/**")).permitAll();

        // Private pages
        http.authorizeRequests().anyRequest().authenticated();

        http.formLogin().loginPage(CookingWithJavaApplication.getAppURL() + "/login").permitAll();
        http.formLogin().loginProcessingUrl("/form-login").permitAll();
        http.formLogin().usernameParameter("nickLogin");
        http.formLogin().passwordParameter("passLogin");
        http.formLogin().defaultSuccessUrl(CookingWithJavaApplication.getAppURL());
        http.formLogin().failureUrl(CookingWithJavaApplication.getAppURL() + "/loginError");

        http.logout().logoutUrl("/logout");
        http.logout().logoutSuccessUrl(CookingWithJavaApplication.getAppURL() + "/logout-success");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }
}