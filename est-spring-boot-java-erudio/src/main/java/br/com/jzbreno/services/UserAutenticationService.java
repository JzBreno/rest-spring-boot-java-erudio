package br.com.jzbreno.services;

import br.com.jzbreno.model.User;
import br.com.jzbreno.repository.UserPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAutenticationService implements UserDetailsService {

//    @Autowired seria necessario caso a injecao nao fosse via construtor
    private final UserPermissionRepository userPermissionRepository;

    public UserAutenticationService(UserPermissionRepository userPermissionRepository) {
        this.userPermissionRepository = userPermissionRepository;
    }

    public Optional<User> getUser(String userName){
        return userPermissionRepository.findByUsername(userName);
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        return userPermissionRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

