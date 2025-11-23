package com.forestry.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * MongoDB配置
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.forestry.repository")
public class MongoConfig {
}