package com.smorag.jdbc_hibernate_benchmark;

import com.smorag.jdbc_hibernate_benchmark.benchmark.hibernate.HibernateOrderBenchmark;
import com.smorag.jdbc_hibernate_benchmark.benchmark.hibernate.HibernateUserBenchmark;
import com.smorag.jdbc_hibernate_benchmark.benchmark.jdbc.JdbcOrderBenchmark;
import com.smorag.jdbc_hibernate_benchmark.benchmark.jdbc.JdbcUserBenchmark;
import com.smorag.jdbc_hibernate_benchmark.utils.DatabaseCleaner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;

@SpringBootApplication
public class JdbcHibernateBenchmarkApplication implements CommandLineRunner {

	private final HibernateUserBenchmark hibernateUserBenchmark;
	private final JdbcUserBenchmark jdbcUserBenchmark;
	private final HibernateOrderBenchmark hibernateOrderBenchmark;
	private final JdbcOrderBenchmark jdbcOrderBenchmark;
	DataSource dataSource;

	public JdbcHibernateBenchmarkApplication(
			HibernateUserBenchmark hibernateUserBenchmark,
			JdbcUserBenchmark jdbcUserBenchmark,
			HibernateOrderBenchmark hibernateOrderBenchmark,
			JdbcOrderBenchmark jdbcOrderBenchmark,
			DataSource dataSource) {
		this.hibernateUserBenchmark = hibernateUserBenchmark;
		this.jdbcUserBenchmark = jdbcUserBenchmark;
		this.hibernateOrderBenchmark = hibernateOrderBenchmark;
		this.jdbcOrderBenchmark = jdbcOrderBenchmark;
		this.dataSource = dataSource;
	}

	public static void main(String[] args) {
		SpringApplication.run(JdbcHibernateBenchmarkApplication.class, args);

	}

	@Override
	public void run(String... args) {

		DatabaseCleaner.clearDatabase(dataSource);
		BenchmarkRunner benchmarkRunner = new BenchmarkRunner();

		benchmarkRunner.startUserBenchmark(jdbcUserBenchmark, hibernateUserBenchmark, 30);
		benchmarkRunner.startOrderBenchmark(jdbcOrderBenchmark, hibernateOrderBenchmark, 30);

	}

}
