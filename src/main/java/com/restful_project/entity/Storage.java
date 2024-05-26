package com.restful_project.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.restful_project.service.deserializer.SpecificationDeserializer;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

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

    @Column(name = "measureunit")
    private String measureUnit;

    @Column(name = "typeofoperation")
    private String typeOfOperation;

    @ManyToOne
    @JoinColumn(name = "specificationid", referencedColumnName = "positionid")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonDeserialize(using = SpecificationDeserializer.class)
    private Specification specificationId;

    public Storage() {}

    public Storage(Integer idStorage, LocalDate date, Integer quantity, String measureUnit, String typeOfOperation, Specification specificationId) {
        this.idStorage = idStorage;
        this.date = date;
        this.quantity = quantity;
        this.typeOfOperation = typeOfOperation;
        this.specificationId = specificationId;
        this.measureUnit = measureUnit;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }
    public Specification getSpecificationId() {
        return specificationId;
    }

    public void setSpecificationId(Specification idPosition) {
        this.specificationId = idPosition;
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
                ", idPosition=" + specificationId +
                ", quantity=" + quantity +
                ", typeOfOperation='" + typeOfOperation + '\'' +
                '}';
    }
}
