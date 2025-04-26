package com.mcp.server.domain.embedding;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogEmbeddingMetadataRepository extends JpaRepository<LogEmbeddingMetadata, Long> {
    List<LogEmbeddingMetadata> findAllByEmbeddedFalse();
    List<LogEmbeddingMetadata> findAllByEmbeddedTrue();
    List<LogEmbeddingMetadata> findByLogId(Long logId);
}
