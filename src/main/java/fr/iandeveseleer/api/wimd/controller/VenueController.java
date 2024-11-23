package fr.iandeveseleer.api.wimd.controller;

import fr.iandeveseleer.api.wimd.model.Machine;
import fr.iandeveseleer.api.wimd.model.Venue;
import fr.iandeveseleer.api.wimd.model.VenueType;
import fr.iandeveseleer.api.wimd.repository.MachineRepository;
import fr.iandeveseleer.api.wimd.repository.VenueRepository;
import fr.iandeveseleer.api.wimd.repository.VenueTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@Setter
public class VenueController {

    private final VenueRepository venueRepository;
    private final VenueTypeRepository venueTypeRepository;
    private final MachineRepository machineRepository;

    @QueryMapping
    public List<Venue> getAllVenues() {
        return venueRepository.findAll();
    }

    @QueryMapping
    public Optional<Venue> getVenue(@Argument String id) {
        return venueRepository.findById(id);
    }

    // Field resolver for the 'machines' field of the Venue type
    @SchemaMapping(typeName = "Venue")
    public List<Machine> machines(Venue venue) {
        // Get the machineIds from the Venue object
        List<String> machineIds = venue.getMachineIds();

        // Fetch and return the corresponding machines from the database
        return machineRepository.findAllById(machineIds);
    }

    // Field resolver for the 'venueType' field of the Venue type
    @SchemaMapping(typeName = "Venue")
    public Optional<VenueType> venueType(Venue venue) {
        // Get the venueTypeId from the Venue object
        String venueTypeId = venue.getVenueTypeId();
        if(StringUtils.isNotEmpty(venueTypeId)) {
            // Fetch and return the corresponding VenueType from the database
            return venueTypeRepository.findById(venueTypeId);
        }
        return Optional.empty();
    }

    @MutationMapping
    public Venue createVenue(
            @Argument String name,
            @Argument String venueTypeId,
            @Argument String city,
            @Argument String country,
            @Argument String street,
            @Argument String streetNumber,
            @Argument String zipCode,
            @Argument List<String> machineIds) {
        Venue venue = new Venue();
        venue.setName(name);
        venue.setVenueTypeId(venueTypeId);
        venue.setCity(city);
        venue.setCountry(country);
        venue.setStreet(street);
        venue.setStreetNumber(streetNumber);
        venue.setZipCode(zipCode);
        venue.setMachineIds(machineIds);
        return venueRepository.save(venue);
    }

    @MutationMapping
    @Transactional
    public Venue addMachineToVenue(
            @Argument String venueId,
            @Argument String machineId) {
        // Fetch the Venue by ID
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new IllegalArgumentException("Venue not found with id: " + venueId));

        // Fetch the Machine by ID
        Machine machine = machineRepository.findById(machineId)
                .orElseThrow(() -> new IllegalArgumentException("Machine not found with id: " + machineId));

        // Add the Machine ID to the Venue's machineIds list if not already present
        if (!venue.getMachineIds().contains(machineId)) {
            venue.getMachineIds().add(machineId);
            venueRepository.save(venue); // Save updated Venue
        }

        // Add the Venue ID to the Machine's venueIds list if not already present
        if (!machine.getVenueIds().contains(venueId)) {
            machine.getVenueIds().add(venueId);
            machineRepository.save(machine); // Save updated Machine
        }

        return venue; // Return the updated Venue
    }

    @MutationMapping
    @Transactional
    public Venue removeMachineFromVenue(
            @Argument String venueId,
            @Argument String machineId) {
        // Fetch the Venue by ID
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new IllegalArgumentException("Venue not found with id: " + venueId));

        // Fetch the Machine by ID
        Machine machine = machineRepository.findById(machineId)
                .orElseThrow(() -> new IllegalArgumentException("Machine not found with id: " + machineId));

        // Remove the Machine ID from the Venue's machineIds list if present
        if (venue.getMachineIds().contains(machineId)) {
            venue.getMachineIds().remove(machineId);
            venueRepository.save(venue); // Save updated Venue
        }

        // Remove the Venue ID from the Machine's venueIds list if present
        if (machine.getVenueIds().contains(venueId)) {
            machine.getVenueIds().remove(venueId);
            machineRepository.save(machine); // Save updated Machine
        }

        return venue; // Return the updated Venue
    }

    @QueryMapping
    public List<VenueType> getAllVenueTypes() {
        return venueTypeRepository.findAll();
    }

    @QueryMapping
    public Optional<VenueType> getVenueType(@Argument String id) {
        return venueTypeRepository.findById(id);
    }

    // Field resolver for the 'venues' field of the VenueType object
    @SchemaMapping(typeName = "VenueType")
    public List<Venue> venues(VenueType venueType) {
        // Get the venueIds from the VenueType object
        List<String> venueIds = venueType.getVenueIds();

        // Fetch and return the corresponding machines from the database
        return venueRepository.findAllById(venueIds);
    }

    @MutationMapping
    public VenueType createVenueType(
            @Argument String name,
            @Argument List<String> venueIds) {
        VenueType venueType = VenueType.builder()
                .name(name)
                .venueIds(venueIds)
                .build();
        return venueTypeRepository.save(venueType);
    }
}
