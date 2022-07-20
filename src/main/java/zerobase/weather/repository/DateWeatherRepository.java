package zerobase.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zerobase.weather.domain.DateWeather;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DateWeatherRepository extends JpaRepository<DateWeather, LocalDate> {

Optional<DateWeather> findByDate(LocalDate date);

}
