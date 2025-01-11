package guru.springframework.spring6restmvc.listeners;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.BeerAudit;
import guru.springframework.spring6restmvc.events.BeerCreatedEvent;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.mappers.BeerMapperImpl;
import guru.springframework.spring6restmvc.repositories.BeerAuditRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by jt, Spring Framework Guru.
 */
@ExtendWith(MockitoExtension.class)
public class BeerCreatedListenerTest {

    @Spy
    BeerMapper mapper = new BeerMapperImpl();

    @Mock
    BeerAuditRepository beerAuditRepository;

    @InjectMocks
    BeerCreatedListener beerCreatedListener;

    @Test
    void testListen() {
        //given
        BeerCreatedEvent event = BeerCreatedEvent.builder()
                .beer(Beer.builder()
                        .id(UUID.randomUUID())
                        .build())
                .build();

        //when
        when(beerAuditRepository.save(any())).thenReturn(BeerAudit.builder().id(event.getBeer().getId()).build());

        beerCreatedListener.listen(event);

        //then
        verify(beerAuditRepository).save(any());
        verify(mapper).beerToBeerAudit(any());

    }
}










