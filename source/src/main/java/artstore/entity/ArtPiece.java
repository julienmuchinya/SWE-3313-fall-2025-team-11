package artstore.entity;

public class ArtPiece {
    private String title;
    private String artist;
    private String description;
    private double price;
    private String image;


    public ArtPiece() {
    }

    public ArtPiece(String title, String artist, String description, double price, String image) {
        this.title = title;
        this.artist = artist;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    public String getTitle() {
        return title; }
    public void setTitle(String title) {
        this.title = title; }

    public String getArtist() {
        return artist; }
    public void setArtist(String artist) {
        this.artist = artist; }

    public String getDescription() {
        return description; }
    public void setDescription(String description) {
        this.description = description; }

    public double getPrice() {
        return price; }
    public void setPrice(double price) {
        this.price = price; }

    public String getImage() {
        return image; }
    public void setImage(String image) {
        this.image = image; }
}
