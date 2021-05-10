package knu.capston.returnhomesafely.processor;


import knu.capston.returnhomesafely.domain.Police;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class PoliceItemProcessor  implements ItemProcessor<Police,Police> {

    @Override
    public Police process(@NotNull Police police){
        return police;
    }
}
