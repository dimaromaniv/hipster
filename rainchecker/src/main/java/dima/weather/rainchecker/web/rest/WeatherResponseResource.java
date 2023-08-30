package dima.weather.rainchecker.web.rest;

import dima.weather.rainchecker.domain.WeatherResponse;
import dima.weather.rainchecker.repository.WeatherResponseRepository;
import dima.weather.rainchecker.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link dima.weather.rainchecker.domain.WeatherResponse}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WeatherResponseResource {

    private final Logger log = LoggerFactory.getLogger(WeatherResponseResource.class);

    private static final String ENTITY_NAME = "weatherResponse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WeatherResponseRepository weatherResponseRepository;

    public WeatherResponseResource(WeatherResponseRepository weatherResponseRepository) {
        this.weatherResponseRepository = weatherResponseRepository;
    }

    /**
     * {@code POST  /weather-responses} : Create a new weatherResponse.
     *
     * @param weatherResponse the weatherResponse to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new weatherResponse, or with status {@code 400 (Bad Request)} if the weatherResponse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/weather-responses")
    public ResponseEntity<WeatherResponse> createWeatherResponse(@RequestBody WeatherResponse weatherResponse) throws URISyntaxException {
        log.debug("REST request to save WeatherResponse : {}", weatherResponse);
        if (weatherResponse.getId() != null) {
            throw new BadRequestAlertException("A new weatherResponse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WeatherResponse result = weatherResponseRepository.save(weatherResponse);
        return ResponseEntity
            .created(new URI("/api/weather-responses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /weather-responses/:id} : Updates an existing weatherResponse.
     *
     * @param id the id of the weatherResponse to save.
     * @param weatherResponse the weatherResponse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weatherResponse,
     * or with status {@code 400 (Bad Request)} if the weatherResponse is not valid,
     * or with status {@code 500 (Internal Server Error)} if the weatherResponse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/weather-responses/{id}")
    public ResponseEntity<WeatherResponse> updateWeatherResponse(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WeatherResponse weatherResponse
    ) throws URISyntaxException {
        log.debug("REST request to update WeatherResponse : {}, {}", id, weatherResponse);
        if (weatherResponse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weatherResponse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weatherResponseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WeatherResponse result = weatherResponseRepository.save(weatherResponse);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, weatherResponse.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /weather-responses/:id} : Partial updates given fields of an existing weatherResponse, field will ignore if it is null
     *
     * @param id the id of the weatherResponse to save.
     * @param weatherResponse the weatherResponse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weatherResponse,
     * or with status {@code 400 (Bad Request)} if the weatherResponse is not valid,
     * or with status {@code 404 (Not Found)} if the weatherResponse is not found,
     * or with status {@code 500 (Internal Server Error)} if the weatherResponse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/weather-responses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WeatherResponse> partialUpdateWeatherResponse(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WeatherResponse weatherResponse
    ) throws URISyntaxException {
        log.debug("REST request to partial update WeatherResponse partially : {}, {}", id, weatherResponse);
        if (weatherResponse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weatherResponse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weatherResponseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WeatherResponse> result = weatherResponseRepository
            .findById(weatherResponse.getId())
            .map(existingWeatherResponse -> {
                if (weatherResponse.getCloudyPercent() != null) {
                    existingWeatherResponse.setCloudyPercent(weatherResponse.getCloudyPercent());
                }
                if (weatherResponse.getTempLevel() != null) {
                    existingWeatherResponse.setTempLevel(weatherResponse.getTempLevel());
                }
                if (weatherResponse.getHumidity() != null) {
                    existingWeatherResponse.setHumidity(weatherResponse.getHumidity());
                }
                if (weatherResponse.getForecastForNextHours() != null) {
                    existingWeatherResponse.setForecastForNextHours(weatherResponse.getForecastForNextHours());
                }
                if (weatherResponse.getRainCounter() != null) {
                    existingWeatherResponse.setRainCounter(weatherResponse.getRainCounter());
                }
                if (weatherResponse.getStatus() != null) {
                    existingWeatherResponse.setStatus(weatherResponse.getStatus());
                }

                return existingWeatherResponse;
            })
            .map(weatherResponseRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, weatherResponse.getId().toString())
        );
    }

    /**
     * {@code GET  /weather-responses} : get all the weatherResponses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of weatherResponses in body.
     */
    @GetMapping("/weather-responses")
    public List<WeatherResponse> getAllWeatherResponses() {
        log.debug("REST request to get all WeatherResponses");
        return weatherResponseRepository.findAll();
    }

    /**
     * {@code GET  /weather-responses/:id} : get the "id" weatherResponse.
     *
     * @param id the id of the weatherResponse to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the weatherResponse, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/weather-responses/{id}")
    public ResponseEntity<WeatherResponse> getWeatherResponse(@PathVariable Long id) {
        log.debug("REST request to get WeatherResponse : {}", id);
        Optional<WeatherResponse> weatherResponse = weatherResponseRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(weatherResponse);
    }

    /**
     * {@code DELETE  /weather-responses/:id} : delete the "id" weatherResponse.
     *
     * @param id the id of the weatherResponse to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/weather-responses/{id}")
    public ResponseEntity<Void> deleteWeatherResponse(@PathVariable Long id) {
        log.debug("REST request to delete WeatherResponse : {}", id);
        weatherResponseRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
