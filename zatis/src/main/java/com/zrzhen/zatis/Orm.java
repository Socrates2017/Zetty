package com.zrzhen.zatis;

import com.zrzhen.zatis.anno.Column;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Orm<T> {

    private Class<T> joClass;

    public Orm(Class<T> joClass) {
        this.joClass = joClass;
    }

    public List<T> getList(List<Map<String, Object>> poList) throws SQLException {
        List<T> joList = new ArrayList<>();

        //获取类的属性字段
        Field[] declaredField = joClass.getDeclaredFields();
        //暴力解除，可以访问私有变量
        Field.setAccessible(declaredField, true);

        for (int i = 0; i < poList.size(); i++) {
            Map<String, Object> po = poList.get(i);
            try {
                T jo = joClass.newInstance();

                for (Map.Entry<String, Object> entry : po.entrySet()) {
                    String poName = entry.getKey();
                    Object poValue = entry.getValue();
                    //遍历实体类属性
                    for (int j = 0; j < declaredField.length; j++) {
                        Field field = declaredField[j];
                        //如果属性上有列注解
                        if (field.isAnnotationPresent(Column.class)) {
                            Column column = field.getAnnotation(Column.class);
                            String joName = column.name();
                            if (joName.equalsIgnoreCase(poName)) {
                                // String name = field.getName();//属性名
                                // Class<?> type = field.getType();//属性类型
                                // 将指定对象变量上(user) 此Field对象表示的字段设置为指定的新值。
                                field.set(jo, poValue);
                                break;
                            }
                        }
                    }
                }
                joList.add(jo);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return joList;
    }


}
