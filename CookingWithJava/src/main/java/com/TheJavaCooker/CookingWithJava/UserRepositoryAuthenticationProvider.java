package com.TheJavaCooker.CookingWithJava;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Usuario;
import com.TheJavaCooker.CookingWithJava.DataBase.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRepositoryAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UsuarioRepository userRepository;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {

        Usuario user = userRepository.buscarPorNombreUsuario(auth.getName());

        if (user == null) {
            throw new BadCredentialsException("User not found");
        }

        String password = auth.getCredentials().toString();
        if (!password.equals(user.getContrasena())) {
            throw new BadCredentialsException("Wrong password");
        }

       /* List<GrantedAuthority> roles = new ArrayList<>();
        for (String role : user.getRoles()) {
            roles.add(new SimpleGrantedAuthority(role));
        }*/
        return new UsernamePasswordAuthenticationToken(user.getNombreUsuario(), password,new ArrayList<>());

    }

    @Override
    public boolean supports(Class<?> authentication) {

        return authentication.equals( UsernamePasswordAuthenticationToken.class);
    }
}
