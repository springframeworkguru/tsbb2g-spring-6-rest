package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.services.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
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

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setValidator(new LocalValidatorFactoryBean())
                .build();
    }

    @Test
    void updateBeerPatchById() throws Exception {
        //given
        Map<String, Object> beerPatch = Map.of("beerName", "New Beer Name",
                "upc", "123456789012",
                "price", new BigDecimal("10.99"),
                "beerStyle", BeerStyle.LAGER);

        when(beerService.patchBeerById(any(), any())).thenReturn(Optional.of(getBeerDto()));

        //then
        mockMvc.perform(patch(BeerController.BEER_PATH + "/" + UUID.randomUUID())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(beerPatch)))
                .andExpect(status().isNoContent());

        verify(beerService).patchBeerById(any(), any());
    }

    @Test
    void deleteById() throws Exception {
        //given
        UUID beerId = UUID.randomUUID();

        //when
        when(beerService.deleteById(any())).thenReturn(true);

        //then
        mockMvc.perform(delete(BeerController.BEER_PATH + "/" + beerId))
                .andExpect(status().isNoContent());

        verify(beerService).deleteById(any());
    }

    @Test
    void deleteByIdNotFound() throws Exception {
        //given
        UUID beerId = UUID.randomUUID();

        //when
        when(beerService.deleteById(any())).thenReturn(false);

        //then
        mockMvc.perform(delete(BeerController.BEER_PATH + "/" + beerId))
                .andExpect(status().isNotFound());

        verify(beerService).deleteById(any());
    }

    @Test
    void updateById() throws Exception {
        //given
        BeerDTO beerDTO = getBeerDto();

        when(beerService.updateBeerById(any(), any())).thenReturn(Optional.of(beerDTO));

        mockMvc.perform(put(BeerController.BEER_PATH + "/" + beerDTO.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isNoContent());

        verify(beerService).updateBeerById(any(), any());
    }

    @Test
    void updateByIdNotFound() throws Exception {
        //given
        BeerDTO beerDTO = getBeerDto();

        when(beerService.updateBeerById(any(), any())).thenReturn(Optional.empty());

        mockMvc.perform(put(BeerController.BEER_PATH + "/" + beerDTO.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isNotFound());

        verify(beerService).updateBeerById(any(), any());
    }

    @Test
    void handlePost() throws Exception {
        //given
        BeerDTO beerDTO = getBeerDto();
        beerDTO.setId(null);
        beerDTO.setVersion(null);

        String beerDtoJson = objectMapper.writeValueAsString(beerDTO);

        //when
        when(beerService.saveNewBeer(any())).thenReturn(getBeerDto());

        //then
        mockMvc.perform(post(BeerController.BEER_PATH)
                .contentType("application/json")
                .content(beerDtoJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

        verify(beerService).saveNewBeer(any());

    }

    @Test
    void listBeers() throws Exception {
        //given
        List<BeerDTO> beerDTOS = List.of(getBeerDto(), getBeerDto());

        Page<BeerDTO> beerPage = new PageImpl<>(beerDTOS, PageRequest.of(0, 10), beerDTOS.size());

        when(beerService.listBeers(any(), any(), any(), any(), any())).thenReturn(beerPage);

        mockMvc.perform(get(BeerController.BEER_PATH)
                .param("pageSize", "10")
        .param("pageNumber", "0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content[0].id").value(beerDTOS.get(0).getId().toString()))
                .andExpect(jsonPath("$.content[1].id").value(beerDTOS.get(1).getId().toString()))
                .andExpect(jsonPath("$.content[0].beerName").value(BEER_NAME ));


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