package knu.capston.returnhomesafely.repository;

import java.util.List;
import javax.persistence.EntityManager;
import knu.capston.returnhomesafely.GPS;
import knu.capston.returnhomesafely.domain.CCTV;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CCTVRepository {

    private final EntityManager entityManager;

    public List<CCTV> selectCCTVGpsAll() {
        String jpql = "select gps from CCTV gps";

        return entityManager.createQuery(jpql, CCTV.class).getResultList();
    }
}
