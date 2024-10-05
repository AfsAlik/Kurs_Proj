package ru.pnck.wishlist.service;

import ru.pnck.wishlist.dto.UserDto;
import ru.pnck.wishlist.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto user);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}
