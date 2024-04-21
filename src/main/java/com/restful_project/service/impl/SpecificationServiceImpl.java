package com.restful_project.service.impl;

import com.restful_project.entity.Order;
import com.restful_project.entity.Specification;
import com.restful_project.repository.SpecificationRepository;
import com.restful_project.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecificationRepository specificationRepository;

    public List<Specification> getAllSpecifications() {
        System.out.println("GET WORK");
        return specificationRepository.findAll();
    }

    @Override
    public List<Specification> getAllSpecificationsSortedById() {
        return specificationRepository.findAll(Sort.by(Sort.Direction.ASC, "positionid"));
    }

    @Override
    public List<Specification> getAllSpecificationsSortedByName() {
        return specificationRepository.findAll(Sort.by(Sort.Direction.ASC, "description"));
    }

    public Specification getSpecificationById(Long id) {
        return specificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Specification not found with id: " + id));
    }

    public Specification createSpecification(Specification specification) {
        if (specification.getParent() != null && specification.getParent().getPositionid() != null) {
            Long parentId = specification.getParent().getPositionid();
            Specification parentSpecification = getSpecificationById(parentId);
            specification.setParent(parentSpecification);
        }


        return specificationRepository.save(specification);
    }

    public Specification updateSpecification(Long id, Specification existingSpecification) {
        Specification existingSpecificationWithParent = getSpecificationWithParentById(id);

        existingSpecificationWithParent.setDescription(existingSpecification.getDescription());
        existingSpecificationWithParent.setQuantityPerParent(existingSpecification.getQuantityPerParent());
        existingSpecificationWithParent.setUnitMeasurement(existingSpecification.getUnitMeasurement());

        existingSpecificationWithParent.setParent(existingSpecification.getParent());

        return specificationRepository.save(existingSpecificationWithParent);
    }

    public void deleteSpecification(Long id) {
        Specification specification = specificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заказ с id " + id + " не найден"));
        specificationRepository.delete(specification);
    }

    private Specification getSpecificationWithParentById(Long id) {
        Specification specification = getSpecificationById(id);
        if (specification.getParent() != null) {
            Long parentId = specification.getParent().getPositionid();
            if (parentId != null) {
                Specification parentSpecification = getSpecificationById(parentId);
                specification.setParent(parentSpecification);
            } else {
                specification.setParent(null);
            }
        } else {
            specification.setParent(null);
        }
        return specification;
    }
}
