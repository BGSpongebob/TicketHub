package org.acme.Model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.List;

@Entity(name="Clients")
public class Client extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clientSeq")
    @SequenceGenerator(name = "clientSeq", sequenceName = "client_seq", allocationSize = 1)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name1;

    @Column(length = 100, nullable = false)
    private String name2;

    @Column(length = 20, nullable = false, unique = true)
    private String phone;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sale> sales;

    // Public empty constructor
    public Client() {}

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }

    // toString method
    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name1='" + name1 + '\'' +
                ", name2='" + name2 + '\'' +
                ", phone='" + phone + '\'' +
                ", sales=" + sales +
                '}';
    }
}
