package Backend;



import java.util.ArrayList;

public class CreateCourseHelper {
    String Title, description, basket, thumbnail, language, introlink, key, author, lastUpdate,Cpath;

    int ratings, registrations, noOfRatings;
    public CreateCourseHelper(){}

    public CreateCourseHelper(String title, String description, String basket, String thumbnail, String language, String introlink, String key, String author, String lastUpdate, String cpath, int ratings, int registrations, int noOfRatings) {
        Title = title;
        this.description = description;
        this.basket = basket;
        this.thumbnail = thumbnail;
        this.language = language;
        this.introlink = introlink;
        this.key = key;
        this.author = author;
        this.lastUpdate = lastUpdate;
        Cpath = cpath;
        this.ratings = ratings;
        this.registrations = registrations;
        this.noOfRatings = noOfRatings;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBasket() {
        return basket;
    }

    public void setBasket(String basket) {
        this.basket = basket;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getIntrolink() {
        return introlink;
    }

    public void setIntrolink(String introlink) {
        this.introlink = introlink;
    }

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public int getRegistrations() {
        return registrations;
    }

    public void setRegistrations(int registrations) {
        this.registrations = registrations;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getNoOfRatings() {
        return noOfRatings;
    }

    public void setNoOfRatings(int noOfRatings) {
        this.noOfRatings = noOfRatings;
    }

    public String getCpath() {
        return Cpath;
    }

    public void setCpath(String cpath) {
        Cpath = cpath;
    }
}
