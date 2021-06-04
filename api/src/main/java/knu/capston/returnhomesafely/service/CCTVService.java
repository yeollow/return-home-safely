package knu.capston.returnhomesafely.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import knu.capston.returnhomesafely.GPS;
import knu.capston.returnhomesafely.domain.CCTV;
import knu.capston.returnhomesafely.domain.GeoHash;
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
            gpsMap.put(cctv.getId(), new GPS(cctv.getLongitude(), cctv.getLatitude()));
        }
        return gpsMap.values();
    }

    public Collection<GPS> getCCTVGeoHash(GPS requestedGps) {
        GeoHash geoHash = new GeoHash();
        List<GPS> gpsList = cctvRepository.selectCCTVGpsAll().stream()
            .map(cctv -> new GPS(cctv.getLongitude(), cctv.getLatitude()))
            .collect(Collectors.toList());

        Map<String, Collection<GPS>> geoHashMap = new HashMap<>();
        for (GPS gps : gpsList) {
            geoHashMap
                .computeIfAbsent(geoHash.encodedGeoHashKey(gps), value -> new ArrayList<>())
                .add(gps);
        }

//        requestedGps의 위치가 hashMap에 존재하지 않는경우 예외처리 필요해보임..
        return geoHashMap
            .get(geoHash.encodedGeoHashKey(requestedGps));
    }
}
