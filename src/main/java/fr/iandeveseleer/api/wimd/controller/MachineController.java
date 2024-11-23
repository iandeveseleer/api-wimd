package fr.iandeveseleer.api.wimd.controller;

import fr.iandeveseleer.api.wimd.model.Machine;
import fr.iandeveseleer.api.wimd.model.Manufacturer;
import fr.iandeveseleer.api.wimd.model.Type;
import fr.iandeveseleer.api.wimd.model.Venue;
import fr.iandeveseleer.api.wimd.repository.MachineRepository;
import fr.iandeveseleer.api.wimd.repository.ManufacturerRepository;
import fr.iandeveseleer.api.wimd.repository.TypeRepository;
import fr.iandeveseleer.api.wimd.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@Setter
public class MachineController {

    private final MachineRepository machineRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final TypeRepository typeRepository;
    private final VenueRepository venueRepository;


    @QueryMapping
    public List<Machine> getAllMachines() {
        return machineRepository.findAll();
    }

    @QueryMapping
    public Optional<Machine> getMachine(@Argument String id) {
        return machineRepository.findById(id);
    }

    // Field resolver for the 'machines' field of the Machine type
    @SchemaMapping(typeName = "Machine")
    public List<Venue> venues(Machine machine) {
        // Get the venueIds from the Machine object
        List<String> venueIds = machine.getVenueIds();

        // Fetch and return the corresponding Venues from the database
        return venueRepository.findAllById(venueIds);
    }

    // Field resolver for the 'machines' field of the Machine type
    @SchemaMapping(typeName = "Machine")
    public Optional<Manufacturer> manufacturer(Machine machine) {
        // Get the manufacturedId from the Machine object
        String manufacturerId = machine.getManufacturerId();

        // Fetch and return the corresponding Manufacturer from the database
        return manufacturerRepository.findById(manufacturerId);
    }

    // Field resolver for the 'type' field of the Machine type
    @SchemaMapping(typeName = "Machine")
    public Optional<Type> type(Machine machine) {
        // Get the typeId from the Machine object
        String typeId = machine.getTypeId();

        // Fetch and return the corresponding Type from the database
        return typeRepository.findById(typeId);
    }

    @MutationMapping
    public Machine createMachine(
            @Argument String name,
            @Argument String typeId,
            @Argument String manufacturerId,
            @Argument List<String> venueIds) {
        Machine machine = Machine.builder()
                .name(name)
                .typeId(typeId)
                .manufacturerId(manufacturerId)
                .venueIds(venueIds)
                .build();

        Machine savedMachine = machineRepository.save(machine);

        // Add machineId to matching type
        Optional<Type> optionalType = typeRepository.findById(savedMachine.getTypeId());
        if(optionalType.isPresent()){
            Type type = optionalType.get();
            type.getMachineIds().add(savedMachine.getId());
            typeRepository.save(type);
        }

        // Add machineId to matching manufacturer
        Optional<Manufacturer> optionalManufacturer = manufacturerRepository.findById(savedMachine.getManufacturerId());
        if(optionalManufacturer.isPresent()){
            Manufacturer manufacturer = optionalManufacturer.get();
            manufacturer.getMachineIds().add(savedMachine.getId());
            manufacturerRepository.save(manufacturer);
        }
        return savedMachine;
    }
}

