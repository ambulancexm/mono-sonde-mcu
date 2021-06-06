package fr.thomas.iot.domain;

import static org.assertj.core.api.Assertions.assertThat;

import fr.thomas.iot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IotTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Iot.class);
        Iot iot1 = new Iot();
        iot1.setId(1L);
        Iot iot2 = new Iot();
        iot2.setId(iot1.getId());
        assertThat(iot1).isEqualTo(iot2);
        iot2.setId(2L);
        assertThat(iot1).isNotEqualTo(iot2);
        iot1.setId(null);
        assertThat(iot1).isNotEqualTo(iot2);
    }
}
