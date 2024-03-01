package entity;


//    date_of_sale DATE,
//    quantity_sold INTEGER NOT NULL CHECK(quantity_sold >= 0),
//    total_price REAL NOT NULL CHECK(total_price >= 0),
//    CONSTRAINT fk_book FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE SET NULL,
//    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE SET NULL

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "sales", schema = "public")


public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long saleId;
    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    @Column(name = "data_of_sale")
    private LocalDate dateOfSale;

    @Column(name = "quantity_solid", nullable = false)
    private Integer quantitySolid;

    @Column(name = "total_ptice", nullable = false)
    private Double totalPrice;


    public void setBook(Book book) {
        this.book = book;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setDateOfSale(LocalDate dateOfSale) {
        this.dateOfSale = dateOfSale;
    }

    public void setQuantitySolid(Integer quantitySolid) {
        this.quantitySolid = quantitySolid;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Sales{" +
                "saleId=" + saleId +
                ", book=" + book +
                ", customer=" + customer +
                ", dateOfSale=" + dateOfSale +
                ", quantitySolid=" + quantitySolid +
                ", totalPrice=" + totalPrice +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sales sales = (Sales) o;
        return  Objects.equals(saleId, sales.saleId) &&
                Objects.equals(book, sales.book) &&
                Objects.equals(customer, sales.customer) &&
                Objects.equals(dateOfSale, sales.dateOfSale) &&
                Objects.equals(quantitySolid, sales.quantitySolid) &&
                Objects.equals(totalPrice, sales.totalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(saleId, book, customer, dateOfSale, quantitySolid, totalPrice);
    }
}
