package com.acme.saas.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** RLS on the users table means findAll() implicitly returns only the caller's tenant rows. */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<User> listUsersForCurrentTenant() {
        return userRepository.findAll();
    }
}
