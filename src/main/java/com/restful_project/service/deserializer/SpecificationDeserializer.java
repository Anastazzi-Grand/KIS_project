package com.restful_project.service.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.restful_project.entity.Specification;
import com.restful_project.repository.SpecificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SpecificationDeserializer extends JsonDeserializer<Specification> {
    @Autowired
    private SpecificationRepository specificationRepository;

    @Override
    public Specification deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Long parentId = p.getLongValue(); // Получаем значение айди родителя из JSON
        return specificationRepository.findById(parentId).orElseThrow(() -> new IllegalArgumentException("Спецификация с ID " + parentId + " не найдена"));
    }
}
