package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSave() {
        Beer beer = Beer.builder().build();

        beer.setBeerName("name");
        beer.setBeerStyle(BeerStyle.LAGER);
        beer.setUpc("123456789012");
        beer.setPrice(new BigDecimal("12.99"));

        Beer savedBeer = beerRepository.saveAndFlush(beer);
        //beerRepository.flush();

        assertThat(savedBeer).isNotNull();

    }
}