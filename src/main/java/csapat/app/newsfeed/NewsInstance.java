package csapat.app.newsfeed;

public class NewsInstance {

    private String author;
    private String title;
    private String description;
    private String imagePath;

    public NewsInstance(){}

    public NewsInstance(String author, String title, String description, String imagePath) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle(){
        return title;
    }
    public String getImagePath() {return imagePath;}
}
