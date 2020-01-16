package com.zrzhen.zetty.im.mybatis;

import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.util.Properties;

public class MyBatisJavaDemo {

    public static void main(String[] args) {
        String name = selectName(1);
        System.out.println(name);
    }


    private static String selectName(Integer id){
        String name = null;
        try (SqlSession session = openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            name = mapper.selectName(id);
        }
        return name;
    }


    private static SqlSession openSession(){
        Properties properties = new Properties();
        properties.setProperty("driver", "com.mysql.cj.jdbc.Driver");
        properties.setProperty("url", "jdbc:mysql://192.168.103.140:3006/cms?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&autoReconnect=true&failOverReadOnly=false&useOldAliasMetadataBehavior=true");
        properties.setProperty("username", "chenanl");
        properties.setProperty("password", "123Aa45678@");
        PooledDataSourceFactory pooledDataSourceFactory = new PooledDataSourceFactory();
        pooledDataSourceFactory.setProperties(properties);
        DataSource dataSource = pooledDataSourceFactory.getDataSource();

        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(UserMapper.class);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession;
    }

}
