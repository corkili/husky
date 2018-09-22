package com.corkili.husky.test.user;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.corkili.husky.common.Constants;
import com.corkili.husky.test.HuskyTest;
import com.corkili.husky.user.UserPO;
import com.corkili.husky.user.UserRepository;

public class UserTest extends HuskyTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testInsert() {
        UserPO userPO = new UserPO();
        userPO.setUsername("test-user");
        userPO.setPassword("passkey");
        userPO.setCreateTime(new Date());
        userPO.setUpdateTime(new Date());
        userPO.setDeleted(Constants.EXISTED);
        userPO = userRepository.save(userPO);
        System.out.println(userPO);
    }

    @Test
    public void testQuery() {
        UserPO userPO = userRepository.findUserPOByUsername("test-user");
        System.out.println(userPO);
    }

    @Test
    public void testUpdate() {
        UserPO userPO = userRepository.findUserPOByUsername("test-user");
        System.out.println(userPO);
        userPO.setPassword("passkey1");
        userPO = userRepository.save(userPO);
        System.out.println(userPO);
    }

    @Test
    public void testDelete() {
        UserPO userPO = userRepository.findUserPOByUsername("test-user");
        System.out.println(userPO);
        userRepository.delete(userPO);
    }

}
