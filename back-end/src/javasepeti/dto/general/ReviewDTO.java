package com.example.javasepeti.dto.general;

import com.example.javasepeti.model.Rating;
import lombok.Data;

import java.util.Date;

@Data
public class ReviewDTO {

    private Long rev_id;
    private Rating rating;
    private String comment;
    private Date date;
    private Long orderId;

    private String menuItemName;
    private String customerUsername;
}
