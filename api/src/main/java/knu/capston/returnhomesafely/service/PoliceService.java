package knu.capston.returnhomesafely.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import knu.capston.returnhomesafely.GPS;
import knu.capston.returnhomesafely.domain.Police;
import knu.capston.returnhomesafely.repository.PoliceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PoliceService {

    private final PoliceRepository policeRepository;
    public Collection<GPS> selectPoliceGpsAll() {
        Map<Long, GPS> gpsMap = new HashMap<>();
        for (Police police : policeRepository.selectPoliceGpsAll()) {
            gpsMap
                .put(police.getId(), new GPS(police.getLatitude(), police.getLongitude()));
        }

        return gpsMap.values();
    }
}
