package server;

import java.sql.Date;

public class Documents {

    private final int document_id;
    private String title;
    private String text;
    private Date date;
    private int author_id;

    public Documents(int document_id, String title, String text, Date date, int author_id) {
        this.document_id = document_id;
        this.title = title;
        this.text = text;
        this.date = date;
        this.author_id = author_id;
    }

    public int getDocument_id() {
        return document_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    @Override
    public String toString() {
        return "Documents{" + "document_id =" + document_id + ", title =" + title + ", text =" + text + ", date =" + date + ", author_id =" + author_id + '}';
    }

}
