package com.example.core.dto.general;

import com.example.core.model.Rating;

import lombok.Data;

import java.util.Date;

@Data
public class ReviewDTO {

    private Long rev_id;
    private Rating rating;
    private String comment;
    private Date date;
    private Long orderId;
}
