package com.apirest.springsecuritydemo4.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.apirest.springsecuritydemo4.entity.OurUser;
import com.apirest.springsecuritydemo4.repository.OurUserRepository;

@Configuration
public class OurUserInfoUserDetailsService implements UserDetailsService {

    @Autowired
    private OurUserRepository ourUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<OurUser> user = ourUserRepository.findByEmail(username);
        return user.map(OurUserInfoDetails::new).orElseThrow(() -> new UsernameNotFoundException("Usuário não existe!"));
    }

}

/*Serviço de detalhes do usuário que é responsável por carregar as informações do usuário 
a partir de um repositório de dados, como um banco de dados.*/
