public class Sales {

    private int bookId;
    private int customersId;
    private int quantitySold;
    private double totalPrice;


    public Sales(int bookId, int customersId, int quantitySold, double totalPrice) {
        this.bookId = bookId;
        this.customersId = customersId;
        this.quantitySold = quantitySold;
        this.totalPrice = totalPrice;
    }

    public int getBookId() {
        return bookId;
    }

    public int getCustomersId() {
        return customersId;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
