package kr.huijoo.dailyinterview.model;

public class QList {
    private String listimg;
    private String listdate;
    private String listtitle;
    private String order;

    public QList() {}

    public QList(String listimg, String listdate, String listtitle, String order){
        this.listimg = listimg;
        this.listdate = listdate;
        this.listtitle = listtitle;
        this.order = order;
    }

    public String getListimg() {
        return listimg;
    }

    public void setListimg(String listimg) {
        this.listimg = listimg;
    }

    public String getListdate() {
        return listdate;
    }

    public void setListdate(String listdate) {
        this.listdate = listdate;
    }

    public String getListtitle() {
        return listtitle;
    }

    public void setListtitle(String listtitle) {
        this.listtitle = listtitle;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
