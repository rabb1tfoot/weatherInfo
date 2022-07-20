package zerobase.weather.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String weather;
    private String icon;
    private double temperature;
    private String text;
    private LocalDate date;

    public void setDateWeather(DateWeather dateWeather){
        this.setDate(dateWeather.getDate());
        this.setWeather(dateWeather.getWeather());
        this.setIcon(dateWeather.getIcon());
        this.setTemperature(dateWeather.getTemperature());
    }

}
