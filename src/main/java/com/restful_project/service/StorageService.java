package com.restful_project.service;

import com.restful_project.entity.Storage;

import java.time.LocalDate;
import java.util.List;

public interface StorageService {
    List<Storage> getAllStorages();

    Storage getStorageById(Long id);

    Storage createStorage(Storage storage);

    Storage updateStorage(Long id, Storage updatedStorage);

    void deleteStorage(Long id);

    List<Storage> getAllStoragesSortedById();

    StringBuilder getCountOfSpecificationInStorage(Long id);

    StringBuilder getDeliveriesByDate(LocalDate date);
}

