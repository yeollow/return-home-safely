package knu.capston.returnhomesafely;

import knu.capston.returnhomesafely.time.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
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
