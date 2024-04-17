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
        return specificationRepository.save(specification);
    }

    public Specification updateSpecification(Long id, Specification existingSpecification) {
        Specification updatedSpecification = getSpecificationById(id);

        updatedSpecification.setDescription(existingSpecification.getDescription());
        updatedSpecification.setQuantityPerParent(existingSpecification.getQuantityPerParent());
        updatedSpecification.setUnitMeasurement(existingSpecification.getUnitMeasurement());

        Long parentId = existingSpecification.getParent().getPositionid();
        Specification parentSpecification = specificationRepository.findById(parentId)
                .orElse(null);
        updatedSpecification.setParent(parentSpecification);

        return specificationRepository.save(updatedSpecification);
    }

    public void deleteSpecification(Long id, boolean cascade) {
        Specification specification = getSpecificationById(id);

        if (cascade) {
            deleteChildrenRecursively(specification);
        }

        specificationRepository.delete(specification);
    }

    private void deleteChildrenRecursively(Specification parent) {
        List<Specification> childSpecifications = getChildSpecifications(parent.getPositionid());
        for (Specification child : childSpecifications) {
            deleteChildrenRecursively(child);
            specificationRepository.delete(child);
        }
    }

    private List<Specification> getChildSpecifications(Long parentId) {
        List<Specification> allSpecifications = specificationRepository.findAll();
        List<Specification> childSpecifications = new ArrayList<>();

        for (Specification spec : allSpecifications) {
            if (spec.getParent() != null && spec.getParent().getPositionid().equals(parentId)) {
                childSpecifications.add(spec);
            }
        }

        return childSpecifications;
    }
}
