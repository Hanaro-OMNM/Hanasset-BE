package com.omnm.hanasset.consultant.service;

import com.omnm.hanasset.global.dto.ConsultantDetailsDTO;
import com.omnm.hanasset.consultant.entity.Consultant;
import com.omnm.hanasset.consultant.repository.ConsultantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConsultantAuthenticationService implements UserDetailsService {

    private final ConsultantRepository consultantRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Consultant consultant = consultantRepository.findByconsultantLoginId(loginId).orElseThrow(() -> new UsernameNotFoundException(loginId + " 유저가 존재하지 않습니다."));

        return ConsultantDetailsDTO.builder()
                .loginId(loginId)
                .password(consultant.getPassword())
                .authority(new SimpleGrantedAuthority("ROLE_USER"))
                .build();
    }
}
