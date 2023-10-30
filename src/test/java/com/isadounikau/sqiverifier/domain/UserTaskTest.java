package com.isadounikau.sqiverifier.domain;

import com.isadounikau.sqiverifier.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTaskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserTask.class);
        UserTask userTask1 = new UserTask();
        userTask1.setId(1L);
        UserTask userTask2 = new UserTask();
        userTask2.setId(userTask1.getId());
        assertThat(userTask1).isEqualTo(userTask2);
        userTask2.setId(2L);
        assertThat(userTask1).isNotEqualTo(userTask2);
        userTask1.setId(null);
        assertThat(userTask1).isNotEqualTo(userTask2);
    }
}
