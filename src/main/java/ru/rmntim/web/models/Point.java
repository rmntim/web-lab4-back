package ru.rmntim.web.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.ZonedDateTime;

@Entity
@Table(name = "points", schema = "public")
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "x")
    private double x;

    @Column(name = "y")
    private double y;

    @Column(name = "r")
    private double r;

    @Column(name = "created_at")
    @ColumnDefault("CURRENT_TIMESTAMP")
    private ZonedDateTime createdAt;

    @Column(name = "execution_time_ns")
    private Long executionTimeNs;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
