package com.zetty.zatis;

import com.zrzhen.zatis.anno.Column;
import com.zrzhen.zatis.anno.Table;

@Table(name = "user")
public class User {


    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "money")
    private Double money;


    public User() {
    }

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
