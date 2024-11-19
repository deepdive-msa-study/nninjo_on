package com.nninjoon.postservice.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PostUploadRequestDto {
    String title;
    String content;
    String author;
    ArrayList<String> hashtags;
}
