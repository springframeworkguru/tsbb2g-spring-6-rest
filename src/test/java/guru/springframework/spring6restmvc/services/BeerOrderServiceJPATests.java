package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.controller.NotFoundException;
import guru.springframework.spring6restmvc.mappers.BeerOrderMapper;
import guru.springframework.spring6restmvc.mappers.BeerOrderMapperImpl;
import guru.springframework.spring6restmvc.repositories.BeerOrderRepository;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeerOrderServiceJPATest {

    @Mock
    BeerOrderRepository beerOrderRepository;

    @Spy
    BeerOrderMapper beerOrderMapper = new BeerOrderMapperImpl();

    @Mock
    CustomerRepository customerRepository;

    @Mock
    BeerRepository beerRepository;

    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    BeerOrderServiceJPA beerOrderServiceJPA;

    @Test
    void deleteOrder() {
        //given
        //when
        when(beerOrderRepository.existsById(any())).thenReturn(true);

        beerOrderServiceJPA.deleteOrder(UUID.randomUUID());

        //then
        verify(beerOrderRepository).existsById(any());
        verify(beerOrderRepository).deleteById(any());
    }

    @Test
    void deleteOrderNotFound() {
        //given
        //when
        when(beerOrderRepository.existsById(any())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> {
            beerOrderServiceJPA.deleteOrder(UUID.randomUUID());
        });

        //then
        verify(beerOrderRepository).existsById(any());
        verifyNoMoreInteractions(beerOrderRepository);
    }

    @Test
    void updateOrder() {
    }

    @Test
    void createOrder() {
    }

    @Test
    void getById() {
    }

    @Test
    void listOrders() {
    }
}