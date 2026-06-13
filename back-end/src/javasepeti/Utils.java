package com.example.javasepeti;

import java.util.Base64;

public class Utils {
    public static String clearBase64Image(String image) {
        if(image.contains(",")){
            return image.split(",")[1];
        }
        return image;
    }
}
