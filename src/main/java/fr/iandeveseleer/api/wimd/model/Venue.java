package fr.iandeveseleer.api.wimd.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Document("venues")
public class Venue {

    @Id
    private String id;

    private String name;

    private String venueTypeId;

    private String street;

    @Field(name = "street_number")
    private String streetNumber;

    @Field(name = "zip_code")
    private String zipCode;

    private String city;

    private String country;

    private List<String> machineIds = new ArrayList<>();

    @Transient
    private List<Machine> machines;

    @Transient
    private VenueType venueType;
}
