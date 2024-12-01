package fr.iandeveseleer.api.wimd.controller;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping(path = "/osm")
@RequiredArgsConstructor
@Setter
public class MapBoxController {

    /**
     * Access token for Mapbox API
     */
    @Value("${application.mapbox.access.token:access-token}")
    private String mapboxAccessToken;

    /**
     * Username for Mapbox API
     */
    @Value("${application.mapbox.username:username}")
    private String mapboxUsername;

    /**
     * Identifier of the style to use
     */
    @Value("${application.mapbox.style.id:style-id}")
    private String mapboxStyleId;

    private final RestTemplate restTemplate;

    @GetMapping("/tiles")
    public ResponseEntity<byte[]> getMapboxTile(
            @RequestParam("x") int x,
            @RequestParam("y") int y,
            @RequestParam("z") int z) {

        String mapboxUrl = String.format(
                "https://api.mapbox.com/styles/v1/%s/%s/tiles/256/%d/%d/%d?access_token=%s",
                mapboxUsername, mapboxStyleId, z, x, y, mapboxAccessToken);

        try {
            byte[] tile = restTemplate.getForObject(mapboxUrl, byte[].class);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/png")
                    .body(tile);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
