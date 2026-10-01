package com.example.demo.service.impl;

import com.example.demo.dto.models.*;
import com.example.demo.entity.*;
import com.example.demo.entity.enums.LaptopStatus;
import com.example.demo.exception.BaseException;
import com.example.demo.exception.ErrorMessage;
import com.example.demo.exception.MessageType;
import com.example.demo.repo.*;
import com.example.demo.service.CurrencyRatesService;
import com.example.demo.service.LaptopSaleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;

@Service
public class LaptopSaleServiceImpl implements LaptopSaleService {
    private final LaptopRepository laptopRepository;
    private final CustomerRepository customerRepository;
    private final CurrencyRatesService currencyRatesService;
    private final SoldLaptopRepository soldLaptopRepository;
    private final SellerRepository sellerRepository;
    private final SellerLaptopRepository sellerLaptopRepository;
    public LaptopSaleServiceImpl(LaptopRepository laptopRepository, CustomerRepository customerRepository, CurrencyRatesService currencyRatesService, SoldLaptopRepository soldLaptopRepository, SellerRepository sellerRepository, SellerLaptopRepository sellerLaptopRepository) {
        this.laptopRepository = laptopRepository;
        this.customerRepository = customerRepository;
        this.currencyRatesService = currencyRatesService;
        this.soldLaptopRepository = soldLaptopRepository;
        this.sellerRepository = sellerRepository;
        this.sellerLaptopRepository=sellerLaptopRepository;
    }


    public boolean laptopNotSold(Laptop laptop){
        return laptop.getLaptopStatus().equals(LaptopStatus.NOT_SOLD);
    }
    public boolean isAmountEnough(Customer customer,BigDecimal price){
        return customer.getAccount().getAmount().compareTo(price) >= 0;
    }
    public String convertDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }
    public BigDecimal convertToTL(BigDecimal priceInUsd){
        DayOfWeek dayOfWeek= LocalDate.now().getDayOfWeek();
        if (dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY)){
            throw new BaseException(new ErrorMessage(MessageType.CONVERT_FAILED,null));
        }
        BigDecimal usd=new BigDecimal(currencyRatesService.getCurrencyRates( convertDate(new Date()) ,convertDate(new Date())).getItems().get(0).getUsd());
        return usd.multiply(priceInUsd);
    }
    private SoldLaptop createSoldLaptop(Customer customer,Seller seller,Laptop laptop){
        SoldLaptop soldLaptop= new SoldLaptop();
        soldLaptop.setLaptop(laptop);
        soldLaptop.setCustomer(customer);
        soldLaptop.setSeller(seller);
        return soldLaptop;
    }
    public void executeAllChanges(SoldLaptop soldLaptop,BigDecimal sellingPrice){
        soldLaptop.getCustomer().getAccount().setAmount(soldLaptop.getCustomer().getAccount().getAmount().subtract(sellingPrice));
        customerRepository.save(soldLaptop.getCustomer());
        soldLaptop.getSeller().getAccount().setAmount(soldLaptop.getSeller().getAccount().getAmount().add(sellingPrice));
        sellerRepository.save(soldLaptop.getSeller());
        soldLaptop.getLaptop().setLaptopStatus(LaptopStatus.SOLD);
        laptopRepository.save(soldLaptop.getLaptop());
    }
    public DtoSoldLaptop toDTO(SoldLaptop soldLaptop){
        DtoSoldLaptop dtoSoldLaptop=new DtoSoldLaptop();
        DtoCustomer dtoCustomer=new DtoCustomer();
        DtoSeller dtoSeller= new DtoSeller();
        DtoLaptop dtoLaptop= new DtoLaptop();
        BeanUtils.copyProperties(soldLaptop,dtoSoldLaptop);
        BeanUtils.copyProperties(soldLaptop.getCustomer(),dtoCustomer);
        BeanUtils.copyProperties(soldLaptop.getSeller(),dtoSeller);
        BeanUtils.copyProperties(soldLaptop.getLaptop(),dtoLaptop);
        dtoSoldLaptop.setCustomer(dtoCustomer);
        dtoSoldLaptop.setSeller(dtoSeller);
        dtoSoldLaptop.setLaptop(dtoLaptop);
        return dtoSoldLaptop;

    }
    @Override
    @Transactional
    public DtoSoldLaptop sellLaptop(DtoSoldLaptopIU dtoSoldLaptopIU) {
        //her birini kontrol et, yoksa exception fırlat,
        Customer customer = customerRepository.findById(dtoSoldLaptopIU.getCustomerId())
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.CUSTOMER_NOT_FOUND, null)));

        Seller seller = sellerRepository.findById(dtoSoldLaptopIU.getSellerId())
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.SELLER_NOT_FOUND, null)));

        Laptop laptop = laptopRepository.findById(dtoSoldLaptopIU.getLaptopId())
                .orElseThrow(() -> new BaseException(new ErrorMessage(MessageType.LAPTOP_NOT_FOUND, null)));
        //laptop satılmış olma kontrolü,
        if (!laptopNotSold(laptop)){
            throw new BaseException(new ErrorMessage(MessageType.LAPTOP_STATUS_IS_ALREADY_SALED,null));
        }
        BigDecimal sellingPrice=convertToTL(laptop.getPrice());
        // kullanıcının bakiye kontrolü,

        if (!isAmountEnough(customer,sellingPrice)){
            throw new BaseException(new ErrorMessage(MessageType.CUSTOMER_AMOUNT_IS_NOT_ENOUGH,null));
        }
        // laptop gerçekten satıcının olup olmama kontrolü

        if (!sellerLaptopRepository.existsBySellerAndLaptop(seller,laptop)){
            throw new BaseException(new ErrorMessage(MessageType.SELLER_AND_LAPTOP_NOT_VALID,null));
        }


        // yeni bir kayıt oluşturacağız,
        SoldLaptop soldLaptop=soldLaptopRepository.save(createSoldLaptop(customer,seller,laptop));
        //bütün değişiklikleri uygulayacağız.
        executeAllChanges(soldLaptop,sellingPrice);
        return toDTO(soldLaptop);

    }
}
