package knu.capston.returnhomesafely.controller;


import java.util.Collection;
import knu.capston.returnhomesafely.GPS;
import knu.capston.returnhomesafely.service.CCTVService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController     //for response json data
@RequiredArgsConstructor
public class CCTVController {
    private final CCTVService cctvService;

    @GetMapping("/api/v1/cctv")
    public Collection<GPS> getCCTVGps() {
        return cctvService.selectCCTVGpsAll();
    }
}
