package com.zetty.zatis;

import com.zrzhen.zatis.DbSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbSourceTest {


    public static void main(String[] args) throws SQLException {
        witoutPool4();
    }

    public static void witoutPool4() throws SQLException {

        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://192.168.148.151:3306/lending-riskcontrol-test?useSSL=true&characterEncoding=utf8&useUnicode=true&zeroDateTimeBehavior=convertToNull";
        String user = "xjd_user_test";
        String password = "66I319DVbygAbnA61";
        String sql = "SELECT a.order_id, a.repay_money  AS overdue FROM t_lending_repayment a WHERE a.repay_status = 2";

        Integer dbMarketInitialSize = 5;
        Integer dbMarketMaxActive = 100;
        Integer dbMarketMinIdle = 5;
        Integer dbMarketMaxWait = 6000;


        DbSource db = new DbSource(driver, url, user, password, true, dbMarketInitialSize, dbMarketMaxActive, dbMarketMinIdle, dbMarketMaxWait);

        String tableName = "t_r360_score";

        List<Map<String, Object>> datas = new ArrayList<>();

        Map<String, Object> row1 = new HashMap<>();
        row1.put("id", "testId200");
        row1.put("user_id", "test_user_id1");
        row1.put("name", "testname1");
        datas.add(row1);

        Map<String, Object> row2 = new HashMap<>();
        row2.put("id", "testId2200");
        row2.put("user_id", "test_user_id1");
        row2.put("name", "testname1");
        datas.add(row2);

        db.begin();
        int rowNum = db.insertAll(tableName, datas);
        db.commit();
        System.out.println(rowNum);


    }
}
