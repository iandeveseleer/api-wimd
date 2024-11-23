package fr.iandeveseleer.api.wimd.repository;

import fr.iandeveseleer.api.wimd.model.Venue;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface VenueRepository extends MongoRepository<Venue, String> {

    @Query()
    Optional<Venue> findVenueById(String pPlaceId);
}
