package fr.iandeveseleer.api.wimd.repository;

import fr.iandeveseleer.api.wimd.model.Machine;
import fr.iandeveseleer.api.wimd.model.Type;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface TypeRepository extends MongoRepository<Type, String> {

    @Query()
    Optional<Machine> findTypeById(String pTypeId);
}
