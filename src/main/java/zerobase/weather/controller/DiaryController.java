package zerobase.weather.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.domain.Diary;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;


    @ApiOperation(value = "일기만들기", notes = "요약")
    @PostMapping("/create/diary")
    void createDiary(@RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate date,
                     @RequestBody String text){

        diaryService.createDiary(date, text);
    }

    @ApiOperation(value = "DB에서 일기를 날짜로 가져오기", notes = "Date type : yyyy-mm-dd")
    @GetMapping("/read/diary")
    List<Diary> readDiary(@RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate date){
        return diaryService.readDiary(date);
    }

    @ApiOperation(value = "DB에서 일기를 기간으로 가져오기", notes = "Date type : yyyy-mm-dd")
    @GetMapping("/read/diarys")
    List<Diary> readDiaries(@RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) @ApiParam(value ="조회할 날짜의 첫 날", example = "2020-07-20") LocalDate startDate,
                            @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) @ApiParam(value ="조회할 날짜의 마지막 날", example = "2020-07-20") LocalDate endDate){

        return diaryService.readDiarys(startDate, endDate);

    }
    @ApiOperation(value = "DB에서 일기를 업데이트 하기", notes = "기존값은 지워집니다")
    @PostMapping("/update/diary")
    void updateDiary(@RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate date,
                            @RequestBody String text){
        diaryService.updateDiary(date, text);

    }

    @ApiOperation(value = "DB에서 일기를 삭제하기", notes = "Date type : yyyy-mm-dd")
    @PostMapping("/delete/diary")
    void deleteDiary(@RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate date){
        diaryService.DeleteDiary(date);

    }


}
