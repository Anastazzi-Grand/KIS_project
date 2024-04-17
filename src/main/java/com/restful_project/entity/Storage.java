package com.restful_project.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "storages")
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idstorage")
    private Integer idStorage;

    @Column(name = "dateandtime")
    private Date dateAndTime;

    @ManyToOne
    @JoinColumn(name = "idposition", referencedColumnName = "positionid")
    private Specification idPosition;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "typeofoperation")
    private String typeOfOperation;

    public Specification getSpecificationId() {
        return idPosition;
    }

    public void setIdPosition(Specification idPosition) {
        this.idPosition = idPosition;
    }

    public Integer getIdStorage() {
        return idStorage;
    }

    public void setIdStorage(Integer idStorage) {
        this.idStorage = idStorage;
    }

    public Date getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getTypeOfOperation() {
        return typeOfOperation;
    }

    public void setTypeOfOperation(String typeOfOperation) {
        this.typeOfOperation = typeOfOperation;
    }

    @Override
    public String toString() {
        return "Storage{" +
                "idStorage=" + idStorage +
                ", dateAndTime=" + dateAndTime +
                ", idPosition=" + idPosition +
                ", quantity=" + quantity +
                ", typeOfOperation='" + typeOfOperation + '\'' +
                '}';
    }
}
