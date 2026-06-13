package com.example.core.model;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Location {
    private BigDecimal latitude;
    private BigDecimal longitude;


    public double calcDistance(Location loc) {
        double R = 6371; // Dünya'nın yarıçapı (km)

        double lat1 = this.getLatitude().doubleValue();
        double lon1 = this.getLongitude().doubleValue();
        double lat2 = loc.getLatitude().doubleValue();
        double lon2 = loc.getLongitude().doubleValue();

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }



}
