package com.restful_project.service.impl;

import com.restful_project.entity.Order;
import com.restful_project.entity.Specification;
import com.restful_project.entity.Storage;
import com.restful_project.repository.SpecificationRepository;
import com.restful_project.repository.StorageRepository;
import com.restful_project.service.StorageService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.Optional;

@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private SpecificationRepository specificationRepository;

    @Override
    public List<Storage> getAllStorages() {
        System.out.println("GET ALL STORAGE");
        return storageRepository.findAll();
    }

    @Override
    public Storage getStorageById(Long id) {
        return storageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @Override
    public Storage createStorage(Storage storage) {
        Specification specification = storage.getSpecificationId();
        if (specification != null) {
            Long specificationId = specification.getPositionid();
            if (specificationId != null) {
                Specification existingSpecification = specificationRepository.findById(specificationId)
                        .orElseThrow(() -> new IllegalArgumentException("Спецификация с ID " + specificationId + " не найдена"));
                storage.setIdPosition(existingSpecification);
            }
        }

        // Сохраняем хранилище
        return storageRepository.save(storage);
    }

    @Override
    public Storage updateStorage(Long id, Storage updatedStorage) {
        Storage existingStorage = getStorageById(id);

        // Обновляем все поля
        existingStorage.setDate(updatedStorage.getDate());
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
        return storageRepository.findAll(Sort.by(Sort.Direction.ASC, "idStorage"));
    }

    @Override
    public StringBuilder getCountOfSpecificationInStorage(Long specificationId) {
        List<Storage> allItems = storageRepository.findAll();

        int totalQuantity = 0;
        boolean found = false;

        for (Storage item : allItems) {
            if (item.getSpecificationId().getPositionid() == specificationId) {
                totalQuantity += item.getQuantity();
                found = true;
            }
        }

        StringBuilder result = new StringBuilder();

        if (!found) {
            result.append("Товара с id ").append(specificationId).append(" нет на складе.");
        } else {
            String description = getDescriptionByPositionId(specificationId);
            result.append(description).append(": ").append(totalQuantity).append(" штук.");
        }

        return result;
    }

    @Override
    public StringBuilder getDeliveriesByDate(LocalDate date) {
        List<Storage> allItems = storageRepository.findAll();

        Map<String, Integer> deliveriesByItem = new HashMap<>();

        for (Storage item : allItems) {
            if (item.getDate().isEqual(date)) {
                String itemName = item.getSpecificationId().getDescription();
                int quantity = item.getQuantity();

                deliveriesByItem.put(itemName, deliveriesByItem.getOrDefault(itemName, 0) + quantity);
            }
        }

        StringBuilder result = new StringBuilder();

        if (deliveriesByItem.isEmpty()) {
            result.append("Поставок на складе за ").append(date).append(" не было.");
        } else {
            result.append("Поставленные товары на склад ").append(date).append(":\n");
            // Выводим информацию о каждой поставке
            for (Map.Entry<String, Integer> entry : deliveriesByItem.entrySet()) {
                result.append(entry.getKey()).append(": ").append(entry.getValue()).append(" шт.\n");
            }
        }

        return result;
    }

    private String getDescriptionByPositionId(Long positionId) {
        Specification specification = specificationRepository.findById(positionId).orElse(null);
        if (specification != null) {
            return specification.getDescription();
        }

        return "Неизвестный объект";
    }
}
