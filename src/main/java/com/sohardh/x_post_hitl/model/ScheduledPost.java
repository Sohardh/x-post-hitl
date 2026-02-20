package com.sohardh.x_post_hitl.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "scheduled_posts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private int serialNumber;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    private Instant scheduledTime;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private PostSession session;

    public enum PostStatus {
        DRAFT, APPROVED, DISCARDED, PUBLISHED
    }
}
