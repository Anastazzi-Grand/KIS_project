package com.restful_project.service.impl;

import com.restful_project.entity.Specification;
import com.restful_project.entity.Storage;
import com.restful_project.repository.SpecificationRepository;
import com.restful_project.repository.StorageRepository;
import com.restful_project.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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
        Specification specification = storage.getSpecificationId();
        if (specification != null) {
            Long specificationId = specification.getPositionid();
            if (specificationId != null) {
                Specification existingSpecification = specificationRepository.findById(specificationId)
                        .orElseThrow(() -> new IllegalArgumentException("Спецификация с ID " + specificationId + " не найдена"));
                storage.setSpecificationId(existingSpecification);
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
        existingStorage.setSpecificationId(updatedStorage.getSpecificationId());
        existingStorage.setQuantity(updatedStorage.getQuantity());
        existingStorage.setTypeOfOperation(updatedStorage.getTypeOfOperation());

        Specification specification = existingStorage.getSpecificationId();
        if (specification != null) {
            Long specificationId = specification.getPositionid();
            if (specificationId != null) {
                Specification updatedSpecification = specificationRepository.findById(specificationId)
                        .orElseThrow(() -> new IllegalArgumentException("Спецификация с ID " + specificationId + " не найдена"));
                existingStorage.setSpecificationId(updatedSpecification);
            } else {
                existingStorage.setSpecificationId(null);
            }
        } else {
            existingStorage.setSpecificationId(null);
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
    public Integer getCountOfSpecificationInStorage(Long specificationId) {
        List<Storage> allItems = storageRepository.findAll();

        int totalQuantity = 0;

        for (Storage item : allItems) {
            if (item.getSpecificationId().getPositionid().equals(specificationId)) {
                if (Objects.equals(item.getTypeOfOperation().toLowerCase(), "приход")) {
                    totalQuantity += item.getQuantity();
                } else if (Objects.equals(item.getTypeOfOperation().toLowerCase(), "уход")) {
                    totalQuantity -= item.getQuantity();
                }
            }
        }

        return totalQuantity;
    }

    @Override
    public StringBuilder getDeliveriesByDate(LocalDate date) {
        List<Storage> allItems = storageRepository.findAll();

        Map<String, Integer> deliveriesByItem = new HashMap<>();

        for (Storage item : allItems) {
            if (!item.getDate().isAfter(date)) {
                String itemName = item.getSpecificationId().getDescription();
                int quantity = item.getQuantity();

                deliveriesByItem.put(itemName, deliveriesByItem.getOrDefault(itemName, 0) + quantity);
            }
        }

        StringBuilder result = new StringBuilder();

        if (deliveriesByItem.isEmpty()) {
            result.append("Товаров на складе ").append(date).append(" не было.");
        } else {
            result.append("Товар(ы) на складе ").append(date).append(":\n");
            // Выводим информацию о каждой поставке
            for (Map.Entry<String, Integer> entry : deliveriesByItem.entrySet()) {
                result.append(entry.getKey()).append(": ").append(entry.getValue()).append(" шт.\n");
            }
        }

        return result;
    }

    @Override
    public List<Storage> getStorages() {
        List<Storage> storages = new ArrayList<>();
        List<Specification> allSpecifications = specificationRepository.findAll();

        LocalDate date = LocalDate.now();

        for (Specification specification : allSpecifications) {
            Long specificationId = specification.getPositionid();
            Integer totalQuantity = getCountOfSpecificationInStorage(specificationId);

            if (totalQuantity > 0) {
                Storage storage = new Storage();
                storage.setIdStorage(null);
                storage.setDate(date);
                storage.setQuantity(totalQuantity);
                storage.setTypeOfOperation("Текущее количество");
                storage.setSpecificationId(specification);

                storages.add(storage);
            }
        }

        return storages;
    }
}
