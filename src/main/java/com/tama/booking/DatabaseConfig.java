package com.tama.booking;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

@Configuration
public class DatabaseConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        String databaseUrl = System.getenv("MYSQL_URL");
        
        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            try {
                URI uri = new URI(databaseUrl);
                String username = uri.getUserInfo().split(":")[0];
                String password = uri.getUserInfo().split(":")[1];
                String host = uri.getHost();
                int port = uri.getPort();
                String path = uri.getPath();

                String jdbcUrl = "jdbc:mysql://" + host + ":" + port + path + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

                return DataSourceBuilder.create()
                        .driverClassName("com.mysql.cj.jdbc.Driver")
                        .url(jdbcUrl)
                        .username(username)
                        .password(password)
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Fallback chạy ở máy nhà (Localhost của XAMPP)
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://localhost:3306/booking?useSSL=false&serverTimezone=UTC")
                .username("root")
                .password("")
                .build();
    }
}