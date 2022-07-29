package org.example;

import javax.persistence.*;
@Entity
@Table(name = "Address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String street;
    @Column(nullable = false)
    private Integer building_Num;
    @Column(nullable = false)
    private Integer flat_Num;
    @OneToOne(mappedBy = "address")
    private Flat flat;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getStreet() { return street; }

    public void setStreet(String street) { this.street = street; }

    public Integer getBuilding_Num() { return building_Num; }

    public void setBuilding_Num(Integer building_Num) { this.building_Num = building_Num; }

    public Integer getFlat_Num() { return flat_Num;}

    public void setFlat_Num(Integer flat_Num) { this.flat_Num = flat_Num;}

    public Flat getFlat() { return flat; }

    public void setFlat(Flat flat) { this.flat = flat; }

    public Address() {
    }

    public Address(String city, String street, Integer building_Num, Integer flat_Num) {
        this.city = city;
        this.street = street;
        this.building_Num = building_Num;
        this.flat_Num = flat_Num;
    }

    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", building_Num=" + building_Num +
                ", flat_Num=" + flat_Num +
                '}';
    }
}
