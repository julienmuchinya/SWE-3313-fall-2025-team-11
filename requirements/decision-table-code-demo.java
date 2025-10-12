import java.util.Scanner;
public class decisionTable {
  public static void main(String[] args) {
    String login;
    String administrator;
    String itemsInCart;

    boolean findAndAddItems = false;
    boolean checkout = false;
    boolean manageInventory = false;
    boolean viewSaleHistoryReport = false;
    boolean registerAnAdministrator = false;


    Scanner input = new Scanner(System.in);
    System.out.println("Is the user logged in?(y/n)");
    login = input.nextLine().toLowerCase();
    if(login.equals("y")) {
      findAndAddItems = true;
      
      
      System.out.println("Is the user an administrator?(y/n)");
      administrator = input.nextLine().toLowerCase();
      System.out.println("Does the user have items in the cart?(y/n)");
      itemsInCart = input.nextLine().toLowerCase();

      if(administrator.equals("y")) {
        manageInventory = true;
        viewSaleHistoryReport = true;
        registerAnAdministrator = true;
      }
      if(itemsInCart.equals("y")) {
        checkout = true;
      }
    }

    System.out.println("Find and add items to cart: " + findAndAddItems);
    System.out.println("Checkout: " + checkout);
    System.out.println("Manage Inventory: " + manageInventory);
    System.out.println("View sales history report: " + viewSaleHistoryReport);
    System.out.println("Register an administrator: " + registerAnAdministrator);
  }
}
