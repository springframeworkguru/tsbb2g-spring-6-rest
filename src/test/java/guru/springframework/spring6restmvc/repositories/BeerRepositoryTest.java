package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;
    //Create additional tests for Spring Data JPA Repository BeerRepository
    //Add Test to validate validation is failing
    //Add Tests for each FindBy method to verify these are functioning property

    // test for findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle
    @Test
    void testFindAllByBeerNameIsLikeIgnoreCaseAndBeerStyle() {
        Beer beer = createBeerEntity();
        beer.setBeerName("Test Beer");
        beer.setBeerStyle(BeerStyle.LAGER);
        beer.setUpc("123456789012");
        beer.setPrice(new BigDecimal("12.99"));

        beerRepository.saveAndFlush(beer);

        Page<Beer> beers = beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("Test%", BeerStyle.LAGER, PageRequest.of(0, 10));
        assertThat(beers.getTotalElements()).isEqualTo(1);
    }

    // test for findAllByBeerStyle
    @Test
    void testFindByStyle() {
        Beer beer = createBeerEntity();
        beer.setBeerStyle(BeerStyle.LAGER);

        beerRepository.saveAndFlush(beer);

        Page<Beer> beers = beerRepository.findAllByBeerStyle(BeerStyle.LAGER, PageRequest.of(0, 10));
        assertThat(beers.getTotalElements()).isEqualTo(1);
    }

    // test for findAllByBeerNameIsLikeIgnoreCase
    @Test
    void testFindAllByBeerNameIsLikeIgnoreCase() {
        Beer beer = createBeerEntity();
        beer.setBeerName("Test Beer");
        beer.setBeerStyle(BeerStyle.LAGER);
        beer.setUpc("123456789012");
        beer.setPrice(new BigDecimal("12.99"));

        beerRepository.saveAndFlush(beer);

        // Test the findAllByBeerNameIsLikeIgnoreCase method
        Page<Beer> beers = beerRepository.findAllByBeerNameIsLikeIgnoreCase("Test%", PageRequest.of(0, 10));
        assertThat(beers.getTotalElements()).isEqualTo(1);
    }

    @Test
    void testValidationFail(){
        Beer beer = createBeerEntity();
        beer.setBeerName(null);
        beer.setUpc(null);
        beer.setPrice(null);

        assertThat(beer.getBeerName()).isNull();
        assertThat(beer.getUpc()).isNull();
        assertThat(beer.getPrice()).isNull();

        // Uncomment the following line to test validation failure
         assertThrows(ConstraintViolationException.class, () -> beerRepository.saveAndFlush(beer));
    }

    @Test
    void testSave() {
        Beer beer = createBeerEntity();

        Beer savedBeer = beerRepository.saveAndFlush(beer);
        //beerRepository.flush();

        assertThat(savedBeer).isNotNull();
    }

    Beer createBeerEntity() {
        return Beer.builder()
                .beerName("Test Beer")
                .beerStyle(BeerStyle.LAGER)
                .upc("123456789012")
                .price(new BigDecimal("12.99"))
                .build();
    }
}