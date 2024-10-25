package com.example.webshopapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "images")
@NoArgsConstructor
public class ImageEntity extends BaseEntity {
    @Lob
    @JdbcTypeCode(Types.BINARY)
    @Column(name = "image_data", columnDefinition = "bytea")
    private byte[] img;

    public ImageEntity(byte[] img){
        this.img = img;
    }
}
