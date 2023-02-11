package com.hzq.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @Author hzq
 * @ClassName com.hzq.dto.PersonDTO
 * @Date 2022/11/8 20:04
 * @Description
 */
public class PersonDTO {

    @JsonProperty("name")
    private String name;
    @JsonProperty("age")
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "PersonDTO{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public PersonDTO(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
