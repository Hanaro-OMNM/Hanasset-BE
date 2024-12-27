package com.omnm.hanasset.user.service;

import com.omnm.hanasset.global.dto.UserDetailsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GuestAuthenticationService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String tmp) throws UsernameNotFoundException {
        return UserDetailsDTO.builder()
                .id(null)
                .email(null)
                .password(null)
                .authority(new SimpleGrantedAuthority("ROLE_GUEST"))
                .build();

    }
}
