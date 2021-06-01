package knu.capston.returnhomesafely.controller;


import java.util.Collection;
import knu.capston.returnhomesafely.GPS;
import knu.capston.returnhomesafely.service.PoliceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController     //for response json data
@RequiredArgsConstructor
public class PoliceController {

    private final PoliceService policeService;

    @GetMapping("/api/v1/police")
    public Collection<GPS> getPoliceGps() {
        return policeService.selectPoliceGpsAll();
    }
}
