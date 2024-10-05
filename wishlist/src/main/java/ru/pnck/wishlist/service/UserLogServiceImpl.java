package ru.pnck.wishlist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.pnck.wishlist.entity.User;
import ru.pnck.wishlist.entity.UserLog;
import ru.pnck.wishlist.repository.UserLogRepository;
import ru.pnck.wishlist.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class UserLogServiceImpl implements UserLogService {
    private final UserLogRepository userLogRepository;

    private final UserRepository userRepository;

    @Autowired
    public UserLogServiceImpl(UserLogRepository userLogRepository, UserRepository userRepository) {
        this.userLogRepository = userLogRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void addLog(User user, String description) {
        var userLog = new UserLog();
        userLog.setUser(user);
        userLog.setDescription(description);
        userLog.setLogDateTime(LocalDateTime.now());
        userLogRepository.save(userLog);
    }

    @Override
    public void addLog(String email, String description) {
        addLog(userRepository.findByEmail(email), description);
    }
}
