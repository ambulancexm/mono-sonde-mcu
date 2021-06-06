package fr.thomas.iot.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.thomas.iot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IotDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IotDTO.class);
        IotDTO iotDTO1 = new IotDTO();
        iotDTO1.setId(1L);
        IotDTO iotDTO2 = new IotDTO();
        assertThat(iotDTO1).isNotEqualTo(iotDTO2);
        iotDTO2.setId(iotDTO1.getId());
        assertThat(iotDTO1).isEqualTo(iotDTO2);
        iotDTO2.setId(2L);
        assertThat(iotDTO1).isNotEqualTo(iotDTO2);
        iotDTO1.setId(null);
        assertThat(iotDTO1).isNotEqualTo(iotDTO2);
    }
}
