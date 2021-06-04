package knu.capston.returnhomesafely.controller;

import java.util.Collection;
import knu.capston.returnhomesafely.GPS;
import knu.capston.returnhomesafely.service.CCTVService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CCTVController {

    private final CCTVService cctvService;

    @GetMapping("/api/v1/cctv")
    public Collection<GPS> getCCTVGps() {
        return cctvService.selectCCTVGpsAll();
    }

    @GetMapping("/api/v1/cctv/reqParam")
    public Collection<GPS> getCCTVGeoHash(@RequestParam double longitude,
        @RequestParam double latitude) {
        return cctvService.getCCTVGeoHash(new GPS(longitude, latitude));
    }

    @PostMapping("/api/v1/cctv")
    public Collection<GPS> getCCTVGeoHashPostMapping(@RequestBody GPS gps) {
        return cctvService.getCCTVGeoHash(gps);
    }
}
