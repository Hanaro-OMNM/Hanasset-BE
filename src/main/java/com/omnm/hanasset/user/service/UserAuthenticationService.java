package com.omnm.hanasset.user.service;

import com.omnm.hanasset.consultant.entity.Consultant;
import com.omnm.hanasset.consultant.repository.ConsultantRepository;
import com.omnm.hanasset.global.dto.UserDetailsDTO;
import com.omnm.hanasset.global.dto.ConsultantDetailsDTO;
import com.omnm.hanasset.user.entity.User;
import com.omnm.hanasset.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserAuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    private final ConsultantRepository consultantRepository;

    /*
        JWT에서 추출한 유저 식별자(userId)와 일치하는 User가 데이터베이스에 존재하는지의 여부를 판단하고, 존재하면 Spring Security에서 내부적으로 사용되는 Auth 객체(UserPasswordAuthenticationToken)를 만들 때 필요한 UserDetails 객체로 반환
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);

        if (!user.isPresent()) {
            Consultant consultant = consultantRepository.findByconsultantLoginId(email).orElseThrow(() -> new UsernameNotFoundException(email));

            return ConsultantDetailsDTO.builder()
                    .loginId(consultant.getConsultantLoginId())
                    .password(consultant.getPassword())
                    .authority(new SimpleGrantedAuthority("ROLE_CONSULTANT")) // 기본 권한 설정
                    .build();
        } else {
            return UserDetailsDTO.builder()
                    .id(user.get().getUserId())
                    .email(user.get().getEmail())
                    .password(user.get().getPassword())
                    .authority(new SimpleGrantedAuthority("ROLE_USER")) // 기본 권한 설정
                    .build();
        }
    }
}
