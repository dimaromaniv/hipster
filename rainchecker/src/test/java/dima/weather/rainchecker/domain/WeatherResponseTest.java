package dima.weather.rainchecker.domain;

import static org.assertj.core.api.Assertions.assertThat;

import dima.weather.rainchecker.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WeatherResponseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WeatherResponse.class);
        WeatherResponse weatherResponse1 = new WeatherResponse();
        weatherResponse1.setId(1L);
        WeatherResponse weatherResponse2 = new WeatherResponse();
        weatherResponse2.setId(weatherResponse1.getId());
        assertThat(weatherResponse1).isEqualTo(weatherResponse2);
        weatherResponse2.setId(2L);
        assertThat(weatherResponse1).isNotEqualTo(weatherResponse2);
        weatherResponse1.setId(null);
        assertThat(weatherResponse1).isNotEqualTo(weatherResponse2);
    }
}
