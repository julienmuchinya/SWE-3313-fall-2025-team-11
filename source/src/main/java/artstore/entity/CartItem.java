package artstore.entity;

public class CartItem {
    private ArtPiece artPiece;
    private int price;
    private int totalQuantity;
    private double totalPrice;

    public CartItem() {
    }
    public CartItem(ArtPiece artPiece, int price, int totalQuantity, double totalPrice) {
        this.artPiece = artPiece;
        this.price = price;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
    }
    public ArtPiece getArtPiece() {
        return artPiece;
    }
    public void setArtPiece(ArtPiece artPiece) {
        this.artPiece = artPiece;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public int getTotalQuantity() {
        return totalQuantity;
    }
    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
    public double getTotalPrice() {
        return totalPrice;

    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
