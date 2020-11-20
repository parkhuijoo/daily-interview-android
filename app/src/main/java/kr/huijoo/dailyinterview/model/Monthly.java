package kr.huijoo.dailyinterview.model;

/**
 * Monthly.java
 * 작성자 : 박희주
 * V1.0
 * Firebase DB 이달의 답변 모델
 */

public class Monthly {
    private String answer;
    private String img;
    private String question;

    public Monthly() {}

    public Monthly(String answer, String img, String question){
        this.answer = answer;
        this.img = img;
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
