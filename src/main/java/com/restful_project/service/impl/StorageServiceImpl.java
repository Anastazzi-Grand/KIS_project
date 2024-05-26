package com.restful_project.service.impl;

import com.restful_project.entity.Specification;
import com.restful_project.entity.Storage;
import com.restful_project.entity.model.StorageStatistic;
import com.restful_project.repository.SpecificationRepository;
import com.restful_project.repository.StorageRepository;
import com.restful_project.service.SpecificationService;
import com.restful_project.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private SpecificationRepository specificationRepository;

    @Autowired
    private SpecificationServiceImpl specificationService;

    @Override
    public List<Storage> getAllStorages() {
        return storageRepository.findAll();
    }

    @Override
    public Storage getStorageById(Long id) {
        return storageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ячейка нет с id: " + id));
    }

    @Override
    public Storage createStorage(Storage storage) {
        Specification specification = storage.getSpecificationId();
        if (specification != null) {
            Long specificationId = specification.getPositionid();
            if (specificationId != null) {
                Specification existingSpecification = specificationService.getSpecificationWithParentById(specificationId);
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
        existingStorage.setMeasureUnit(updatedStorage.getMeasureUnit());
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
                .orElseThrow(() -> new IllegalArgumentException("Заказ с id " + id + " не найден"));
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
                } else {
                    throw new RuntimeException("На складе нет товара с таким id");
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
        List<Specification> allSpecifications = specificationService.getAllSpecificationsSortedById();

        LocalDate date = LocalDate.now();

        for (Specification specification : allSpecifications) {
            Long specificationId = specification.getPositionid();
            Integer totalQuantity = getCountOfSpecificationInStorage(specificationId);

            if (totalQuantity != 0) {
                Storage storage = new Storage(null, date, totalQuantity, specification.getUnitMeasurement(), "Текущее количество", specification);

                storages.add(storage);
            }
        }

        storages.sort(Comparator.comparing(s -> s.getSpecificationId().getPositionid()));

        return storages;
    }

    public List<StorageStatistic> getStorageHistoryBySpecificationId(Long specificationId, LocalDate fromDate, LocalDate toDate) {
        Specification specification = specificationService.getSpecificationById(specificationId);
        List<Storage> storageHistory = findAllBySpecificationId(specification);

        storageHistory.sort(Comparator.comparing(Storage::getDate));

        List<StorageStatistic> dailyStorage = new ArrayList<>();

        Map<LocalDate, Integer> dailyIncoming = new HashMap<>();
        Map<LocalDate, Integer> dailyOutgoing = new HashMap<>();
        Map<LocalDate, Integer> dailyBalance = new HashMap<>();

        int currentRest = 0;

        for (Storage storage : storageHistory) {
            LocalDate date = storage.getDate();
            String operation = storage.getTypeOfOperation();
            int quantity = storage.getQuantity();

            if (operation.equalsIgnoreCase("приход")) {
                dailyIncoming.merge(date, quantity, Integer::sum);
                currentRest += quantity;
            } else if (operation.equalsIgnoreCase("уход")) {
                dailyOutgoing.merge(date, quantity, Integer::sum);
                currentRest -= quantity;
            }
            dailyBalance.put(date, currentRest);
        }

        Set<LocalDate> allDates = new HashSet<>(dailyBalance.keySet());

        for (LocalDate date : allDates) {
            int incoming = dailyIncoming.getOrDefault(date, 0);
            int outgoing = dailyOutgoing.getOrDefault(date, 0);
            int balance = dailyBalance.get(date);

            dailyStorage.add(new StorageStatistic(date, incoming, outgoing, balance));

        }

        return dailyStorage.stream()
                .filter(storage -> {
                    LocalDate date = storage.getDate();
                    boolean filterByFromDate = fromDate == null || !date.isBefore(fromDate);
                    boolean filterByToDate = toDate == null || !date.isAfter(toDate);
                    return (fromDate == null || filterByFromDate) && (toDate == null || filterByToDate);
                }).sorted(Comparator.comparing(StorageStatistic::getDate)).toList();
    }

    private List<Storage> findAllBySpecificationId(Specification specification) {
        return storageRepository.findAll().stream()
                .filter(storage -> storage.getSpecificationId().equals(specification))
                .collect(Collectors.toList());
    }
}
