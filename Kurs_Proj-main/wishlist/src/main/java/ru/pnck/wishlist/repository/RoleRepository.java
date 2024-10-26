package ru.pnck.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pnck.wishlist.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
