package com.smorag.jdbc_hibernate_benchmark.benchmark.jdbc;

import javax.sql.DataSource;
import java.time.LocalDate;

public class JdbcBaseBenchmark {
    protected final DataSource dataSource;
    protected static final String COLUMN_NAMES = "iteration, framework, entity, operation, records, query/method, time_ms";
    public static final String DAY = String.valueOf(LocalDate.now().getDayOfMonth());
    public static final String MONTH = String.valueOf(LocalDate.now().getMonth().getValue());
    protected static final String FRAMEWORK = "JDBC";

    public JdbcBaseBenchmark(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
