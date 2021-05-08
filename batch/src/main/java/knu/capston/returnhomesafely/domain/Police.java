package knu.capston.returnhomesafely.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.Entity;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Police {

    @Id
    @GeneratedValue
    private Long id;
}
