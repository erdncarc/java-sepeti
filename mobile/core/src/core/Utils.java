package com.example.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.core.dto.restaurant.OpeningHourDTO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Utils {

    public static Bitmap getImageAsBitmap(String image) {
        if (image != null && !image.isEmpty()) {
            image = image.replace("\\n", "") // \n karakterlerini sil
                    .replace("\\r", "")
                    .replace("\"", "")  // \" → "
                    .trim();
            byte[] imageBytes =  android.util.Base64.decode(image, android.util.Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        }
        return null;
    }

    public static OpeningHourDTO getTodayOpeningHours(List<OpeningHourDTO> hours) {
        DayOfWeek today = LocalDate.now().getDayOfWeek(); // bugün hangi gün
        for (OpeningHourDTO entry : hours) {
            if (entry.getDayOfWeek() == today) {
                return entry;
            }
        }
        return null; // bugün için kayıt yoksa
    }

    public static boolean isRestaurantOpenNow(List<OpeningHourDTO> hours) {
        OpeningHourDTO todayHours = getTodayOpeningHours(hours);
        if (todayHours == null) return true;

        LocalTime now = LocalTime.now();
        return now.isAfter(todayHours.getOpenTime()) && now.isBefore(todayHours.getCloseTime());
    }

    public Bitmap base64ToBitmap(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

}
