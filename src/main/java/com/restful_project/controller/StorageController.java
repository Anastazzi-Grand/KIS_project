package com.restful_project.controller;

import com.restful_project.entity.Storage;
import com.restful_project.service.StorageService;
import com.restful_project.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/storages")
@CrossOrigin(origins = "http://localhost:4200")
public class StorageController {
    @Autowired
    private StorageService storageService;

    @GetMapping
    public List<Storage> getAllStorages() {
        return storageService.getAllStorages();
    }

    @GetMapping("/sortById")
    public List<Storage> getAllStoragesSortedById() {
        return storageService.getAllStoragesSortedById();
    }

    @GetMapping("/getCount/{id}")
    public Integer getCountOfSpecificationInStorage(@PathVariable("id") Long id) {
        return storageService.getCountOfSpecificationInStorage(id);
    }

    @GetMapping("/getStorages")
    public List<Storage> getStorages() {
        return storageService.getStorages();
    }

    @GetMapping("/deliveries")
    public StringBuilder getDeliveriesByDate(@RequestParam("date") LocalDate date) {
        return storageService.getDeliveriesByDate(date);
    }

    @GetMapping("/{id}")
    public Storage getStorageById(@PathVariable("id") Long id) {
        return storageService.getStorageById(id);
    }

    @PostMapping
    public Storage createStorage(@RequestBody Storage storage) {
        return storageService.createStorage(storage);
    }

    @PutMapping("/{id}")
    public Storage updateStorage(@PathVariable("id") Long id, @RequestBody Storage storage) {
        return storageService.updateStorage(id, storage);
    }

    @DeleteMapping("/{id}")
    public void deleteStorage(@PathVariable("id") Long id) {
        storageService.deleteStorage(id);
    }
}
