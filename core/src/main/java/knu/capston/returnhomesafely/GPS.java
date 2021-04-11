package knu.capston.returnhomesafely;

import knu.capston.returnhomesafely.time.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GPS extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Double longitude;
    private Double latitude;
}
