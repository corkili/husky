package com.corkili.husky.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserPO, Long> {

    UserPO findUserPOByUsername(String username);

}
