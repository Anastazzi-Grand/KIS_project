package com.restful_project.controller;

import com.restful_project.entity.Order;
import com.restful_project.entity.Specification;
import com.restful_project.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/specifications")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    @GetMapping
    public List<Specification> getAllSpecifications() {
        return specificationService.getAllSpecifications();
    }

    @GetMapping("/{id}")
    public Specification getSpecificationById(@PathVariable("id") Long id) {
        return specificationService.getSpecificationById(id);
    }

    @GetMapping("/sortById")
    public List<Specification> getAllSpecificationsSortedById() {
        return specificationService.getAllSpecificationsSortedById();
    }

    @GetMapping("/sortByName")
    public List<Specification> getAllSpecificationsSortedByName() {
        return specificationService.getAllSpecificationsSortedByName();
    }

    @PostMapping
    public Specification createSpecification(@RequestBody Specification specification) {
        return specificationService.createSpecification(specification);
    }

    @PutMapping("/{id}")
    public Specification updateSpecification(@PathVariable("id") Long id, @RequestBody Specification specification) {
        return specificationService.updateSpecification(id, specification);
    }

    @DeleteMapping("/{id}")
    public void deleteSpecification(@PathVariable("id") Long id) {
        specificationService.deleteSpecification(id);
    }
}
