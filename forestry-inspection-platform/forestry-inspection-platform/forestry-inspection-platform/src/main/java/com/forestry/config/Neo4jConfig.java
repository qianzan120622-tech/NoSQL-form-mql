package com.forestry.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Neo4j配置
 */
@Configuration
@EnableNeo4jRepositories(basePackages = "com.forestry.repository")
@EnableTransactionManagement
public class Neo4jConfig {
}