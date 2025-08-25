package com.fourseasons.crowdfunding.app.config;

import com.fourseasons.crowdfunding.app.entity.Role;
import com.fourseasons.crowdfunding.app.entity.ProjectCategory;
import com.fourseasons.crowdfunding.app.repository.RoleRepository;
import com.fourseasons.crowdfunding.app.repository.ProjectCategoryRepository;
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

    @Autowired
    private ProjectCategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        // 初始化角色資料
        initializeRoles();
        // 初始化專案分類資料
        initializeCategories();
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

    /**
     * 初始化專案分類資料
     */
    private void initializeCategories() {
        // 檢查是否已有分類資料，如果沒有則插入預設分類
        if (categoryRepository.count() == 0) {
            ProjectCategory techCategory = new ProjectCategory();
            techCategory.setName("科技創新");
            techCategory.setDescription("包含軟體開發、硬體設計、AI技術等科技相關專案");
            categoryRepository.save(techCategory);

            ProjectCategory artCategory = new ProjectCategory();
            artCategory.setName("藝術文化");
            artCategory.setDescription("包含音樂、電影、繪畫、攝影等藝術文化專案");
            categoryRepository.save(artCategory);

            ProjectCategory socialCategory = new ProjectCategory();
            socialCategory.setName("社會公益");
            socialCategory.setDescription("包含環保、教育、醫療、慈善等社會公益專案");
            categoryRepository.save(socialCategory);

            ProjectCategory businessCategory = new ProjectCategory();
            businessCategory.setName("商業創業");
            businessCategory.setDescription("包含新創公司、產品開發、商業模式創新等專案");
            categoryRepository.save(businessCategory);

            ProjectCategory lifestyleCategory = new ProjectCategory();
            lifestyleCategory.setName("生活風格");
            lifestyleCategory.setDescription("包含美食、旅遊、時尚、運動等生活相關專案");
            categoryRepository.save(lifestyleCategory);

            System.out.println("專案分類資料初始化完成");
        }
    }
}
