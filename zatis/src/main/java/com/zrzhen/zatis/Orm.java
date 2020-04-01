package com.zrzhen.zatis;


import com.zrzhen.zatis.anno.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Orm<T> {

    private static final Logger log = LoggerFactory.getLogger(Orm.class);


    /**
     * 实体类类型
     */
    private Class<T> joClass;

    /**
     *
     * @param joClass
     */
    public Orm(Class<T> joClass) {
        this.joClass = joClass;
    }

    /**
     * map list数据转换成实体列表
     *
     * @param poList
     * @return
     */
    public List<T> getEntityList(List<Map<String, Object>> poList) {
        List<T> joList = new ArrayList<>();
        Field[] declaredField = joClass.getDeclaredFields();
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
                            String joColumnName = column.name();
                            if (joColumnName.equalsIgnoreCase(poName)) {
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
                log.error(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
            }
        }
        return joList;
    }


}
