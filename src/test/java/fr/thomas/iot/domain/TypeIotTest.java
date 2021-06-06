package fr.thomas.iot.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.thomas.iot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeIotTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeIot.class);
        TypeIot typeIot1 = new TypeIot();
        typeIot1.setId(1L);
        TypeIot typeIot2 = new TypeIot();
        typeIot2.setId(typeIot1.getId());
        assertThat(typeIot1).isEqualTo(typeIot2);
        typeIot2.setId(2L);
        assertThat(typeIot1).isNotEqualTo(typeIot2);
        typeIot1.setId(null);
        assertThat(typeIot1).isNotEqualTo(typeIot2);
    }
}
