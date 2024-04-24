package com.restful_project.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.restful_project.service.deserializer.SpecificationDeserializer;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "storage")
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idstorage")
    private Integer idStorage;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private LocalDate date;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "typeofoperation")
    private String typeOfOperation;

    @ManyToOne
    @JoinColumn(name = "idposition", referencedColumnName = "positionid")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonDeserialize(using = SpecificationDeserializer.class)
    private Specification idPosition;

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
                ", dateAndTime=" + date +
                ", idPosition=" + idPosition +
                ", quantity=" + quantity +
                ", typeOfOperation='" + typeOfOperation + '\'' +
                '}';
    }
}
