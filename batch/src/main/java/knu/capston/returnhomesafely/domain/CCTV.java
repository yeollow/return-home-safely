package knu.capston.returnhomesafely.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CCTV {

    @Id
    @GeneratedValue
    private Long id;
    private String location;
    private String number;
    private Double latitude;
    private Double longitude;
    private String dataDate;

    private String managementAgency;
    private String loadLocation;
    private String purpose;
    private int numOfCamera;
    private int pixels;
    private String direction;
    private int storageDays;
    private String installationDate;

    @Builder
    public CCTV(Double latitude, Double longitude, String location, String number,
        String dataDate) {
        this.location = location;
        this.number = number;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dataDate = dataDate;
    }
}
