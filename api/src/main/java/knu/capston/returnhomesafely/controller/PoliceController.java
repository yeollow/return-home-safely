package knu.capston.returnhomesafely.controller;


import java.util.Collection;
import knu.capston.returnhomesafely.GPS;
import knu.capston.returnhomesafely.service.PoliceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PoliceController {

    private final PoliceService policeService;

    @GetMapping("/api/v1/police")
    public Collection<GPS> getPoliceGps() {
        return policeService.selectPoliceGpsAll();
    }

    @GetMapping("/api/v1/police/reqParam")
    public Collection<GPS> getPoliceGeoHash(@RequestParam double longitude,
        @RequestParam double latitude) {
        return policeService.getCCTVGeoHash(new GPS(longitude, latitude));
    }

    @PostMapping("/api/v1/police")
    public Collection<GPS> getPoliceGeoHashPostMapping(@RequestBody GPS gps) {
        return policeService.getCCTVGeoHash(gps);
    }
}
