package com.msouza.agrega_invest.service;

import com.msouza.agrega_invest.dto.CreateUserDto;
import com.msouza.agrega_invest.entity.User;
import com.msouza.agrega_invest.respository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID createUser(CreateUserDto createUserDto) {
        var entity = new User(UUID.randomUUID(),
                createUserDto.username(),
                createUserDto.email(),
                createUserDto.password(),
                Instant.now(),
                null);

        var userSaved = userRepository.save(entity);

        return userSaved.getUserId();
    }

    public Optional<User> findUserById(String userId){
        return userRepository.findById(UUID.fromString(userId));
    }
}
