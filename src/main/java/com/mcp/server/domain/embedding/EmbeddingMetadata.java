package com.mcp.server.domain.embedding;

// ✅ 분리 이유
// EmbeddingMetadata는 역할/성격이 다른 두 유형(Log 기반 / Context 기반)의 데이터를 처리하므로
// 아래처럼 두 테이블로 분리하는 것이 유지보수성과 확장성, 품질 분석 면에서 유리하다.
// - LogEmbeddingMetadata: InferLog 기반 PROMPT/QUERY/RESULT 텍스트 저장
// - ContextEmbeddingMetadata: ContextSet 기반 PERSONA/ROLE/SITUATION/GOAL/TONE 텍스트 저장

/*

@Entity
@Table(name = "embedding_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmbeddingMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String project; // 🔹 소속 프로젝트명 (e.g. "mcp", "resume-ai")

    @Column(nullable = false)
    private Long sourceId; // 🔹 연관된 도메인 객체의 ID (e.g. infer_log.id 또는 context_set.id)

    @Column(nullable = false, length = 100)
    private String sourceType; // 🔹 예: INFER_LOG, CONTEXT_SET 등 (도메인 종류)

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 🔹 임베딩 대상 텍스트 (프롬프트/결과/조합 등)

    @Column(nullable = false, length = 100)
    private String contentType; // 🔹 예: PROMPT, RESULT, QUERY, COMBINED

    @Column(nullable = false)
    private boolean embedded; // 🔹 벡터화 여부

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
*/
