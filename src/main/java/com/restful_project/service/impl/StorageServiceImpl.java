package com.restful_project.service.impl;

import com.restful_project.entity.Order;
import com.restful_project.entity.Specification;
import com.restful_project.entity.Storage;
import com.restful_project.repository.SpecificationRepository;
import com.restful_project.repository.StorageRepository;
import com.restful_project.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private SpecificationRepository specificationRepository;

    @Override
    public List<Storage> getAllStorages() {
        return storageRepository.findAll();
    }

    @Override
    public Storage getStorageById(Long id) {
        return storageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @Override
    public Storage createStorage(Storage storage) {
        return storageRepository.save(storage);
    }

    @Override
    public Storage updateStorage(Long id, Storage updatedStorage) {
        Storage existingStorage = getStorageById(id);

        // Обновляем все поля
        existingStorage.setDateAndTime(updatedStorage.getDateAndTime());
        existingStorage.setIdPosition(updatedStorage.getSpecificationId());
        existingStorage.setQuantity(updatedStorage.getQuantity());
        existingStorage.setTypeOfOperation(updatedStorage.getTypeOfOperation());

        Specification specification = existingStorage.getSpecificationId();
        if (specification != null) {
            Long specificationId = specification.getPositionid();
            if (specificationId != null) {
                Specification updatedSpecification = specificationRepository.findById(specificationId)
                        .orElseThrow(() -> new IllegalArgumentException("Спецификация с ID " + specificationId + " не найдена"));
                existingStorage.setIdPosition(updatedSpecification);
            } else {
                existingStorage.setIdPosition(null);
            }
        } else {
            existingStorage.setIdPosition(null);
        }

        // Сохраняем обновленную запись
        return storageRepository.save(existingStorage);
    }

    @Override
    public void deleteStorage(Long id) {
        Storage storage = storageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Заказ с id " + id + " не найден"));
        storageRepository.delete(storage);
    }

    @Override
    public List<Storage> getAllStoragesSortedById() {
        return null;
    }

    @Override
    public List<Storage> getAllStoragesSortedByName() {
        return null;
    }
}