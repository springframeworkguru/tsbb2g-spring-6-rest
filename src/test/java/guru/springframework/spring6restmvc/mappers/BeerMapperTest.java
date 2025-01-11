package guru.springframework.spring6restmvc.mappers;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerDTO;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Created by jt, Spring Framework Guru.
 */
public class BeerMapperTest {

    public static final UUID BEER_ID = UUID.randomUUID();

    BeerMapper beerMapper = new BeerMapperImpl();

    @Test
    void testConvertToDto() {

        BeerDTO newObj = beerMapper.beerToBeerDto(createBeerObj());

        assertThat(newObj.getId()).isEqualTo(BEER_ID);
    }

    @Test
    void testConvertToEntity() {

        Beer newObj = beerMapper.beerDtoToBeer(createBeerDTOObj());

        assertThat(newObj.getId()).isEqualTo(BEER_ID);
    }

    Beer createBeerObj() {
        return Beer.builder().id(BEER_ID).build();
    }

    BeerDTO createBeerDTOObj() {
        return BeerDTO.builder().id(BEER_ID).build();
    }
}
