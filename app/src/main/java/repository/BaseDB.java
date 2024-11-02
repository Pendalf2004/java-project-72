package repository;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Setter;

@Setter
public class BaseDB {
    public static HikariDataSource dataConfig;

}
