package com.mcp.server.domain.embedding;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContextEmbeddingMetadataRepository extends JpaRepository<ContextEmbeddingMetadata, Long> {
    List<ContextEmbeddingMetadata> findAllByEmbeddedFalse();
    List<ContextEmbeddingMetadata> findAllByEmbeddedTrue();
    List<ContextEmbeddingMetadata> findByContextSetId(Long contextSetId);
}