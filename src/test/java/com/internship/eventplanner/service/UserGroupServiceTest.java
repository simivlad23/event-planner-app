package com.internship.eventplanner.service;

import com.internship.eventplanner.EventPlannerApp;
import com.internship.eventplanner.domain.UserGroup;
import com.internship.eventplanner.repository.UserGroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = EventPlannerApp.class)
@Transactional
class UserGroupServiceTest {

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private UserGroupRepository userGroupRepository;


    @Test
    void pagination_get_with_sussces() throws Exception {

        // create 15 groups
        for (int index = 0; index < 14; index++) {
            UserGroup userGroup1 = new UserGroup();
            userGroupRepository.save(userGroup1);
        }

        Page<UserGroup> page = userGroupService.getAllGroupsByPage(0, 5);

        assertThat(page.getTotalElements() == userGroupRepository.findAll().size());
        assertThat(page.getContent().size() == 5);


    }

    @Test
    void pagination_get_nothing() throws Exception {
        Page<UserGroup> page = userGroupService.getAllGroupsByPage(0, 5);

        assertThat(page.getTotalElements() == userGroupRepository.findAll().size());
        assertThat(page.getContent().size() == 0);

    }


}
