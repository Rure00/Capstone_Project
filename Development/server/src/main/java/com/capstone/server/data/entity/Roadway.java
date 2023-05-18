package com.capstone.server.data.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Table(name = "roadway")
public class Roadway extends BaseEntity {
    @Id
    private Long roadId;

    @Column(nullable = false)
    private String roadName;

    @Column(nullable = false)
    private Integer roadLength;

    @Column(nullable = false)
    private Boolean existence;

    @Column(nullable = false)
    private float score;

    @Column(nullable = false)
    private Integer updateNum;
}
