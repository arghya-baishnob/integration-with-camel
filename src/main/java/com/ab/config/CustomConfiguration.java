package com.ab.config;

import org.apache.camel.CamelContext;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


import javax.sql.DataSource;

/**
 * Created by arghya on 2/19/2016.
 */
@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = {"com.ab.route"})
public class CustomConfiguration extends CamelConfiguration {

    @Value("${db.driver}")
    private String dbDriver;

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.username}")
    private String dbUsername;

    @Value("${db.password}")
    private String dbPassword;

    // To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Override
    protected void setupCamelContext(CamelContext camelContext) throws Exception {
        final PropertiesComponent pc = new PropertiesComponent();
        pc.setLocation("classpath:components.properties");
        camelContext.addComponent("properties", pc);
        super.setupCamelContext(camelContext);
    }

    @Bean(name = "db.arg")
    public DataSource argDataSource() {
        //return createDataSource(dbDriver, dbUsername, dbPassword, dbUrl);
        return createDataSource("oracle.jdbc.driver.OracleDriver", "arghya", "arghya", "jdbc:oracle:thin:@localhost:1521:XE");
    }

    private DataSource createDataSource(final String driver, final String username, final String password, final String url) {
        final PoolProperties p = new PoolProperties();
        p.setUrl(url);
        p.setDriverClassName(driver);
        p.setUsername(username);
        p.setPassword(password);
        p.setInitialSize(5);
        p.setMinIdle(5);
        p.setMaxActive(100);
        p.setMaxIdle(100);
        p.setValidationQuery("SELECT NOW()");
        p.setTestOnBorrow(true);
        p.setTestWhileIdle(true);
        p.setTimeBetweenEvictionRunsMillis(60000);
        p.setConnectionProperties("cachePrepStmts=true;prepStmtCacheSize=100;zeroDateTimeBehavior=convertToNull");
        p.setDefaultTransactionIsolation(2);
        p.setValidationInterval(30000);
        return new org.apache.tomcat.jdbc.pool.DataSource(p);
    }
}