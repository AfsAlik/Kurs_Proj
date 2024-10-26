package ru.pnck.wishlist.service;

import org.springframework.data.jpa.repository.EntityGraph;
import ru.pnck.wishlist.dto.UserDto;
import ru.pnck.wishlist.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void saveUser(UserDto user);

    User findUserByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    List<User> findAllUsers();

    Optional<User> findUserById(Long userId);

    void deleteUserById(Long userId);
}
