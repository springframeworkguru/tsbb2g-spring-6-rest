package guru.springframework.spring6restmvc.listeners;

import guru.springframework.spring6restmvc.entities.BeerOrderLine;
import guru.springframework.spring6restmvc.events.DrinkPreparedEvent;
import guru.springframework.spring6restmvc.model.BeerOrderLineDTO;
import guru.springframework.spring6restmvc.repositories.BeerOrderLineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by jt, Spring Framework Guru.
 */
@ExtendWith(MockitoExtension.class)
public class DrinkPreparedListenerTest {

    @Mock
    BeerOrderLineRepository beerOrderLineRepository;

    @InjectMocks
    DrinkPreparedListener drinkPreparedListener;

    @Test
    void testListenEvent() {
        //given
        DrinkPreparedEvent drinkPreparedEvent = DrinkPreparedEvent.builder()
                .beerOrderLine(BeerOrderLineDTO.builder().id(UUID.randomUUID()).build())
                .build();

        BeerOrderLine beerOrderLine = BeerOrderLine.builder().id(drinkPreparedEvent.getBeerOrderLine().getId()).build();

        //when
        when(beerOrderLineRepository.findById(any())).thenReturn(java.util.Optional.of(beerOrderLine));

        drinkPreparedListener.listen(drinkPreparedEvent);

        //then
        verify(beerOrderLineRepository).findById(any());
        verify(beerOrderLineRepository).saveAndFlush(any());

    }


}
