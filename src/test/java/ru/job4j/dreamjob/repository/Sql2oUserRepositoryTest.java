package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.User;

import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Sql2oUserRepositoryTest {
    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oCandidateRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        var users = sql2oUserRepository.findAll();
        for (var user : users) {
            sql2oUserRepository.deleteById(user.getId());
        }
    }

    @Test
    void whenRegisterThenGetSameByEmailAndPassword() {
        var user = sql2oUserRepository.save(new User(0, "email", "name", "password"));
        var saved = sql2oUserRepository.findByEmailAndPassword("email", "password");
        assertThat(saved).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    void whenRegisterTwoWithSameEmailTheError() {
        var user1 = sql2oUserRepository.save(new User(0, "email", "name", "password"));
        var user2 = sql2oUserRepository.save(new User(0, "email", "name", "password"));
        assertThat(user1).isPresent();
        assertThat(user2).isEmpty();
    }

}