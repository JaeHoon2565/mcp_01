package com.mcp.server.domain.context;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContextSetRepository extends JpaRepository<ContextSet, Long> {
    // 필요하면 findByName 같은 커스텀 쿼리도 여기에 추가 가능
}