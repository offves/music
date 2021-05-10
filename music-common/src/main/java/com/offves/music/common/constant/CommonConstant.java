package com.offves.music.common.constant;

import java.time.format.DateTimeFormatter;

public interface CommonConstant {

    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    DateTimeFormatter DATE_FORMATTER2 = DateTimeFormatter.ofPattern("yyyyMMdd");

    DateTimeFormatter MONTH_DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd");

    DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    DateTimeFormatter START_TIME_OF_DAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00");

    DateTimeFormatter END_TIME_OF_DAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59");

    DateTimeFormatter ORDER_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

}
