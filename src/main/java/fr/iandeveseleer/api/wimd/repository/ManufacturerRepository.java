package fr.iandeveseleer.api.wimd.repository;

import fr.iandeveseleer.api.wimd.model.Manufacturer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ManufacturerRepository extends MongoRepository<Manufacturer, String> {

    @Query()
    Optional<Manufacturer> findManufacturerById(String pManufacturerId);
}
