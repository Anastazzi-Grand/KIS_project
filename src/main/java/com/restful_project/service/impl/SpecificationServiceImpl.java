package com.restful_project.service.impl;

import com.restful_project.entity.Specification;
import com.restful_project.repository.SpecificationRepository;
import com.restful_project.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecificationRepository specificationRepository;

    public List<Specification> getAllSpecifications() {
        return specificationRepository.findAll();
    }

    public Specification getSpecificationById(Long id) {
        return specificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Specification not found with id: " + id));
    }

    public Specification createSpecification(Specification specification) {
        return specificationRepository.save(specification);
    }

    public Specification updateSpecification(Long id, Specification updatedSpecification) {
        Specification existingSpecification = getSpecificationById(id);

        existingSpecification.setDescription(updatedSpecification.getDescription());
        existingSpecification.setQuantityPerParent(updatedSpecification.getQuantityPerParent());
        existingSpecification.setUnitMeasurement(updatedSpecification.getUnitMeasurement());

        Long parentId = updatedSpecification.getParent().getPositionid();
        Specification parentSpecification = specificationRepository.findById(parentId)
                .orElse(null);
        existingSpecification.setParent(parentSpecification);

        return specificationRepository.save(existingSpecification);
    }

    public void deleteSpecification(Long id) {

        specificationRepository.deleteById(id);
    }
}
