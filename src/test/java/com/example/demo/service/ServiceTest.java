package com.example.demo.service;

import com.example.demo.dto.CurrencyRatesItems;
import com.example.demo.dto.CurrencyRatesResponse;
import com.example.demo.dto.models.DtoSoldLaptop;
import com.example.demo.dto.models.DtoSoldLaptopIU;
import com.example.demo.entity.*;
import com.example.demo.entity.enums.LaptopStatus;
import com.example.demo.exception.BaseException;
import com.example.demo.repo.*;
import com.example.demo.service.impl.LaptopSaleServiceImpl;
import net.bytebuddy.NamingStrategy;
import org.aspectj.lang.annotation.Before;
import org.hibernate.sql.ast.tree.expression.CaseSimpleExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.security.config.web.server.ServerOneTimeTokenLoginDsl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceTest
{
    @Mock
    private  LaptopRepository laptopRepository;
    @Mock
    private  CustomerRepository customerRepository;
    @Mock
    private  CurrencyRatesService currencyRatesService;
    @Mock
    private  SoldLaptopRepository soldLaptopRepository;
    @Mock
    private  SellerRepository sellerRepository;
    @Mock
    private SellerLaptopRepository sellerLaptopRepository;
    @InjectMocks
    private LaptopSaleServiceImpl laptopSaleService;

    private Customer customer;
    private Seller seller;
    private Laptop laptop;
    private DtoSoldLaptopIU requestDto;
    private CurrencyRatesResponse currencyResponse;
    @BeforeEach
        void setUp(){
            Account customerAccount = new Account();
            customerAccount.setAmount(new BigDecimal("50000.00"));
            customer = new Customer();
            customer.setId(1L);
            customer.setAccount(customerAccount);

            Account sellerAccount = new Account();
            sellerAccount.setAmount(new BigDecimal("10000.00"));
            seller = new Seller();
            seller.setId(2L);
            seller.setAccount(sellerAccount);

            laptop = new Laptop();
            laptop.setId(3L);
            laptop.setPrice(new BigDecimal("1000.00"));
            laptop.setLaptopStatus(LaptopStatus.NOT_SOLD);

            requestDto = new DtoSoldLaptopIU();
            requestDto.setCustomerId(1L);
            requestDto.setSellerId(2L);
            requestDto.setLaptopId(3L);

            CurrencyRatesItems item = new CurrencyRatesItems();
            item.setUsd("30.00");

            currencyResponse = new CurrencyRatesResponse();
            currencyResponse.setItems(List.of(item));
        }
    @Test
    void testLaptopSaleService_Successful() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(sellerRepository.findById(2L)).thenReturn(Optional.of(seller));
        when(laptopRepository.findById(3L)).thenReturn(Optional.of(laptop));
        when(currencyRatesService.getCurrencyRates(anyString(), anyString())).thenReturn(currencyResponse);
        when(soldLaptopRepository.save(any(SoldLaptop.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(sellerLaptopRepository.existsBySellerAndLaptop(seller,laptop)).thenReturn(true);

        DtoSoldLaptop result = laptopSaleService.sellLaptop(requestDto);

        assertNotNull(result);
        assertEquals(new BigDecimal("20000.00"), customer.getAccount().getAmount());
        assertEquals(new BigDecimal("40000.00"), seller.getAccount().getAmount());
        assertEquals(LaptopStatus.SOLD, laptop.getLaptopStatus());

        verify(soldLaptopRepository, times(1)).save(any(SoldLaptop.class));
    }
    @Test
    void testLaptopSaleService_InsufficientBalance_ThrowsBaseException() {
        customer.getAccount().setAmount(new BigDecimal("100.00"));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(sellerRepository.findById(2L)).thenReturn(Optional.of(seller));
        when(laptopRepository.findById(3L)).thenReturn(Optional.of(laptop));
        when(currencyRatesService.getCurrencyRates(anyString(), anyString())).thenReturn(currencyResponse);

        assertThrows(BaseException.class, () -> laptopSaleService.sellLaptop(requestDto));

        verify(soldLaptopRepository, never()).save(any(SoldLaptop.class));
    }

}

