package Backend;

public class LectureHelper {
    String title, videoLink, notesLink,lPath,courseKey;
    int viewsCount,likeCount,dislikeCount,noLecture;
    Boolean isFinal;

    public LectureHelper(String title, String videoLink, String notesLink, String lPath, int viewsCount, int likeCount, int dislikeCount, int noLecture, Boolean isFinal) {
        this.title = title;
        this.videoLink = videoLink;
        this.notesLink = notesLink;
        this.lPath = lPath;
        this.viewsCount = viewsCount;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.noLecture = noLecture;
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

    public int getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public int getNoLecture() {
        return noLecture;
    }

    public void setNoLecture(int noLecture) {
        this.noLecture = noLecture;
    }

    public String getlPath() {
        return lPath;
    }

    public void setlPath(String lPath) {
        this.lPath = lPath;
    }
}
