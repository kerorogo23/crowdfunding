package com.fourseasons.crowdfunding.app.config;

import com.fourseasons.crowdfunding.app.entity.Role;
import com.fourseasons.crowdfunding.app.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 資料初始化器
 * 用於在應用程式啟動時初始化必要的資料
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // 初始化角色資料
        initializeRoles();
    }

    /**
     * 初始化角色資料
     */
    private void initializeRoles() {
        // 檢查是否已有角色資料，如果沒有則插入預設角色
        if (roleRepository.count() == 0) {
            Role guestRole = new Role();
            guestRole.setName(Role.ROLE_GUEST);
            roleRepository.save(guestRole);

            Role memberRole = new Role();
            memberRole.setName(Role.ROLE_MEMBER);
            roleRepository.save(memberRole);

            Role creatorRole = new Role();
            creatorRole.setName(Role.ROLE_CREATOR);
            roleRepository.save(creatorRole);

            Role adminRole = new Role();
            adminRole.setName(Role.ROLE_ADMIN);
            roleRepository.save(adminRole);

            System.out.println("角色資料初始化完成");
        }
    }
}
