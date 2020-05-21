package com.zetty.zatis;

import com.zrzhen.zatis.Orm;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrmTest {

    public static void main(String[] args) throws SQLException {

        Orm<User> a = new Orm<>(User.class);
        List<User> userList = a.getEntityList(poList());


    }

    public static List<Map<String, Object>> poList() {
        List<Map<String, Object>> poList = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("id", 334);
        map1.put("name", "张三");
        map1.put("money", 2.1);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("id", 2);
        map2.put("name", "李四");
        map2.put("money", 3.441);
        poList.add(map1);
        poList.add(map2);
        return poList;
    }
}
