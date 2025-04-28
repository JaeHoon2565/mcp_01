#  MCP Server

LLM 기반 시스템에서 **프롬프트 / 응답 / 맥락 정보**를 구분 저장하고,  
자동 임베딩 및 추후 벡터 DB 연동을 위한 **AI 중계 및 메타관리 서버**입니다.

---

##  프로젝트 구조

```bash
src
 └─ main
     ├─ java.com.mcp.server
     │   ├─ client        # 외부 LLM API 클라이언트
     │   ├─ config        # Spring 설정 클래스
     │   ├─ controller    # 관리자용 Web 컨트롤러
     │   ├─ domain        # JPA 엔티티 및 리포지토리
     │   ├─ dto           # 요청/응답 DTO
     │   ├─ exception     # 공통 예외 처리
     │   ├─ scheduler     # 임베딩 백필 스케줄러
     │   ├─ service       # 도메인 로직 서비스
     │   └─ util          # 공통 유틸성 기능
     └─ resources
         ├─ templates/admin  # 관리자 페이지 (Thymeleaf)
         ├─ static           # 정적 리소스
         └─ META-INF


🔄 주요 기능

기능	설명
LogEmbeddingMetadata	       프롬프트/쿼리/결과 데이터를 로그 기반으로 저장
ContextEmbeddingMetadata       페르소나/상황/역할 등 맥락 정보를 JSON으로 저장
EmbeddingBackfillService   	누락된 벡터화 대상 메타데이터를 자동 생성
EmbeddingScheduler         	백필 서비스 1시간마다 자동 실행 (@Scheduled)
Admin UI	/admin/*           페이지로 관리자 기능 제공 예정


MCP 서버 향후 계획 및 발전 로드맵
1. 현재 단계
Spring Boot 기반 자체 임베딩 파이프라인 구축

메타데이터 저장 완료 (ContextEmbeddingMetadata, LogEmbeddingMetadata)

임베딩 대상 데이터 백필 스케줄러 구성 완료

MongoDB Atlas에 임시 벡터 저장 예정

✅ 목표: Spring Boot 내부에서 임베딩 → 저장까지 자동화된 기본 파이프라인 구축

2. 단기 목표 (2025년 상반기)
MongoDB → Weaviate 또는 Qdrant로 벡터 데이터 이관

유사도 검색 API 개발

예: "비슷한 컨텍스트 찾아 반환"

Cosine Similarity / Inner Product 기반 검색 기능 구현

VectorSearchService 추가 설계

✅ 목표: Mongo → 벡터DB → 검색 API까지 자연스럽게 이어지는 흐름 구축

3. 중장기 목표 (2025년 하반기)
RAG (Retrieval-Augmented Generation) 구조 적용

유사 컨텍스트 검색 후 프롬프트에 삽입하여 LLM 응답 품질 향상

Embedding 품질 모니터링 시스템 구축

Prometheus + Grafana

다중 임베딩 모델 지원

OpenAI, Groq, Huggingface 모델 사용 가능화

스케줄러 고도화

실패 감지 및 Slack/Discord 알림

자동 재시도 로직 도입

Embedding Metadata History 테이블 추가 고려

✅ 목표: 신뢰도 높고 품질 관리 가능한 LLM 기반 시스템 완성

🗂 기술 스택 (계획 포함)

구분	스택
백엔드 서버	Spring Boot 3.x
데이터베이스	MySQL, MongoDB Atlas
벡터 DB	Weaviate, Qdrant
유사도 검색	Cosine Similarity, Inner Product
임베딩 모델	Groq API, OpenAI API, Huggingface
모니터링	Prometheus, Grafana
알림 시스템	Slack, Discord Webhook
📄 요약

단계	주요 목표	키워드
현재	임베딩 파이프라인 구축	Spring Boot, MongoDB
단기	벡터DB 연동 및 검색 기능 추가	Weaviate, Qdrant, 유사도 검색
중장기	RAG + 품질 모니터링 시스템 고도화	RAG, Prometheus, 다중 임베딩 지원
📌 추가 메모
본 프로젝트는 Spring Boot 기반으로 임베딩/메타데이터 관리를 자동화하며,
이후 유사도 검색 및 RAG 기반 LLM 품질 향상을 목표로 합니다.

✨ [작성 버전: 2025-04-27 기준]

