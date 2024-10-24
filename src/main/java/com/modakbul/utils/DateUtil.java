package com.modakbul.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DateUtil {

    // startDate부터 endDate까지의 LocalDate 리스트 생성
    public static List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = new ArrayList<>();

        // 현재 날짜를 startDate로 설정
        LocalDate currentDate = startDate;

        // currentDate가 endDate보다 이후가 아닐 때까지 반복
        while (!currentDate.isAfter(endDate)) {
            dates.add(currentDate);  // 리스트에 현재 날짜 추가
            currentDate = currentDate.plusDays(1);  // 날짜를 하루씩 증가
        }

        return dates;  // 날짜 리스트 반환
    }
}
