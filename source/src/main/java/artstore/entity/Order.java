package artstore.entity;

public class Order {
    private User user;
    private int orderId;
    private String status;
    private double totalPrice;

    public Order() {
    }

    public Order(User user, int orderId, String status, double totalPrice) {
        this.user = user;
        this.orderId = orderId;
        this.status = status;
        this.totalPrice = totalPrice;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

}
