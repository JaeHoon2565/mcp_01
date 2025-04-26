package com.mcp.server.util;


import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * ✅ DotenvEnvironmentPostProcessor
 *
 * 📌 목적:
 * - Spring Boot 시작 시 `.env` 파일의 내용을 자동으로 로딩하여
 *   `application.yml`처럼 환경 설정에 반영되도록 한다.
 *
 * 🧩 동작 원리:
 * - Spring Boot는 EnvironmentPostProcessor를 통해
 *   애플리케이션 실행 전에 환경을 구성할 수 있도록 허용함.
 * - `spring-dotenv` 라이브러리의 `DotenvPropertySource`를 이용해 `.env` 파일을 읽고,
 *   그 내용을 가장 우선순위 높은 프로퍼티 소스로 등록.
 *
 * ⚙️ 예시:
 * .env 파일에 다음을 작성하면,
 *   TOGETHER_API_KEY=abc123
 *
 * application.yml 또는 Java 코드에서
 *   ${TOGETHER_API_KEY} 또는 environment.getProperty("TOGETHER_API_KEY") 로 사용 가능.
 *
 * 📝 META-INF/spring.factories 파일에 등록되어야 동작함:
 * org.springframework.boot.env.EnvironmentPostProcessor=\
 *   com.mcp.server.DotenvEnvironmentPostProcessor
 */
public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Dotenv dotenv = Dotenv.configure()
                .filename(".env")
                .ignoreIfMissing()
                .load();

        Map<String, Object> map = new HashMap<>();
        dotenv.entries().forEach(entry -> map.put(entry.getKey(), entry.getValue()));

        environment.getPropertySources().addFirst(new MapPropertySource("dotenv", map));
    }
}
