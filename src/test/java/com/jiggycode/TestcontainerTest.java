package com.jiggycode;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class TestcontainerTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer;

    static {
       postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
               .withDatabaseName("jiggycode-dao-unit-test")
               .withUsername("jiggycode")
               .withPassword("password");
    }

    @Test
    void canStartPostgresDB() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
    }
}
