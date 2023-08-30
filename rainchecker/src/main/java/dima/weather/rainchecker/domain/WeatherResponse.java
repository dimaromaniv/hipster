package dima.weather.rainchecker.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WeatherResponse.
 */
@Entity
@Table(name = "weather_response")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WeatherResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cloudy_percent")
    private Double cloudyPercent;

    @Column(name = "temp_level")
    private Double tempLevel;

    @Column(name = "humidity")
    private Double humidity;

    @Column(name = "forecast_for_next_hours")
    private Double forecastForNextHours;

    @Column(name = "rain_counter")
    private Integer rainCounter;

    @Column(name = "status")
    private String status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WeatherResponse id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCloudyPercent() {
        return this.cloudyPercent;
    }

    public WeatherResponse cloudyPercent(Double cloudyPercent) {
        this.setCloudyPercent(cloudyPercent);
        return this;
    }

    public void setCloudyPercent(Double cloudyPercent) {
        this.cloudyPercent = cloudyPercent;
    }

    public Double getTempLevel() {
        return this.tempLevel;
    }

    public WeatherResponse tempLevel(Double tempLevel) {
        this.setTempLevel(tempLevel);
        return this;
    }

    public void setTempLevel(Double tempLevel) {
        this.tempLevel = tempLevel;
    }

    public Double getHumidity() {
        return this.humidity;
    }

    public WeatherResponse humidity(Double humidity) {
        this.setHumidity(humidity);
        return this;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getForecastForNextHours() {
        return this.forecastForNextHours;
    }

    public WeatherResponse forecastForNextHours(Double forecastForNextHours) {
        this.setForecastForNextHours(forecastForNextHours);
        return this;
    }

    public void setForecastForNextHours(Double forecastForNextHours) {
        this.forecastForNextHours = forecastForNextHours;
    }

    public Integer getRainCounter() {
        return this.rainCounter;
    }

    public WeatherResponse rainCounter(Integer rainCounter) {
        this.setRainCounter(rainCounter);
        return this;
    }

    public void setRainCounter(Integer rainCounter) {
        this.rainCounter = rainCounter;
    }

    public String getStatus() {
        return this.status;
    }

    public WeatherResponse status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WeatherResponse)) {
            return false;
        }
        return id != null && id.equals(((WeatherResponse) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WeatherResponse{" +
            "id=" + getId() +
            ", cloudyPercent=" + getCloudyPercent() +
            ", tempLevel=" + getTempLevel() +
            ", humidity=" + getHumidity() +
            ", forecastForNextHours=" + getForecastForNextHours() +
            ", rainCounter=" + getRainCounter() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
