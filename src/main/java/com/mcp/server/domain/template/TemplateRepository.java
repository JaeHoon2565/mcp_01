package com.mcp.server.domain.template;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
/*
// ✅ 13. TemplateRepository 추가
// 템플릿을 저장하고 조회하기 위한 Spring Data JPA 인터페이스
*/
public interface TemplateRepository extends JpaRepository<Template, Long> {
    List<Template> findByProject(String project);
}
