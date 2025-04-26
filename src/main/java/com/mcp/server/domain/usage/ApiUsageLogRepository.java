package com.mcp.server.domain.usage;

import com.mcp.server.dto.ApiUsageStats;
import com.mcp.server.dto.ApiUsageSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * ✅ ApiUsageLogRepository
 * - 모델별/날짜별 사용량 조회에 사용
 */
public interface ApiUsageLogRepository extends JpaRepository<ApiUsageLog, Long> {
    List<ApiUsageLog> findByDate(LocalDate date);
    List<ApiUsageLog> findByDateAndModel(LocalDate date, String model);


    List<ApiUsageLog> findByDateBetween(LocalDate from, LocalDate to);

    @Query("""
        SELECT new com.mcp.server.dto.ApiUsageStats(
            u.date,
            u.model,
            COUNT(u),
            SUM(u.tokensUsed),
            AVG(u.elapsedTimeMs)
        )
        FROM ApiUsageLog u
        WHERE u.date BETWEEN :from AND :to
        GROUP BY u.date, u.model
        ORDER BY u.date ASC
    """)
    List<ApiUsageStats> findStatsBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("""
    SELECT new com.mcp.server.dto.ApiUsageSummary(
        u.date,
        u.model,
        COUNT(u),
        SUM(u.tokensUsed),
        AVG(u.elapsedTimeMs)
    )
    FROM ApiUsageLog u
    WHERE u.date BETWEEN :from AND :to
    GROUP BY u.date, u.model
    ORDER BY u.date ASC
""")
    List<ApiUsageSummary> findSummaryBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);
}