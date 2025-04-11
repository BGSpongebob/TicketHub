package org.acme.Model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity(name = "Organizers")
public class Organizer extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organizerSeq")
    @SequenceGenerator(name = "organizerSeq", sequenceName = "organizer_seq", allocationSize = 1)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name1;

    @Column(length = 100, nullable = false)
    private String name2;

    @Column(length = 20, nullable = false, unique = true)
    private String phone;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @OneToMany(mappedBy = "organizer", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> organizedEvents;

    @OneToMany(mappedBy = "organizer", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings;

    // Public empty constructor
    public Organizer() {}

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Event> getOrganizedEvents() {
        return organizedEvents;
    }

    public void setOrganizedEvents(List<Event> organizedEvents) {
        this.organizedEvents = organizedEvents;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    // toString method
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name1='" + name1 + '\'' +
                ", name2='" + name2 + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", user=" + user +
                ", organizedEvents=" + organizedEvents +
                ", ratings=" + ratings +
                '}';
    }
}
