package zerobase.weather.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class DiaryService {

    @Value("${openweathermap.key}")
    private String apiKey;

    private final DiaryRepository diaryRepository;

    private final DateWeatherRepository dateWeatherRepository;

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void saveWeatherDate(){
        dateWeatherRepository.save(getWeatherFromApi());
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createDiary(LocalDate date, String text) {

        //데이터 가져오기
        DateWeather parseWeather = getDateWeather(date);

        //db저장
        Diary nowDiary = new Diary();
        nowDiary.setDateWeather(parseWeather);
        nowDiary.setText(text);
        nowDiary.setDate(date);
        diaryRepository.save(nowDiary);
    }


    @Transactional(readOnly = true)
    public List<Diary> readDiarys(LocalDate startDate, LocalDate endDate) {
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }
    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date) {
        return diaryRepository.findAllByDate(date);
    }
    public void updateDiary(LocalDate date, String text) {

        Diary diary = diaryRepository.getFirstByDate(date);

        diary.setText(text);
        diaryRepository.save(diary);

    }

    public void DeleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }

    private DateWeather getDateWeather(LocalDate date) {
        Optional<DateWeather> dateWeather = dateWeatherRepository.findByDate(date);
        return dateWeather.orElse(getWeatherFromApi());
    }

    private String getWeatherString(){
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + apiKey;
        try{
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            BufferedReader br;
            br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            if(responseCode != 200){
                br = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while((inputLine = br.readLine()) != null){
                response.append(inputLine);
            }
            br.close();
            return response.toString();
        }catch (Exception e){
            return e.getMessage();
        }
    }

    private Map<String, Object> parseWeather(String jsonString){
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try{
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        }catch (ParseException e){
            throw new RuntimeException(e);
        }
        Map<String, Object> resultMap = new HashMap<>();

        JSONObject mainData = (JSONObject)jsonObject.get("main");
        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
        JSONObject weatherData = (JSONObject) weatherArray.get(0);
        resultMap.put("temp", mainData.get("temp"));
        resultMap.put("main", weatherData.get("main"));
        resultMap.put("icon", weatherData.get("icon"));
        return resultMap;
    }

    private DateWeather getWeatherFromApi(){
        String weatherData = getWeatherString();

        //파싱
        Map<String, Object> parseWeather = parseWeather(weatherData);

        DateWeather dateWeather = new DateWeather();
        dateWeather.setDate(LocalDate.now());
        dateWeather.setWeather(parseWeather.get("main").toString());
        dateWeather.setIcon(parseWeather.get("icon").toString());
        dateWeather.setTemperature((double)parseWeather.get("temp"));

        return dateWeather;
    }

}
