package knu.capston.returnhomesafely.time;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Data
public class BaseTimeEntity {

    @CreatedDate
    private LocalDateTime createDate;

    @CreatedDate
    private LocalDateTime modifiedDate;
}
