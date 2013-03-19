package edu.rit.asksg.service;


import edu.rit.asksg.domain.AsksgUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug( "loadUserByUsername called with " + username);
        AsksgUser user = this.userRepository.findByUserName(username);
        logger.debug( "Returned user " + user.getUsername() + ":" + user.getPassword());
        return user;
    }

}
