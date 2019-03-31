package ru.lilitweb.books.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "ru.lilitweb.books.repository")
@EnableMongoAuditing
public class MongoConfig {
}
