package dima.weather.rainchecker.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import dima.weather.rainchecker.IntegrationTest;
import dima.weather.rainchecker.domain.WeatherResponse;
import dima.weather.rainchecker.repository.WeatherResponseRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WeatherResponseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WeatherResponseResourceIT {

    private static final Double DEFAULT_CLOUDY_PERCENT = 1D;
    private static final Double UPDATED_CLOUDY_PERCENT = 2D;

    private static final Double DEFAULT_TEMP_LEVEL = 1D;
    private static final Double UPDATED_TEMP_LEVEL = 2D;

    private static final Double DEFAULT_HUMIDITY = 1D;
    private static final Double UPDATED_HUMIDITY = 2D;

    private static final Double DEFAULT_FORECAST_FOR_NEXT_HOURS = 1D;
    private static final Double UPDATED_FORECAST_FOR_NEXT_HOURS = 2D;

    private static final Integer DEFAULT_RAIN_COUNTER = 1;
    private static final Integer UPDATED_RAIN_COUNTER = 2;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/weather-responses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WeatherResponseRepository weatherResponseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWeatherResponseMockMvc;

    private WeatherResponse weatherResponse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WeatherResponse createEntity(EntityManager em) {
        WeatherResponse weatherResponse = new WeatherResponse()
            .cloudyPercent(DEFAULT_CLOUDY_PERCENT)
            .tempLevel(DEFAULT_TEMP_LEVEL)
            .humidity(DEFAULT_HUMIDITY)
            .forecastForNextHours(DEFAULT_FORECAST_FOR_NEXT_HOURS)
            .rainCounter(DEFAULT_RAIN_COUNTER)
            .status(DEFAULT_STATUS);
        return weatherResponse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WeatherResponse createUpdatedEntity(EntityManager em) {
        WeatherResponse weatherResponse = new WeatherResponse()
            .cloudyPercent(UPDATED_CLOUDY_PERCENT)
            .tempLevel(UPDATED_TEMP_LEVEL)
            .humidity(UPDATED_HUMIDITY)
            .forecastForNextHours(UPDATED_FORECAST_FOR_NEXT_HOURS)
            .rainCounter(UPDATED_RAIN_COUNTER)
            .status(UPDATED_STATUS);
        return weatherResponse;
    }

    @BeforeEach
    public void initTest() {
        weatherResponse = createEntity(em);
    }

    @Test
    @Transactional
    void createWeatherResponse() throws Exception {
        int databaseSizeBeforeCreate = weatherResponseRepository.findAll().size();
        // Create the WeatherResponse
        restWeatherResponseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(weatherResponse))
            )
            .andExpect(status().isCreated());

        // Validate the WeatherResponse in the database
        List<WeatherResponse> weatherResponseList = weatherResponseRepository.findAll();
        assertThat(weatherResponseList).hasSize(databaseSizeBeforeCreate + 1);
        WeatherResponse testWeatherResponse = weatherResponseList.get(weatherResponseList.size() - 1);
        assertThat(testWeatherResponse.getCloudyPercent()).isEqualTo(DEFAULT_CLOUDY_PERCENT);
        assertThat(testWeatherResponse.getTempLevel()).isEqualTo(DEFAULT_TEMP_LEVEL);
        assertThat(testWeatherResponse.getHumidity()).isEqualTo(DEFAULT_HUMIDITY);
        assertThat(testWeatherResponse.getForecastForNextHours()).isEqualTo(DEFAULT_FORECAST_FOR_NEXT_HOURS);
        assertThat(testWeatherResponse.getRainCounter()).isEqualTo(DEFAULT_RAIN_COUNTER);
        assertThat(testWeatherResponse.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createWeatherResponseWithExistingId() throws Exception {
        // Create the WeatherResponse with an existing ID
        weatherResponse.setId(1L);

        int databaseSizeBeforeCreate = weatherResponseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWeatherResponseMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(weatherResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeatherResponse in the database
        List<WeatherResponse> weatherResponseList = weatherResponseRepository.findAll();
        assertThat(weatherResponseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWeatherResponses() throws Exception {
        // Initialize the database
        weatherResponseRepository.saveAndFlush(weatherResponse);

        // Get all the weatherResponseList
        restWeatherResponseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(weatherResponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].cloudyPercent").value(hasItem(DEFAULT_CLOUDY_PERCENT.doubleValue())))
            .andExpect(jsonPath("$.[*].tempLevel").value(hasItem(DEFAULT_TEMP_LEVEL.doubleValue())))
            .andExpect(jsonPath("$.[*].humidity").value(hasItem(DEFAULT_HUMIDITY.doubleValue())))
            .andExpect(jsonPath("$.[*].forecastForNextHours").value(hasItem(DEFAULT_FORECAST_FOR_NEXT_HOURS.doubleValue())))
            .andExpect(jsonPath("$.[*].rainCounter").value(hasItem(DEFAULT_RAIN_COUNTER)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getWeatherResponse() throws Exception {
        // Initialize the database
        weatherResponseRepository.saveAndFlush(weatherResponse);

        // Get the weatherResponse
        restWeatherResponseMockMvc
            .perform(get(ENTITY_API_URL_ID, weatherResponse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(weatherResponse.getId().intValue()))
            .andExpect(jsonPath("$.cloudyPercent").value(DEFAULT_CLOUDY_PERCENT.doubleValue()))
            .andExpect(jsonPath("$.tempLevel").value(DEFAULT_TEMP_LEVEL.doubleValue()))
            .andExpect(jsonPath("$.humidity").value(DEFAULT_HUMIDITY.doubleValue()))
            .andExpect(jsonPath("$.forecastForNextHours").value(DEFAULT_FORECAST_FOR_NEXT_HOURS.doubleValue()))
            .andExpect(jsonPath("$.rainCounter").value(DEFAULT_RAIN_COUNTER))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingWeatherResponse() throws Exception {
        // Get the weatherResponse
        restWeatherResponseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWeatherResponse() throws Exception {
        // Initialize the database
        weatherResponseRepository.saveAndFlush(weatherResponse);

        int databaseSizeBeforeUpdate = weatherResponseRepository.findAll().size();

        // Update the weatherResponse
        WeatherResponse updatedWeatherResponse = weatherResponseRepository.findById(weatherResponse.getId()).get();
        // Disconnect from session so that the updates on updatedWeatherResponse are not directly saved in db
        em.detach(updatedWeatherResponse);
        updatedWeatherResponse
            .cloudyPercent(UPDATED_CLOUDY_PERCENT)
            .tempLevel(UPDATED_TEMP_LEVEL)
            .humidity(UPDATED_HUMIDITY)
            .forecastForNextHours(UPDATED_FORECAST_FOR_NEXT_HOURS)
            .rainCounter(UPDATED_RAIN_COUNTER)
            .status(UPDATED_STATUS);

        restWeatherResponseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWeatherResponse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWeatherResponse))
            )
            .andExpect(status().isOk());

        // Validate the WeatherResponse in the database
        List<WeatherResponse> weatherResponseList = weatherResponseRepository.findAll();
        assertThat(weatherResponseList).hasSize(databaseSizeBeforeUpdate);
        WeatherResponse testWeatherResponse = weatherResponseList.get(weatherResponseList.size() - 1);
        assertThat(testWeatherResponse.getCloudyPercent()).isEqualTo(UPDATED_CLOUDY_PERCENT);
        assertThat(testWeatherResponse.getTempLevel()).isEqualTo(UPDATED_TEMP_LEVEL);
        assertThat(testWeatherResponse.getHumidity()).isEqualTo(UPDATED_HUMIDITY);
        assertThat(testWeatherResponse.getForecastForNextHours()).isEqualTo(UPDATED_FORECAST_FOR_NEXT_HOURS);
        assertThat(testWeatherResponse.getRainCounter()).isEqualTo(UPDATED_RAIN_COUNTER);
        assertThat(testWeatherResponse.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingWeatherResponse() throws Exception {
        int databaseSizeBeforeUpdate = weatherResponseRepository.findAll().size();
        weatherResponse.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeatherResponseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, weatherResponse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weatherResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeatherResponse in the database
        List<WeatherResponse> weatherResponseList = weatherResponseRepository.findAll();
        assertThat(weatherResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWeatherResponse() throws Exception {
        int databaseSizeBeforeUpdate = weatherResponseRepository.findAll().size();
        weatherResponse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeatherResponseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(weatherResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeatherResponse in the database
        List<WeatherResponse> weatherResponseList = weatherResponseRepository.findAll();
        assertThat(weatherResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWeatherResponse() throws Exception {
        int databaseSizeBeforeUpdate = weatherResponseRepository.findAll().size();
        weatherResponse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeatherResponseMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(weatherResponse))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WeatherResponse in the database
        List<WeatherResponse> weatherResponseList = weatherResponseRepository.findAll();
        assertThat(weatherResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWeatherResponseWithPatch() throws Exception {
        // Initialize the database
        weatherResponseRepository.saveAndFlush(weatherResponse);

        int databaseSizeBeforeUpdate = weatherResponseRepository.findAll().size();

        // Update the weatherResponse using partial update
        WeatherResponse partialUpdatedWeatherResponse = new WeatherResponse();
        partialUpdatedWeatherResponse.setId(weatherResponse.getId());

        partialUpdatedWeatherResponse.cloudyPercent(UPDATED_CLOUDY_PERCENT);

        restWeatherResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeatherResponse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWeatherResponse))
            )
            .andExpect(status().isOk());

        // Validate the WeatherResponse in the database
        List<WeatherResponse> weatherResponseList = weatherResponseRepository.findAll();
        assertThat(weatherResponseList).hasSize(databaseSizeBeforeUpdate);
        WeatherResponse testWeatherResponse = weatherResponseList.get(weatherResponseList.size() - 1);
        assertThat(testWeatherResponse.getCloudyPercent()).isEqualTo(UPDATED_CLOUDY_PERCENT);
        assertThat(testWeatherResponse.getTempLevel()).isEqualTo(DEFAULT_TEMP_LEVEL);
        assertThat(testWeatherResponse.getHumidity()).isEqualTo(DEFAULT_HUMIDITY);
        assertThat(testWeatherResponse.getForecastForNextHours()).isEqualTo(DEFAULT_FORECAST_FOR_NEXT_HOURS);
        assertThat(testWeatherResponse.getRainCounter()).isEqualTo(DEFAULT_RAIN_COUNTER);
        assertThat(testWeatherResponse.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateWeatherResponseWithPatch() throws Exception {
        // Initialize the database
        weatherResponseRepository.saveAndFlush(weatherResponse);

        int databaseSizeBeforeUpdate = weatherResponseRepository.findAll().size();

        // Update the weatherResponse using partial update
        WeatherResponse partialUpdatedWeatherResponse = new WeatherResponse();
        partialUpdatedWeatherResponse.setId(weatherResponse.getId());

        partialUpdatedWeatherResponse
            .cloudyPercent(UPDATED_CLOUDY_PERCENT)
            .tempLevel(UPDATED_TEMP_LEVEL)
            .humidity(UPDATED_HUMIDITY)
            .forecastForNextHours(UPDATED_FORECAST_FOR_NEXT_HOURS)
            .rainCounter(UPDATED_RAIN_COUNTER)
            .status(UPDATED_STATUS);

        restWeatherResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWeatherResponse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWeatherResponse))
            )
            .andExpect(status().isOk());

        // Validate the WeatherResponse in the database
        List<WeatherResponse> weatherResponseList = weatherResponseRepository.findAll();
        assertThat(weatherResponseList).hasSize(databaseSizeBeforeUpdate);
        WeatherResponse testWeatherResponse = weatherResponseList.get(weatherResponseList.size() - 1);
        assertThat(testWeatherResponse.getCloudyPercent()).isEqualTo(UPDATED_CLOUDY_PERCENT);
        assertThat(testWeatherResponse.getTempLevel()).isEqualTo(UPDATED_TEMP_LEVEL);
        assertThat(testWeatherResponse.getHumidity()).isEqualTo(UPDATED_HUMIDITY);
        assertThat(testWeatherResponse.getForecastForNextHours()).isEqualTo(UPDATED_FORECAST_FOR_NEXT_HOURS);
        assertThat(testWeatherResponse.getRainCounter()).isEqualTo(UPDATED_RAIN_COUNTER);
        assertThat(testWeatherResponse.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingWeatherResponse() throws Exception {
        int databaseSizeBeforeUpdate = weatherResponseRepository.findAll().size();
        weatherResponse.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWeatherResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, weatherResponse.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weatherResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeatherResponse in the database
        List<WeatherResponse> weatherResponseList = weatherResponseRepository.findAll();
        assertThat(weatherResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWeatherResponse() throws Exception {
        int databaseSizeBeforeUpdate = weatherResponseRepository.findAll().size();
        weatherResponse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeatherResponseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weatherResponse))
            )
            .andExpect(status().isBadRequest());

        // Validate the WeatherResponse in the database
        List<WeatherResponse> weatherResponseList = weatherResponseRepository.findAll();
        assertThat(weatherResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWeatherResponse() throws Exception {
        int databaseSizeBeforeUpdate = weatherResponseRepository.findAll().size();
        weatherResponse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWeatherResponseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(weatherResponse))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WeatherResponse in the database
        List<WeatherResponse> weatherResponseList = weatherResponseRepository.findAll();
        assertThat(weatherResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWeatherResponse() throws Exception {
        // Initialize the database
        weatherResponseRepository.saveAndFlush(weatherResponse);

        int databaseSizeBeforeDelete = weatherResponseRepository.findAll().size();

        // Delete the weatherResponse
        restWeatherResponseMockMvc
            .perform(delete(ENTITY_API_URL_ID, weatherResponse.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WeatherResponse> weatherResponseList = weatherResponseRepository.findAll();
        assertThat(weatherResponseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
