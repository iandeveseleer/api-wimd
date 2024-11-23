package fr.iandeveseleer.api.wimd.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Document("manufacturers")
public class Manufacturer {

    @Id
    private String id;

    private String name;

    private List<String> machineIds = new ArrayList<>();

    @Transient
    private List<Machine> machines;
}
