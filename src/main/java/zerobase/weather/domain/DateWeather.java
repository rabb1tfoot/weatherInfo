package zerobase.weather.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateWeather {
    @Id
    private LocalDate date;
    private String weather;
    private String icon;
    private double temperature;

}
