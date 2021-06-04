package knu.capston.returnhomesafely.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import knu.capston.returnhomesafely.GPS;
import knu.capston.returnhomesafely.domain.GeoHash;
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
                .put(police.getId(), new GPS(police.getLongitude(), police.getLatitude()));
        }

        return gpsMap.values();
    }

    public Collection<GPS> getCCTVGeoHash(GPS requestedGps) {
        GeoHash geoHash = new GeoHash();
        List<GPS> gpsList = policeRepository.selectPoliceGpsAll().stream()
            .map(cctv -> new GPS(cctv.getLongitude(), cctv.getLatitude()))
            .collect(Collectors.toList());

        Map<String, Collection<GPS>> geoHashMap = new HashMap<>();      //duplicated declaration..
        for (GPS gps : gpsList) {
            geoHashMap.computeIfAbsent(geoHash.encodedGeoHashKey(gps), value -> new ArrayList<>())
                .add(gps);
        }

        return geoHashMap.get(geoHash.encodedGeoHashKey(requestedGps));     //can be null..
    }
}
