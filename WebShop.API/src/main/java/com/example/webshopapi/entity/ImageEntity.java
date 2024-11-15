package com.example.webshopapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.util.UUID;

@Entity
@Data
@Table(name = "images")
@RequiredArgsConstructor
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Lob
    @JdbcTypeCode(Types.BINARY)
    @Column(name = "image_data", columnDefinition = "bytea")
    private byte[] img;

    public ImageEntity(byte[] img){
        this.img = img;
    }
}
