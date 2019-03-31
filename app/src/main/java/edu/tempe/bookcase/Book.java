package edu.tempe.bookcase;

public class Book {
    private int id;
    private String title;
    private String author;
    private int published;
    private String coverURL;

    public Book(int id, String coverURL, int published, String title, String author){
        setId(id);
        setCoverURL(coverURL);
        setPublished(published);
        setTitle(title);
        setAuthor(author);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public void setPublished(int published) {
        this.published = published;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public int getPublished() {
        return published;
    }

    public String getCoverURL() {
        return coverURL;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }
}
