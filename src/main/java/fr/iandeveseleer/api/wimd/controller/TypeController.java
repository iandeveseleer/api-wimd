package fr.iandeveseleer.api.wimd.controller;

import fr.iandeveseleer.api.wimd.model.Machine;
import fr.iandeveseleer.api.wimd.model.Type;
import fr.iandeveseleer.api.wimd.repository.MachineRepository;
import fr.iandeveseleer.api.wimd.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@Setter
public class TypeController {

    private final TypeRepository typeRepository;
    private final MachineRepository machineRepository;

    @QueryMapping
    public List<Type> getAllTypes() {
        return typeRepository.findAll();
    }

    @QueryMapping
    public Optional<Type> getType(@Argument String id) {
        return typeRepository.findById(id);
    }

    // Field resolver for the 'machine' field of the Type object
    @SchemaMapping(typeName = "Type")
    public List<Machine> machines(Type type) {
        // Get the machineIds from the Type object
        List<String> machineIds = type.getMachineIds();

        // Fetch and return the corresponding Machines from the database
        return machineRepository.findAllById(machineIds);
    }
}

