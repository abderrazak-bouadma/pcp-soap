package com.cdc.fast.ws.misc;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created with IntelliJ IDEA.
 * User: Utilistateur
 * Date: 06/06/13
 * Time: 18:03
 * To change this template use File | Settings | File Templates.
 */
public class PCPUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println("Current User " + s);
        return null;
    }
}
