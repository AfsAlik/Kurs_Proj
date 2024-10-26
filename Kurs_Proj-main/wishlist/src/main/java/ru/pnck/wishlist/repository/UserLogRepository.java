package ru.pnck.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pnck.wishlist.entity.UserLog;

public interface UserLogRepository extends JpaRepository<UserLog, Long> {
}
