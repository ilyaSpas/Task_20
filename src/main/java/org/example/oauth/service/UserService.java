package org.example.oauth.service;

import org.example.oauth.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    void save(User createUser);
}
