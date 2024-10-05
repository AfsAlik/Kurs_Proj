package ru.pnck.wishlist.service;

import ru.pnck.wishlist.entity.User;

public interface UserLogService {
    void addLog(User user, String description);

    void addLog(String email, String description);
}
