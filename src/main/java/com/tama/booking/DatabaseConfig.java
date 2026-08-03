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
        // Ưu tiên đọc biến môi trường kết nối trên Railway
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null || databaseUrl.isEmpty()) {
            databaseUrl = System.getenv("MYSQL_URL");
        }

        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            try {
                String cleanUrl = databaseUrl.replace("jdbc:", "");
                if (cleanUrl.startsWith("mysql://")) {
                    URI uri = new URI(cleanUrl);
                    String userInfo = uri.getUserInfo();
                    String username = userInfo != null ? userInfo.split(":")[0] : "root";
                    String password = userInfo != null && userInfo.contains(":") ? userInfo.split(":")[1] : "";
                    String host = uri.getHost();
                    int port = uri.getPort() != -1 ? uri.getPort() : 3306;
                    String path = uri.getPath();

                    String jdbcUrl = "jdbc:mysql://" + host + ":" + port + path + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

                    return DataSourceBuilder.create()
                            .driverClassName("com.mysql.cj.jdbc.Driver")
                            .url(jdbcUrl)
                            .username(username)
                            .password(password)
                            .build();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // PHẦN NÀY CHỈ DÙNG KHI CHẠY Ở MÁY PHỤ Ở NHÀ
        String localHostIp = "172.31.130.17"; 
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://" + localHostIp + ":3306/booking?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true")
                .username("root")
                .password("")
                .build();
    }
}