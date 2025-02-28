package com.kynsof.share.core.infrastructure.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class DateUtil {

    private static final String[] validDateFormat = new String[]{"yyyyMMdd", "yyyy/MM/dd"};

    public static LocalDateTime parseDateToDateTime(String date) {
        date = date.trim();
        LocalDate localDate;
        try {
            if (date.length() == 8) {
                localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(validDateFormat[0]));
                return localDate.atTime(0, 0);
            }
            if (date.length() == 10) {
                localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(validDateFormat[1]));
                return localDate.atTime(0, 0);
            }
        } catch (Exception e) {
            System.err.println("Error parsing date to DateTime: " + e.getMessage());
        }
        return null;
    }

    public static LocalDate parseDateToLocalDate(String date) {
        date = date.trim();
        LocalDate localDate;
        try {
            if (date.length() == 8) {
                localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(validDateFormat[0]));
                return localDate;
            }
            if (date.length() == 10) {
                localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(validDateFormat[1]));
                return localDate;
            }
        } catch (Exception e) {
            System.err.println("Error parsing date to DateTime: " + e.getMessage());
        }
        return null;
    }

    public static LocalDate parseDateToLocalDate(String date, String dateFormat) {
        date = date.trim();
        try {
            if (Objects.isNull(dateFormat) || dateFormat.isEmpty()) {
                return parseDateToLocalDate(date);
            }
            LocalDate localDate;
            if (date.length() == 8) {
                localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat));
                return localDate;
            }
            if (date.length() == 10) {
                localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat));
                return localDate;
            }
        } catch (Exception e) {
            System.err.println("Error parsing date to DateTime: " + e.getMessage());
        }
        return null;
    }

    public static boolean validateDateFormat(String date) {
        date = date.trim();
        try {
            if (date.length() == 8) {
                LocalDate.parse(date, DateTimeFormatter.ofPattern(validDateFormat[0]));
                return true;
            }
            if (date.length() == 10) {
                LocalDate.parse(date, DateTimeFormatter.ofPattern(validDateFormat[1]));
                return true;
            }
        } catch (DateTimeParseException exception) {
            System.err.println("Error parsing date to DateTime: " + exception.getMessage());
            return false;
        }
        return false;

    }

    public static boolean validateDateFormat(String date, String dateFormat) {
        date = date.trim();
        if (Objects.isNull(dateFormat) || dateFormat.isEmpty()) {
            return validateDateFormat(date);
        }
        try {
            if (date.length() == 8) {
                LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat));
                return true;
            }
            if (date.length() == 10) {
                LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat));
                return true;
            }
        } catch (DateTimeParseException exception) {
            System.err.println("Error parsing date to DateTime: " + exception.getMessage());
            return false;
        }
        return false;

    }

    public static boolean getDateForCloseOperation(LocalDate beginDate, LocalDate endDate) {
        LocalDate currentDate = LocalDate.now();
        return (currentDate.isEqual(beginDate) || currentDate.isEqual(endDate))
                || (currentDate.isBefore(endDate) && currentDate.isAfter(beginDate));
    }

    public static boolean getDateForCloseOperation(LocalDate beginDate, LocalDate endDate, LocalDate invoiceDate) {
        return (invoiceDate.isEqual(beginDate) || invoiceDate.isEqual(endDate))
                || (invoiceDate.isBefore(endDate) && invoiceDate.isAfter(beginDate));
    }
}
