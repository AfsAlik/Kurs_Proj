package ru.pnck.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pnck.wishlist.entity.User;
import ru.pnck.wishlist.entity.Wish;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findAllByUserId(Long userId);
}
