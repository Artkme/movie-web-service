package ge.artkme.webapps.model;

public class Movie {
    private String imdbId;
    private String title;

    public Movie(){}

    public Movie(String imdbId, String title) {
        this.imdbId = imdbId;
        this.title = title;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "imdbId='" + imdbId + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
