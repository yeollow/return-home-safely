package knu.capston.returnhomesafely.processor;

import knu.capston.returnhomesafely.domain.CCTV;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class CCTVItemProcessor implements ItemProcessor<CCTV, CCTV> {

    @Override
    public CCTV process(@NotNull CCTV cctv) {
        return cctv;
    }
}
