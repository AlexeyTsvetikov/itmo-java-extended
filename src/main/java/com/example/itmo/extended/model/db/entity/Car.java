package com.example.itmo.extended.model.db.entity;

import com.example.itmo.extended.model.enums.CarStatus;
import com.example.itmo.extended.model.enums.CarType;
import com.example.itmo.extended.model.enums.Color;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "color")
    @Enumerated(EnumType.STRING)
    private Color color;

    @Column(name = "year")
    private Integer year;

    @Column(name = "price")
    private Long price;

    @Column(name = "is_new")
    private Boolean isNew;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CarType type;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CarStatus status;
}
