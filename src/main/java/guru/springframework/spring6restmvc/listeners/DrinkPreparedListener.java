package guru.springframework.spring6restmvc.listeners;

import guru.springframework.spring6restmvc.config.KafkaConfig;
import guru.springframework.spring6restmvc.events.DrinkPreparedEvent;
import guru.springframework.spring6restmvc.model.BeerOrderLineStatus;
import guru.springframework.spring6restmvc.repositories.BeerOrderLineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jt, Spring Framework Guru.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DrinkPreparedListener {

    private final BeerOrderLineRepository beerOrderLineRepository;

    @Transactional
    @KafkaListener(groupId = "DrinkPreparedListener", topics = KafkaConfig.DRINK_PREPARED_TOPIC)
    public void listen(DrinkPreparedEvent event) {

        log.debug("Drink Prepared Event Received");

        beerOrderLineRepository.findById(event.getBeerOrderLine().getId()).ifPresentOrElse(beerOrderLine -> {

            beerOrderLine.setOrderLineStatus(BeerOrderLineStatus.COMPLETE);

            beerOrderLineRepository.saveAndFlush(beerOrderLine);
        }, () -> log.error("Beer Order Line Not Found!"));
    }
}
