package com.zrzhen.zatis;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
                    arg1 = "'" + arg + "'";
                } else {
                    arg1 = String.valueOf(arg);
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
            Map<String, Object> rowMap = new LinkedHashMap<>();//LinkedHashMap能保证顺序
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                rowMap.put(metaData.getColumnName(i), resultSet.getObject(i));
            }
            datas.add(rowMap);
        }
        return datas;
    }


    /**
     * @param distinct
     * @param tables
     * @param columns
     * @param where
     * @param groupBy
     * @param having
     * @param orderBy
     * @param limit
     * @return
     */
    public static String buildQueryString(
            boolean distinct, String tables, String[] columns, String where,
            String groupBy, String having, String orderBy, String limit) {
        if (isEmpty(groupBy) && !isEmpty(having)) {
            throw new IllegalArgumentException(
                    "HAVING clauses are only permitted when using a groupBy clause");
        }
        if (!isEmpty(limit) && !sLimitPattern.matcher(limit).matches()) {
            throw new IllegalArgumentException("invalid LIMIT clauses:" + limit);
        }

        StringBuilder query = new StringBuilder(120);

        query.append("SELECT ");
        if (distinct) {
            query.append("DISTINCT ");
        }
        if (columns != null && columns.length != 0) {
            appendColumns(query, columns);
        } else {
            query.append(" * ");
        }
        query.append("FROM ");
        query.append(tables);
        appendClause(query, " WHERE ", where);
        appendClause(query, " GROUP BY ", groupBy);
        appendClause(query, " HAVING ", having);
        appendClause(query, " ORDER BY ", orderBy);
        appendClause(query, " LIMIT ", limit);
        return query.toString();
    }

    /**
     * Add the names that are non-null in columns to s, separating
     * them with commas.
     */
    private static void appendColumns(StringBuilder s, String[] columns) {
        int n = columns.length;

        for (int i = 0; i < n; i++) {
            String column = columns[i];

            if (column != null) {
                if (i > 0) {
                    s.append(", ");
                }
                s.append(column);
            }
        }
        s.append(' ');
    }

    /**
     * addClause
     *
     * @param s      the add StringBuilder
     * @param name   clauseName
     * @param clause clauseSelection
     */
    private static void appendClause(StringBuilder s, String name, String clause) {
        if (!isEmpty(clause)) {
            s.append(name);
            s.append(clause);
        }
    }

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    private static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * the pattern of limit
     */
    private static final Pattern sLimitPattern =
            Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");
}
