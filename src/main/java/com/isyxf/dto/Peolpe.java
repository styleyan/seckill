package com.isyxf.dto;

public class Peolpe {
    private String name = "yxf";
    private int age = 18;
    private String add = "江西省广丰县";
    private String sex = "男";
    private String values = "默认值";
    private Long timer;

    public Long getTimer() {
        return timer;
    }

    public void setTimer(Long timer) {
        this.timer = timer;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getValues() {
        return values;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Peolpe{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", add='" + add + '\'' +
                ", sex='" + sex + '\'' +
                ", values='" + values + '\'' +
                ", timer=" + timer +
                '}';
    }
}
