package kr.huijoo.dailyinterview.model;

/**
 * Comment.java
 * 작성자 : 박희주
 * V1.0
 * Firebase DB 댓글 모델
 */

public class Comment {
    private String content;
    private String name;

    public Comment() {}

    public Comment(String content, String name) {
        this.content = content;
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
