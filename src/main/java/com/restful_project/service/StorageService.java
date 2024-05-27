package com.restful_project.service;

import com.restful_project.entity.Storage;
import com.restful_project.entity.model.StorageStatistic;

import java.time.LocalDate;
import java.util.List;

public interface StorageService {
    List<Storage> getAllStorages();

    Storage getStorageById(Long id);

    Storage createStorage(Storage storage);

    Storage updateStorage(Long id, Storage updatedStorage);

    void deleteStorage(Long id);

    List<Storage> getAllStoragesSortedById();

    Integer getCountOfSpecificationInStorage(Long id);

    StringBuilder getDeliveriesByDate(LocalDate date);

    List<Storage> getStorages();

    List<StorageStatistic> getStorageHistoryBySpecificationId(Long specificationId, LocalDate fromDate, LocalDate toDate);

    List<Storage> getDeficitStorages();
}

