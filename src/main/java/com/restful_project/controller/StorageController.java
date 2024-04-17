package com.restful_project.controller;

import com.restful_project.entity.Storage;
import com.restful_project.service.StorageService;
import com.restful_project.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/storages")
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
