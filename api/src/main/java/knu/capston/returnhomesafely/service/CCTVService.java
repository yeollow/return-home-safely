package knu.capston.returnhomesafely.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import knu.capston.returnhomesafely.GPS;
import knu.capston.returnhomesafely.domain.CCTV;
import knu.capston.returnhomesafely.repository.CCTVRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CCTVService {
    private final CCTVRepository cctvRepository;

    public Collection<GPS> selectCCTVGpsAll() {
        Map<Long, GPS> gpsMap = new HashMap<>();
        for (CCTV cctv : cctvRepository.selectCCTVGpsAll()) {
            gpsMap.put(cctv.getId(), new GPS(cctv.getLatitude(), cctv.getLongitude()));
        }
        return gpsMap.values();
    }
}
