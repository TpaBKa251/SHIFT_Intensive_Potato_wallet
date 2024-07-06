package ru.cft.template.core.mapper;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class MapperBean {

    @Bean
    public UserMapper userMapper() {
        return Mappers.getMapper(UserMapper.class);
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return Mappers.getMapper(PasswordEncoder.class);
//    }
}
