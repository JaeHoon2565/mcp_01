package com.mcp.server.service;


import com.mcp.server.domain.context.ContextSet;
import com.mcp.server.domain.context.ContextSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ContextSetService
 *
 * 컨텍스트 세트(ContextSet)를 관리하는 서비스 계층.
 * 주로 관리자 기능 또는 추론 요청 시 contextJson 구성을 위해 사용됨.
 */
@Service
@RequiredArgsConstructor
public class ContextSetService {

    private final ContextSetRepository contextSetRepository;

    /**
     * 모든 컨텍스트 세트를 조회
     *
     * @return DB에 저장된 모든 ContextSet 리스트
     */
    public List<ContextSet> getAllContextSets() {
        return contextSetRepository.findAll();
    }

    /**
     * ID로 특정 컨텍스트 세트 조회
     *
     * @param id 조회할 ContextSet의 ID
     * @return ID에 해당하는 ContextSet
     * @throws IllegalArgumentException 존재하지 않을 경우 예외 발생
     */
    public ContextSet getContextSet(Long id) {
        return contextSetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ContextSet not found"));
    }

    /**
     * 컨텍스트 세트 저장 (생성 또는 수정)
     *
     * @param contextSet 저장할 ContextSet 객체
     * @return 저장된 ContextSet
     */
    public ContextSet save(ContextSet contextSet) {
        return contextSetRepository.save(contextSet);
    }
}
