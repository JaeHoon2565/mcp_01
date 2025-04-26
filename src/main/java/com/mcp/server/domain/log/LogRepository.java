package com.mcp.server.domain.log;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * ✅ 8. LogRepository
 * - Log 엔티티를 DB에 저장하고 조회하는 Spring Data JPA 인터페이스
 * - 기본 제공되는 CRUD 외에도, 필터 조건에 따라 다양한 조회 기능 제공
 */
public interface LogRepository extends JpaRepository<Log, Long> {

    // ✅ 제공자 기준으로 필터 (대소문자 구분 없이)
    List<Log> findByProviderIgnoreCase(String provider);

    // ✅ 모델 기준으로 필터 (대소문자 구분 없이)
    List<Log> findByModelIgnoreCase(String model);

    // ✅ 프로젝트 기준으로 필터 (대소문자 구분 없이)
    List<Log> findByProjectIgnoreCase(String project);

    // ✅ 제공자 + 모델로 필터 (대소문자 구분 없이)
    List<Log> findByProviderIgnoreCaseAndModelIgnoreCase(String provider, String model);

    // ✅ 최근 생성일 기준 정렬
    List<Log> findAllByOrderByCreatedAtDesc();

    // ✅ 제공자 기준 + 최신순 정렬 (대소문자 구분 없이)
    List<Log> findByProviderIgnoreCaseOrderByCreatedAtDesc(String provider);

    // ✅ 프로젝트 + 제공자 기준 + 최신순 정렬 (대소문자 구분 없이)
    List<Log> findByProjectIgnoreCaseAndProviderIgnoreCaseOrderByCreatedAtDesc(String project, String provider);

    // 필요하면 Paging 지원도 가능:
    // Page<Log> findByProviderIgnoreCase(String provider, Pageable pageable);
}
