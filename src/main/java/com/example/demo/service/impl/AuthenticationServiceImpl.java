package com.example.demo.service.impl;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.DtoUser;
import com.example.demo.dto.RefreshTokenRequest;
import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import com.example.demo.exception.BaseException;
import com.example.demo.exception.ErrorMessage;
import com.example.demo.exception.MessageType;
import com.example.demo.jwt.JwtService;
import com.example.demo.repo.RefreshTokenRepository;
import com.example.demo.repo.UserRepository;
import com.example.demo.service.AuthenticationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;
    public User createUser(AuthRequest authRequest) {
        return userRepository.save(User.builder()
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .username(authRequest.getUsername())
                .createTime(new Date())
                .build());
    }
    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setCreateTime(new Date());
        refreshToken.setExpiredDate(new Date(System.currentTimeMillis() + 1000*60*60*4));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        return refreshToken;
    }
    public DtoUser register(AuthRequest authRequest){
        User user = createUser(authRequest);
        DtoUser dtoUser = new DtoUser();
        BeanUtils.copyProperties(user, dtoUser);
        return dtoUser;
    }
    public boolean isValidRefreshToken(RefreshToken refreshToken){
        return refreshToken.getExpiredDate().after(new Date());
    }
    @Override
    public AuthResponse authenticate(AuthRequest authRequest) {
        try{
            UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword());
            authenticationProvider.authenticate(authenticationToken);
            Optional<User> optional=userRepository.findByUsername(authRequest.getUsername());
            String accessToken= jwtService.generateToken(optional.get());
            RefreshToken savedRefreshToken= refreshTokenRepository.save(createRefreshToken(optional.get()));
            return new AuthResponse(accessToken,savedRefreshToken.getRefreshToken());
        }
        catch (Exception exception){
            throw new BaseException(new ErrorMessage(MessageType.USERNAME_OR_PASSWORD_INVALID,exception.getMessage()));
        }
    }
    public AuthResponse refreshToken(RefreshTokenRequest request){
        Optional<RefreshToken> optional=refreshTokenRepository.findByRefreshToken(request.getRefreshToken());
        if (optional.isEmpty()) {
            throw  new BaseException(new ErrorMessage(MessageType.REFRESH_TOKEN_NOT_FOUND,null));
        }
        if (!isValidRefreshToken(optional.get())){
            throw new BaseException(new ErrorMessage(MessageType.REFRESH_TOKEN_IS_EXPIRED,null));
        }
        String newAccessToken=jwtService.generateToken(optional.get().getUser());

        RefreshToken newRefreshToken=refreshTokenRepository.save(createRefreshToken(optional.get().getUser()));
        System.out.println("Refresh Token başarılı bir şekilde oluşturuldu.");
        return new AuthResponse(newAccessToken,newRefreshToken.getRefreshToken());

    }


}

