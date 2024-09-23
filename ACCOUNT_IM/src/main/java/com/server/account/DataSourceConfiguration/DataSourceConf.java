package com.server.account.DataSourceConfiguration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@MapperScan(value="com.server.account.Users.Mapper")
@MapperScan(value="com.server.account.MsSql.MapperPack", sqlSessionFactoryRef="MSSQLSqlSessionFactory")
public class DataSourceConf {
	
	/**
	 * Master Pool
	 * IBM DB2 DataSourceConfiguration
	 * @return
	 */
	
	@Primary
	@Bean
	@ConfigurationProperties(prefix="spring.db2.datasource.hikari")
	public DataSource Db2DataSource() {
		return DataSourceBuilder.create()
				.type(HikariDataSource.class)
				.build();
	}
	
	@Primary
	@Bean
	public SqlSessionFactory Db2SqlSessionFactory(DataSource Db2DataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(Db2DataSource);
		
		Resource myBatisConfig = new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml");
		bean.setConfigLocation(myBatisConfig);
		
		Resource[] resouce = new PathMatchingResourcePatternResolver().getResources("classpath:mappers/*Mapper.xml");
		bean.setMapperLocations(resouce);
		
		return bean.getObject();
	}
	
	@Primary
	@Bean
	public DataSourceTransactionManager Db2TranscationManager(DataSource Db2DataSource) {
		return new DataSourceTransactionManager(Db2DataSource);
	}
	
	@Primary
	@Bean
	public PlatformTransactionManager transactionManager() {
	  return new DataSourceTransactionManager(Db2DataSource());
	}
	
	/**
	 * MS-SQL DataSourceConfiguration
	 * @return
	 */
	
	@Bean
	@ConfigurationProperties(prefix="spring.mssql.datasource.hikari")
	public DataSource MSSQLDataSource() {
		return DataSourceBuilder.create()
				.type(HikariDataSource.class)
				.build();
	}
	
	@Bean
	public SqlSessionFactory MSSQLSqlSessionFactory(@Qualifier("MSSQLDataSource") DataSource MSSQLDataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(MSSQLDataSource);
		
		Resource myBatisConfig = new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml");
		bean.setConfigLocation(myBatisConfig);
		
		Resource[] resouce = new PathMatchingResourcePatternResolver().getResources("classpath:mappers/mssql/*Mapper.xml");
		bean.setMapperLocations(resouce);
		
		return bean.getObject();
	}
	
	@Bean
	public DataSourceTransactionManager MSSQLTranscationManager(@Qualifier("MSSQLDataSource") DataSource MSSQLDataSource) {
		return new DataSourceTransactionManager(MSSQLDataSource);
	}

}
