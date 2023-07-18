package Backend;

public class ratings {
    String Comments,Name,Id;
    int rating;

    public ratings(){};

    public ratings(String comments, String name, String id, int rating) {
        Comments = comments;
        Name = name;
        Id = id;
        this.rating = rating;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
