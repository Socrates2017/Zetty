package com.zrzhen.zatis;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 转换辅助类
 */
public class DbConvert {
    /**
     * 拼接sql语句，不用于执行，仅用于日志
     *
     * @param sql      sql语句
     * @param bindArgs 绑定的参数
     * @return 拼接后的sql
     */
    protected static String sqlStatement(String sql, Object[] bindArgs) {
        StringBuilder sb = new StringBuilder(sql);
        if (bindArgs != null && bindArgs.length > 0) {
            int index = 0;
            for (int i = 0; i < bindArgs.length; i++) {
                index = sb.indexOf("?", index);
                Object arg = bindArgs[i];
                String arg1;
                if (arg instanceof String) {
                    arg1 = "'" + bindArgs[i] + "'";
                } else {
                    arg1 = String.valueOf(bindArgs[i]);
                }
                sb.replace(index, index + 1, arg1);
            }
        }
        return sb.toString();
    }

    /**
     * 将结果集对象封装成List<Map<String, Object>> 对象
     *
     * @param resultSet 结果集
     * @return 结果的封装
     * @throws SQLException
     */
    protected static List<Map<String, Object>> getDatas(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> datas = new ArrayList<>();
        /**获取结果集的数据结构对象**/
        ResultSetMetaData metaData = resultSet.getMetaData();
        while (resultSet.next()) {
            Map<String, Object> rowMap = new HashMap<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                rowMap.put(metaData.getColumnName(i), resultSet.getObject(i));
            }
            datas.add(rowMap);
        }
        return datas;
    }
}
