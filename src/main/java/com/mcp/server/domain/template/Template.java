package com.mcp.server.domain.template;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
/*
// ✅ 12. Template 엔티티 추가
// 자주 사용하는 context 템플릿 저장용 엔티티

*/
@Entity
@Getter
@NoArgsConstructor
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String project;
    private String name; // 템플릿 이름

    @Column(columnDefinition = "TEXT")
    private String description; // 설명 (선택)

    @Column(columnDefinition = "TEXT")
    private String contextJson; // JSON 문자열 형태로 저장

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Template(String project, String name, String description, String contextJson) {
        this.project = project;
        this.name = name;
        this.description = description;
        this.contextJson = contextJson;
    }
}