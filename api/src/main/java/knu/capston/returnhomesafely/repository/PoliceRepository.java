package knu.capston.returnhomesafely.repository;

import java.util.List;
import javax.persistence.EntityManager;
import knu.capston.returnhomesafely.domain.Police;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PoliceRepository {

    private final EntityManager entityManager;

    public List<Police> selectPoliceGpsAll() {
        String jpql = "select gps from Police gps";

        return entityManager.createQuery(jpql, Police.class).getResultList();

    }
}
