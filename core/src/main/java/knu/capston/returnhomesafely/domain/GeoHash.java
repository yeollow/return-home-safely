package knu.capston.returnhomesafely.domain;

import java.util.ArrayList;
import java.util.List;
import knu.capston.returnhomesafely.GPS;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GeoHash {
    private List<precision> precisionList;
    private static final String base32 = "0123456789bcdefghjkmnpqrstuvwxyz";

    public String encodedGeoHashKey(GPS gps) {
        StringBuilder gpsBuilder = new StringBuilder();
        double[] latitudeRange = new double[]{-90, 90};
        double[] longitudeRange = new double[]{-180, 180};
        int latCount = 0;
        int longCount = 0;

        getPrecisionList();
        int latCountMax = this.precisionList.get(6).getLatBits();
        int longCountMax = this.precisionList.get(6).getLongBits();
        for (int i = 0; i < longCountMax; i++) {
            double latitudeAve = (latitudeRange[0] + latitudeRange[1]) / 2;
            double longitudeAve = (longitudeRange[0] + longitudeRange[1]) / 2;

            if (longCount < longCountMax) {
                if (gps.getLongitude() >= longitudeAve) {
                    gpsBuilder.append("1");
                    longitudeRange[0] = longitudeAve;
                } else {
                    gpsBuilder.append("0");
                    longitudeRange[1] = longitudeAve;
                }

                longCount++;
            }

            if (latCount < latCountMax) {
                if (gps.getLatitude() >= latitudeAve) {
                    gpsBuilder.append("1");
                    latitudeRange[0] = latitudeAve;
                } else {
                    gpsBuilder.append("0");
                    latitudeRange[1] = latitudeAve;
                }

                latCount++;
            }
        }

        StringBuilder encoding = new StringBuilder();
        for (int i = 0; i < gpsBuilder.length() / 5; i++) {
            encoding.append(base32Encoding(gpsBuilder.substring(i * 5, i * 5 + 5)));
        }

        return String.valueOf(encoding);
    }

    private char base32Encoding(String gpsBuilderSubString) {
        return base32.charAt(Integer.parseInt(gpsBuilderSubString, 2));
    }

    private void getPrecisionList() {
        this.precisionList = new ArrayList<>();
                                                                                  // bounding Box (km)
        this.precisionList.add(null);
        this.precisionList.add(new precision(2, 3));            // +-2500
        this.precisionList.add(new precision(5, 5));            // +-630
        this.precisionList.add(new precision(7, 8));            // +-78
        this.precisionList.add(new precision(10, 10));          // +=20
        this.precisionList.add(new precision(12, 13));          // +=2.4
        this.precisionList.add(new precision(15, 15));          // +=0.61
        this.precisionList.add(new precision(17, 18));          // +=0.076
        this.precisionList.add(new precision(20, 20));          // +=0.019
    }
}
