package com.capstone.server.data.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ImageDto {
    private String originImageName;
    private String newImageName;
    private String imagePath;

    @Builder
    public ImageDto(String originImageName, String newImageName, String imagePath) {
        this.originImageName = originImageName;
        this.newImageName = newImageName;
        this.imagePath = imagePath;
    }

}