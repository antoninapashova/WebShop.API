package com.example.webshopapi.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ImageDto {
    public UUID id;
    public byte[] img;
}
