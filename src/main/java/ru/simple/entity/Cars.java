package ru.simple.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cars"
    ,catalog="automobile"
)
public class Cars  implements java.io.Serializable {


     private Integer id;
     private String model;
     private Integer year;
     private Integer price;
     private String color;

    public Cars() {
    }

	
    public Cars(String model) {
        this.model = model;
    }
    public Cars(String model, Integer year, Integer price, String color) {
       this.model = model;
       this.year = year;
       this.price = price;
       this.color = color;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="id", unique=true, nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    
    @Column(name="model", nullable=false, length=45)
    public String getModel() {
        return this.model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }

    
    @Column(name="year")
    public Integer getYear() {
        return this.year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }

    
    @Column(name="price")
    public Integer getPrice() {
        return this.price;
    }
    
    public void setPrice(Integer price) {
        this.price = price;
    }

    
    @Column(name="color", length=45)
    public String getColor() {
        return this.color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }




}


