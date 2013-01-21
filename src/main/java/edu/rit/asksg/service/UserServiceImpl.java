package edu.rit.asksg.service;


import edu.rit.asksg.domain.AsksgUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserServiceImpl implements UserService, UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return AsksgUser.findAsksgUsersByUserNameEquals(username).getSingleResult();
    }
}
