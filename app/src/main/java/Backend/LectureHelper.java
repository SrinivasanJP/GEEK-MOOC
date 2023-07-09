package Backend;

public class LectureHelper {
    String title, videoLink, notesLink;
    Boolean isFinal;

    public LectureHelper(String title, String videoLink, String notesLink, Boolean isFinal) {
        this.title = title;
        this.videoLink = videoLink;
        this.notesLink = notesLink;
        this.isFinal = isFinal;
    }

    public LectureHelper() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getNotesLink() {
        return notesLink;
    }

    public void setNotesLink(String notesLink) {
        this.notesLink = notesLink;
    }

    public Boolean getFinal() {
        return isFinal;
    }

    public void setFinal(Boolean aFinal) {
        isFinal = aFinal;
    }
}
