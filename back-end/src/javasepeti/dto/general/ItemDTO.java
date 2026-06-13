package com.example.javasepeti.dto.general;

import com.example.javasepeti.enums.Label;
import com.example.javasepeti.enums.MealCategory;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ItemDTO {

    protected Long itemId;

    protected String name;
    protected String description;

    protected List<MealCategory> categories = new ArrayList<>();

    protected List<Label> labels = new ArrayList<>();

    protected int protein;

    protected int carb;

    protected int fat;

    protected int calorie;
}
