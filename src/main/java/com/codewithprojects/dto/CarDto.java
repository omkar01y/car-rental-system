package com.codewithprojects.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class CarDto {


    private Long id;

    private String brand;
    private String color;
    private String name;
    private String type;
    private String transmission;
    private String description;
    private long price;
    private Date year;
    private MultipartFile image;
    private  byte[] returnedImage;
}
