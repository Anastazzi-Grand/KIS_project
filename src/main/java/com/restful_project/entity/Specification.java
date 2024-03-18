package com.restful_project.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "specification")
public class Specification {
    @Id
    private Long positionid;
    @ManyToOne
    @JoinColumn(name = "parentsid")
    private Specification parent;

    @Column(name = "description")
    private String description;

    @Column(name = "quantityperparent")
    private Integer quantityPerParent;
    @Column(name = "unitmeasurement")
    private String unitMeasurement;

    public Long getPositionid() {
        return positionid;
    }

    public void setPositionid(Long positionid) {
        this.positionid = positionid;
    }

    public Specification getParent() {
        return parent;
    }

    public void setParent(Specification parent) {
        this.parent = parent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantityPerParent() {
        return quantityPerParent;
    }

    public void setQuantityPerParent(Integer quantityPerParent) {
        this.quantityPerParent = quantityPerParent;
    }

    public String getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(String unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    @Override
    public String toString() {
        return "Specification{" +
                "positionId=" + positionid +
                ", parent=" + parent +
                ", description='" + description + '\'' +
                ", quantityPerParent=" + quantityPerParent +
                ", unitMeasurement='" + unitMeasurement + '\'' +
                '}';
    }
}
