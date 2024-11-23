package fr.iandeveseleer.api.wimd.repository;

import fr.iandeveseleer.api.wimd.model.Machine;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface MachineRepository extends MongoRepository<Machine, String> {

    @Query()
    Optional<Machine> findMachineById(String pMachineId);
}
