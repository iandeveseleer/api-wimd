package fr.iandeveseleer.api.wimd.repository;

import fr.iandeveseleer.api.wimd.model.VenueType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface VenueTypeRepository extends MongoRepository<VenueType, String> {

    @Query()
    Optional<VenueType> findVenueTypeById(String pVenueTypeId);
}
