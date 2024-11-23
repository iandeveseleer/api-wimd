package fr.iandeveseleer.api.wimd.controller;

import fr.iandeveseleer.api.wimd.model.Manufacturer;
import fr.iandeveseleer.api.wimd.repository.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@Setter
public class ManufacturerController {

    private final ManufacturerRepository manufacturerRepository;

    @QueryMapping
    public List<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.findAll();
    }

    @QueryMapping
    public Optional<Manufacturer> getManufacturer(@Argument String id) {
        return manufacturerRepository.findById(id);
    }

    @MutationMapping
    public Manufacturer createManufacturer(
            @Argument String name,
            @Argument List<String> machineIds) {
        Manufacturer manufacturer = Manufacturer.builder()
                .name(name)
                .machineIds(machineIds)
                .build();
        return manufacturerRepository.save(manufacturer);
    }
}

