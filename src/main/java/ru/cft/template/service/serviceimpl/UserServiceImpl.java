package ru.cft.template.service.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.cft.template.entity.User;
import ru.cft.template.entity.Wallet;
import ru.cft.template.jwt.JwtTokenUtils;
import ru.cft.template.mapper.UserMapper;
import ru.cft.template.model.request.RegisterBody;
import ru.cft.template.model.request.UserUpdateBody;
import ru.cft.template.model.response.TokenResponse;
import ru.cft.template.model.response.UserResponse;
import ru.cft.template.repository.UserRepository;
import ru.cft.template.service.WalletService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final WalletService walletService;
    private final JwtTokenUtils jwtTokenUtils;

    public TokenResponse registerUser(RegisterBody registerBody) {
        User user = UserMapper.mapRegisterBodyToUser(registerBody);
        Wallet wallet = walletService.createWallet();
        user.setWallet(wallet);
        userRepository.save(user);

        return TokenResponse.builder()
                .token(jwtTokenUtils.generateToken(user))
                .build();
    }

    public UserResponse getUserResponseByAuthentication(Authentication authentication) {
        UUID id = jwtTokenUtils.getUserIdFromAuthentication(authentication);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID: " + id + " not found"));

        return UserMapper.mapToResponse(user);
    }

    public User getUserByAuthentication(Authentication authentication) {
        UUID id = jwtTokenUtils.getUserIdFromAuthentication(authentication);

        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID: " + id + " not found"));
    }

    public UserResponse updateUser(Authentication authentication, UserUpdateBody updateBody) {
        UUID id = jwtTokenUtils.getUserIdFromAuthentication(authentication);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID: " + id + " not found"));

        if (updateBody.firstName() != null){
            user.setFirstName(updateBody.firstName());
        }
        if (updateBody.lastName() != null){
            user.setLastName(updateBody.lastName());
        }
        /*if (updateBody.middleName() != null){
            user.setMiddleName(updateBody.middleName());
        }*/
        if (updateBody.age() != null){
            user.setAge(updateBody.age());
        }

        userRepository.save(user);
        return UserMapper.mapToResponse(user);
    }

    public User findUserByPhone(Long phone) {
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException("User with phone: " + phone + " not found"));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found"));
    }

    public User findUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID: " + id + " not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found"));
    }
}
