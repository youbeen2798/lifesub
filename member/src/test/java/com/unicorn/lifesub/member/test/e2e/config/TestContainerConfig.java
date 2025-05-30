// TestContainerConfig.java
package com.unicorn.lifesub.member.test.e2e.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

/**
 * 테스트 환경을 위한 PostgreSQL 컨테이너 설정 클래스입니다.
 * Testcontainers를 사용하여 테스트용 PostgreSQL 데이터베이스를 관리합니다.
 *
 * @author Tech Lead
 * @version 1.1
 */
@Testcontainers
public class TestContainerConfig {

    /**
     * PostgreSQL 테스트 컨테이너 인스턴스입니다.
     * 테스트 실행 시 자동으로 시작되며, 테스트 종료 시 자동으로 정리됩니다.
     */
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13.2-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withStartupTimeout(Duration.ofSeconds(60))
            .withReuse(true);

    /**
     * 테스트 컨테이너의 데이터베이스 속성을 Spring 환경에 동적으로 등록합니다.
     * 커넥션 풀 설정을 테스트 환경에 최적화하여 경고를 제거합니다.
     *
     * @param registry 스프링 속성 레지스트리
     */
    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        // 데이터베이스 기본 연결 정보
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);

        // JPA 설정
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
        registry.add("spring.jpa.hibernate.show_sql", () -> "true");

        // HikariCP 커넥션 풀 최적화 설정
        registry.add("spring.datasource.hikari.maximum-pool-size", () -> "3");  // 축소
        registry.add("spring.datasource.hikari.minimum-idle", () -> "1");       // 축소
        registry.add("spring.datasource.hikari.idle-timeout", () -> "10000");   // 10초
        registry.add("spring.datasource.hikari.connection-timeout", () -> "5000"); // 5초
        registry.add("spring.datasource.hikari.max-lifetime", () -> "30000");     // 30초
        registry.add("spring.datasource.hikari.validation-timeout", () -> "2500"); // 2.5초
        registry.add("spring.datasource.hikari.connection-test-query", () -> "SELECT 1");
        registry.add("spring.datasource.hikari.leak-detection-threshold", () -> "5000"); // 5초

        // 커넥션 풀 자동 종료 설정
        registry.add("spring.datasource.hikari.auto-commit", () -> "true");
        registry.add("spring.datasource.hikari.pool-name", () -> "TestHikariPool");
    }
}