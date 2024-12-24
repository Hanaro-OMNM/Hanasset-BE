package com.omnm.hanasset.chat.websocket;

import com.omnm.hanasset.global.config.security.TokenProvider;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketSession;

import javax.security.auth.login.LoginException;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@RequiredArgsConstructor
@Configuration
@Log4j2
public class WebSocketAuthInterceptor implements ChannelInterceptor {


    private final TokenProvider tokenProvider;
    private static final String BEARER_PREFIX = "Bearer ";


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // Stomp 메시지 객체의 헤더 접근
        // StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert accessor != null;
        StompCommand command = accessor.getCommand();
        log.info("COMMAND: {}, HEADER: {}", command, accessor.getMessageHeaders());

        // [구독 취소, 메시지 수신, 메시지 송신, 연결 완료] 시에는 JWT 검사 X
        assert command != null;
        if (command.equals(StompCommand.CONNECT) || command.equals(StompCommand.SEND)) {
            // 예) nativeHeaders={Authorization=[Bearer asklgj11n], accept_version=[1.1, 1.0]}
            String authorizationHeader = String.valueOf(accessor.getNativeHeader("Authorization"));
            if (authorizationHeader == null || authorizationHeader.equals("null")) {
                log.info("헤더가 없는 요청입니다.");
                throw new MalformedJwtException("JWT");
            }

            String token = authorizationHeader.substring(BEARER_PREFIX.length()+1); // 앞에 Bearer 제거
            log.info(token);
            log.info(tokenProvider.validateToken(token));
            if (!tokenProvider.validateToken(token)) {
                throw new MalformedJwtException("JWT");
            }

            if (command.equals(StompCommand.CONNECT)) {
                log.info("웹소켓 연결");
            } else {
                String userEmail = tokenProvider.parseClaims(token).getSubject();
            }
        } else if (command.equals(StompCommand.SUBSCRIBE)) {
            log.info("채팅방 구독");
        }

        return message;
    }

}