package ru.pnck.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pnck.wishlist.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
