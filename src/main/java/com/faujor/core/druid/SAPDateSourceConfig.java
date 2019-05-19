package com.faujor.core.druid;

import java.sql.SQLException;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
// 扫描mapper接口并容器管理
@MapperScan(basePackages = SAPDateSourceConfig.PACKAGE, sqlSessionFactoryRef = "sapSqlSessionFactory")
public class SAPDateSourceConfig {
	private Logger logger = LoggerFactory.getLogger(DruidDataSource.class);
	static final String PACKAGE = "com.faujor.dao.sapcenter";
	private static final String MYBATIS_CONFIG = "mybatis/mybatis-config.xml";
	static final String MAPPER_LOCATION = "classpath:mybatis/mapper/sapcenter/*/*.xml";

	@Value("${sapcenter.datasource.url}")
	private String url;

	@Value("${sapcenter.datasource.username}")
	private String username;

	@Value("${sapcenter.datasource.password}")
	private String password;

	@Value("${sapcenter.datasource.driverClassName}")
	private String driverClass;

	@Value("${spring.datasource.initialSize}")
	private int initialSize;

	@Value("${spring.datasource.minIdle}")
	private int minIdle;

	@Value("${spring.datasource.maxActive}")
	private int maxActive;

	@Value("${spring.datasource.maxWait}")
	private int maxWait;

	@Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
	private int timeBetweenEvictionRunsMillis;

	@Value("${spring.datasource.minEvictableIdleTimeMillis}")
	private int minEvictableIdleTimeMillis;

	@Value("${spring.datasource.validationQuery}")
	private String validationQuery;

	@Value("${spring.datasource.testWhileIdle}")
	private boolean testWhileIdle;

	@Value("${spring.datasource.testOnBorrow}")
	private boolean testOnBorrow;

	@Value("${spring.datasource.testOnReturn}")
	private boolean testOnReturn;

	@Value("${spring.datasource.poolPreparedStatements}")
	private boolean poolPreparedStatements;

	@Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
	private int maxPoolPreparedStatementPerConnectionSize;

	@Value("${spring.datasource.filters}")
	private String filters;

	@Value("{spring.datasource.connectionProperties}")
	private String connectionProperties;

	@Bean(name = "sapDataSource")
	public DataSource sapDataSource() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(driverClass);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);

		// configuration
		dataSource.setInitialSize(initialSize);
		dataSource.setMinIdle(minIdle);
		dataSource.setMaxActive(maxActive);
		dataSource.setMaxWait(maxWait);
		dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		dataSource.setValidationQuery(validationQuery);
		dataSource.setTestWhileIdle(testWhileIdle);
		dataSource.setTestOnBorrow(testOnBorrow);
		dataSource.setTestOnReturn(testOnReturn);
		dataSource.setPoolPreparedStatements(poolPreparedStatements);
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
		try {
			dataSource.setFilters(filters);
		} catch (SQLException e) {
			logger.error("druid configuration initialization filter", e);
		}
		dataSource.setConnectionProperties(connectionProperties);
		return dataSource;
	}

	@Bean(name = "sapTransactionMapper")
	public DataSourceTransactionManager sapTransactionMapper() {
		return new DataSourceTransactionManager(sapDataSource());
	}

	@Bean(name = "sapSqlSessionFactory")
	public SqlSessionFactory sapSqlSessionFactory(@Qualifier("sapDataSource") DataSource sapDataSource)
			throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(sapDataSource);
		sessionFactory.setConfigLocation(new ClassPathResource(MYBATIS_CONFIG));
		sessionFactory.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources(SAPDateSourceConfig.MAPPER_LOCATION));
		return sessionFactory.getObject();
	}
}
