package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.Mapping;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.N;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerController customerController;

    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .build();
    }

    @DisplayName("Test Patch Operations")
    @Nested
    public class PatchTests {

        @DisplayName("Test Patch Operation")
        @Test
        void patchCustomerById() throws Exception {
            //given
            Map<String, Object> patchMap = Map.of("name", "Fred");

            when(customerService.patchCustomerById(any(), any())).thenReturn(Optional.of(CustomerDTO.builder().build()));

            //when
            mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID, UUID.randomUUID())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(patchMap)))
                    .andExpect(status().isNoContent());

            verify(customerService).patchCustomerById(any(), any());
        }
    }

    @DisplayName("Test Delete Operations")
    @Nested
    public class TestDeleteOperations {
        @Test
        void deleteCustomerById() throws Exception {
            UUID id = UUID.randomUUID();

            when(customerService.deleteCustomerById(any())).thenReturn(true);

            mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, id))
                    .andExpect(status().isNoContent());

            verify(customerService).deleteCustomerById(any());
        }

        @Test
        void deleteCustomerByIdNotFound() throws Exception {
            UUID id = UUID.randomUUID();

            when(customerService.deleteCustomerById(any())).thenReturn(false);

            mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, id))
                    .andExpect(status().isNotFound());

            verify(customerService).deleteCustomerById(any());
        }
    }

    @Nested
    public class  UpdateTests {
        @Test
        void updateCustomerByID() throws Exception {
            CustomerDTO customerDTO = getCustomerDTO();

            when(customerService.updateCustomerById(any(), any())).thenReturn(Optional.of(customerDTO));

            //when
            mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, customerDTO.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(customerDTO)))
                    .andExpect(status().isNoContent());

            verify(customerService).updateCustomerById(any(), any());
        }

        @Test
        void updateCustomerByIDNotFound() throws Exception {
            CustomerDTO customerDTO = getCustomerDTO();

            when(customerService.updateCustomerById(any(), any())).thenReturn(Optional.empty());

            //when
            mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, customerDTO.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(customerDTO)))
                    .andExpect(status().isNotFound());

            verify(customerService).updateCustomerById(any(), any());
        }
    }

    @DisplayName("Test Create Operations")
    @Nested
   public class TestCreateOperations {
        @Test
        void handlePost() throws Exception {
            CustomerDTO customerDTO = getCustomerDTO();

            when(customerService.saveNewCustomer(any())).thenReturn(customerDTO);

            //when
            mockMvc.perform(post(CustomerController.CUSTOMER_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(customerDTO))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"));

            verify(customerService).saveNewCustomer(any());
        }
   }

   @DisplayName("Test Get Operations")
    @Nested
    public class TestGetOperations {
       @Test
       void listAllCustomers() throws Exception{
           CustomerDTO customerDTO = getCustomerDTO();

           when(customerService.getAllCustomers()).thenReturn(List.of(customerDTO));

           mockMvc.perform(get(CustomerController.CUSTOMER_PATH))
                   .andExpect(status().isOk());
       }

       @Test
       void getCustomerById()  throws  Exception{
           CustomerDTO customerDTO = getCustomerDTO();

           when(customerService.getCustomerById(any())).thenReturn(Optional.of(customerDTO));

           //when
           mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, customerDTO.getId()))
                   .andExpect(status().isOk());
       }
    }

    CustomerDTO getCustomerDTO(){
        return CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Joe")
                .build();
    }
}








