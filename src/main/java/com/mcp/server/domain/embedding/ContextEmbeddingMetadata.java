package com.mcp.server.domain.embedding;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "embedding_context_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContextEmbeddingMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String project; // 🔹 프로젝트 이름

    @Column(nullable = false)
    private Long contextSetId; // 🔹 연관된 ContextSet ID

    @Column(nullable = false, length = 100)
    private String contextType; // 🔹 PERSONA / ROLE / SITUATION / GOAL / TONE

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private boolean embedded; // 🔹 벡터화 여부

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
