package dima.weather.rainchecker.repository;

import dima.weather.rainchecker.domain.WeatherResponse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WeatherResponse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WeatherResponseRepository extends JpaRepository<WeatherResponse, Long> {}
