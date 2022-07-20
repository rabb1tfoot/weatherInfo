package zerobase.weather.error;

public class InvalidDate extends RuntimeException{
    private static final String MESSAGE = "날짜의 범위를 넘었습니다.";

    public InvalidDate(){
        super(MESSAGE);
    }

}
