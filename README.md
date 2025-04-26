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
