package com.smorag.jdbc_hibernate_benchmark.benchmark.hibernate;

import javax.sql.DataSource;
import java.time.LocalDate;

public abstract class HibernateBaseBenchmark {
    public static final String DAY = String.valueOf(LocalDate.now().getDayOfMonth());
    public static final String MONTH = String.valueOf(LocalDate.now().getMonth().getValue());
    protected final DataSource dataSource;
    protected final static String COLUMN_NAMES = "iteration, framework, entity, operation, records, query/method, time_ms";
    protected final static String FRAMEWORK = "HIBERNATE";

    public HibernateBaseBenchmark(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
