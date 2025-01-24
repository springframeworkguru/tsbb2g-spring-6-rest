package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.controller.NotFoundException;
import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.BeerOrder;
import guru.springframework.spring6restmvc.entities.BeerOrderLine;
import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.mappers.BeerOrderMapper;
import guru.springframework.spring6restmvc.mappers.BeerOrderMapperImpl;
import guru.springframework.spring6restmvc.model.*;
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
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeerOrderServiceJPATests {

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

        //given
        UUID beerOrderId = UUID.randomUUID();
        UUID beerOrderLineId = UUID.randomUUID();
        UUID beerId = UUID.randomUUID();
        Customer customer = Customer.builder().id(UUID.randomUUID()).build();

        Beer beer = Beer.builder().id(beerId).build();
        BeerOrderUpdateDTO beerOrderUpdateDTO = BeerOrderUpdateDTO.builder()
                .beerOrderLines(Set.of(BeerOrderLineUpdateDTO.builder()
                        .id(beerOrderLineId)
                        .beerId(beerId)
                        .build()))
                .build();

        BeerOrder beerOrder = BeerOrder.builder().id(beerOrderId)
                .beerOrderLines(Set.of(BeerOrderLine.builder().id(beerOrderLineId).beer(beer).build()))
                .build();

        //when
        when(beerOrderRepository.findById(any())).thenReturn(java.util.Optional.of(beerOrder));
        when(customerRepository.findById(any())).thenReturn(java.util.Optional.of(customer));
        when(beerRepository.findById(any())).thenReturn(java.util.Optional.of(beer));
        when(beerOrderRepository.save(any())).thenReturn(beerOrder);

        BeerOrderDTO updatedDto =  beerOrderServiceJPA.updateOrder(beerOrderId, beerOrderUpdateDTO);

        //then
        assertThat(updatedDto.getId()).isEqualTo(beerOrderId);

        verify(beerOrderRepository).findById(any());
        verify(customerRepository).findById(any());
        verify(beerOrderRepository).save(any());

    }
    @Test
    void updateOrderNotFound() {
        UUID beerOrderId = UUID.randomUUID();
        UUID beerOrderLineId = UUID.randomUUID();
        UUID beerId = UUID.randomUUID();
        Customer customer = Customer.builder().id(UUID.randomUUID()).build();

        Beer beer = Beer.builder().id(beerId).build();
        BeerOrderUpdateDTO beerOrderUpdateDTO = BeerOrderUpdateDTO.builder()
                .customerId(customer.getId())
                .beerOrderLines(Set.of(BeerOrderLineUpdateDTO.builder()
                        .id(beerOrderLineId)
                        .beerId(beerId)
                        .build()))
                .build();

        BeerOrder beerOrder = BeerOrder.builder().id(beerOrderId)
                .beerOrderLines(Set.of(BeerOrderLine.builder().id(beerOrderLineId).beer(beer).build()))
                .build();

        //when
        when(beerOrderRepository.findById(any())).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            beerOrderServiceJPA.updateOrder(beerOrderId, beerOrderUpdateDTO);
        });

        //then
        verify(beerOrderRepository).findById(any());
        verifyNoMoreInteractions(customerRepository, beerRepository, beerOrderRepository);
    }

    @Test
    void createOrder() {
        //given
        UUID beerOrderId = UUID.randomUUID();
        UUID beerId = UUID.randomUUID();
        Customer customer = Customer.builder().id(UUID.randomUUID()).build();

        //when
        when(customerRepository.findById(any())).thenReturn(java.util.Optional.of(customer));
        when(beerRepository.findById(any())).thenReturn(java.util.Optional.of(Beer.builder().id(beerId).build()));
        when(beerOrderRepository.save(any())).thenReturn(BeerOrder.builder().id(beerOrderId).build());

        BeerOrder beerOrder = beerOrderServiceJPA.createOrder(BeerOrderCreateDTO.builder()
                .customerId(customer.getId())
                .beerOrderLines(Set.of(BeerOrderLineCreateDTO.builder()
                        .beerId(beerId)
                        .orderQuantity(1)
                        .build()))
                .build());

        assertThat(beerOrder).isNotNull();
        assertThat(beerOrder.getId()).isEqualTo(beerOrderId);
        verify(customerRepository).findById(any());
        verify(beerRepository).findById(any());
        verify(beerOrderRepository).save(any());
    }

    @Test
    void getById() {
        //given
        UUID beerOrderId = UUID.randomUUID();
        BeerOrder beerOrder = BeerOrder.builder().id(beerOrderId).build();

        //when
        when(beerOrderRepository.findById(any())).thenReturn(java.util.Optional.of(beerOrder));
        Optional<BeerOrderDTO> result = beerOrderServiceJPA.getById(beerOrderId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(beerOrderId);
    }

    @Test
    void listOrders() {
        //given
        UUID beerOrderId = UUID.randomUUID();
        BeerOrder beerOrder = BeerOrder.builder().id(beerOrderId).build();

        //when
        when(beerOrderRepository.findAll()).thenReturn(java.util.List.of(beerOrder));

        Page<BeerOrderDTO> result = beerOrderServiceJPA.listOrders(0, 25);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(beerOrderId);
    }
}

















