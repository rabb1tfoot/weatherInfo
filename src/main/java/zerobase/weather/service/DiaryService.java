package zerobase.weather.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import zerobase.weather.domain.Diary;
import zerobase.weather.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiaryService {

    @Value("${openweathermap.key}")
    private String apiKey;

    private final DiaryRepository diaryRepository;

    public void createDiary(LocalDate date, String text) {
        String weatherData = getWeatherString();

        //파싱
        Map<String, Object> parseWeather = parseWeather(weatherData);

        //db저장
        Diary nowDiary = new Diary();
        nowDiary.setWeather(parseWeather.get("main").toString());
        nowDiary.setIcon(parseWeather.get("icon").toString());
        nowDiary.setTemperature((double)parseWeather.get("temp"));
        nowDiary.setText(text);
        nowDiary.setDate(date);

        diaryRepository.save(nowDiary);
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
}
