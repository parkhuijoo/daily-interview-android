package kr.huijoo.dailyinterview.model;

/**
 * User.java
 * 작성자 : 박희주
 * V1.0
 * Firebase DB 유저 모델
 */

public class User {
    String birth;
    String name;
    String company;
    String position;

    public User(String birth, String name, String company, String position) {
        this.birth = birth;
        this.name = name;
        this.company = company;
        this.position = position;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
