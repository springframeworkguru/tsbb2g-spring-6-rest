package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.services.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BeerControllerTest {

    public static final String BEER_NAME = "Beer1";
    public static final String UPC = "123456789012";
    public static final BigDecimal PRICE = new BigDecimal("10.99");
    public static final int QUANTITY_ON_HAND = 100;
    public static final BeerStyle BEER_STYLE = BeerStyle.LAGER;
    MockMvc mockMvc;

    @Mock
    BeerService beerService;

    @InjectMocks
    BeerController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    void updateBeerPatchById() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void updateById() {
    }

    @Test
    void handlePost() {
    }

    @Test
    void listBeers() {
    }

    @Test
    void getBeerById() throws Exception {
        //given
        BeerDTO beerDTO = getBeerDto();

        //when
        when(beerService.getBeerById(any(UUID.class))).thenReturn(Optional.of(beerDTO));

        //then
        mockMvc.perform(get(BeerController.BEER_PATH + "/" + beerDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(beerDTO.getId().toString()))
                .andExpect(jsonPath("$.version").value(beerDTO.getVersion().toString()))
                .andExpect(jsonPath("$.beerName").value(BEER_NAME))
                .andExpect(jsonPath("$.beerStyle").value(BEER_STYLE.toString()))
                .andExpect(jsonPath("$.upc").value(UPC))
                .andExpect(jsonPath("$.price").value(PRICE.toString()))
                .andExpect(jsonPath("$.quantityOnHand").value(QUANTITY_ON_HAND));


    }

    BeerDTO getBeerDto() {
        return BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName(BEER_NAME)
                .upc(UPC)
                .price(PRICE)
                .quantityOnHand(QUANTITY_ON_HAND)
                .beerStyle(BEER_STYLE)
                .build();
    }
}