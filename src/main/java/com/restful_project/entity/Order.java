package com.restful_project.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clientname", nullable = false)
    private String clientName;

    @Column(name = "orderdate")
    private LocalDate orderDate;

    @Column(name = "count")
    private Integer count;

    @Column(name = "measureunit")
    private String measureUnit;

    @ManyToOne
    @JoinColumn(name = "specificationid")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Specification specificationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Specification getSpecificationId() {
        return specificationId;
    }

    public void setSpecificationId(Specification specificationId) {
        this.specificationId = specificationId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", clientName='" + clientName + '\'' +
                ", specificationId=" + specificationId +
                ", orderDate=" + orderDate +
                ", count=" + count +
                ", measureUnit='" + measureUnit + '\'' +
                '}';
    }
}
