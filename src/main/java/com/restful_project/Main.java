package com.restful_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@EnableJpaRepositories("com.restful_project.repository")
@ComponentScan("com.restful_project.*")
@EnableConfigurationProperties
public class Main {
    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        // Получаем DataSource
        DataSource dataSource = context.getBean(DataSource.class);
        Connection connection = null;

        try {
            // Устанавливаем соединение с базой данных
            connection = dataSource.getConnection();
            System.out.println("Соединение с базой данных успешно установлено!");

        } catch (SQLException e) {
            System.out.println("Ошибка при установлении соединения с базой данных: " + e.getMessage());
        } finally {
            // Закрываем соединение
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Ошибка при закрытии соединения с базой данных: " + e.getMessage());
                }
            }
        }
    }
}
