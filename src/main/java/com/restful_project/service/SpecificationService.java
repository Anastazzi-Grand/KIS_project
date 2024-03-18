package com.restful_project.service;

import com.restful_project.entity.Specification;

import java.util.List;

public interface SpecificationService {


     List<Specification> getAllSpecifications();

     Specification getSpecificationById(Long id);

     Specification createSpecification(Specification specification);

     Specification updateSpecification(Long id, Specification updatedSpecification);

     void deleteSpecification(Long id);
}
