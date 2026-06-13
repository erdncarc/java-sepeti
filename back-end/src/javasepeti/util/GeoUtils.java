package com.example.javasepeti.util;

import java.math.BigDecimal;

public class GeoUtils {

    private static final int EARTH_RADIUS_KM = 6371;

    /**
     * Calculates the Haversine distance between two latitude/longitude points.
     *
     * @param lat1 Latitude of point 1 (BigDecimal)
     * @param lon1 Longitude of point 1 (BigDecimal)
     * @param lat2 Latitude of point 2 (BigDecimal)
     * @param lon2 Longitude of point 2 (BigDecimal)
     * @return Distance in kilometers (double)
     */
    public static double calculateDistance(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {

        double dLat = Math.toRadians(lat2.doubleValue() - lat1.doubleValue());
        double dLon = Math.toRadians(lon2.doubleValue() - lon1.doubleValue());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1.doubleValue())) * Math.cos(Math.toRadians(lat2.doubleValue()))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

}
