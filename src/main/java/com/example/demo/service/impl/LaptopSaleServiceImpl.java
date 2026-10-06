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
        return SoldLaptop.builder()
                .laptop(laptop)
                .customer(customer)
                .seller(seller)
                .build();
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
        if (soldLaptop==null) return null;
        return DtoSoldLaptop.builder()
                .id(soldLaptop.getId())
                .createTime(soldLaptop.getCreateTime())
                .laptop(laptopToDTO(soldLaptop.getLaptop()))
                .customer(customerToDTO(soldLaptop.getCustomer()))
                .seller(sellerToDTO(soldLaptop.getSeller()))
                .build();

    }
    public DtoLaptop laptopToDTO(Laptop laptop){
        if (laptop==null) return null;
        return DtoLaptop.builder()
                .id(laptop.getId())
                .createTime(laptop.getCreateTime())
                .laptopModel(laptop.getLaptopModel())
                .laptopStatus(laptop.getLaptopStatus())
                .color(laptop.getColor())
                .condition(laptop.getCondition())
                .price(laptop.getPrice())
                .productionYear(laptop.getProductionYear())
                .ramOption(laptop.getRamOption())
                .storageOption(laptop.getStorageOption())
                .build();
    }
    public DtoSeller sellerToDTO(Seller seller){
        if (seller==null) return null;
        return DtoSeller.builder()
                .id(seller.getId())
                .createTime(seller.getCreateTime())
                .address(addressToDTO(seller.getAddress()))
                .account(accountToDTO(seller.getAccount()))
                .firstName(seller.getFirstName())
                .lastName(seller.getLastName())
                .build();
    }
    public DtoCustomer customerToDTO(Customer customer){
        if (customer==null) return null;
        return DtoCustomer.builder()
                .id(customer.getId())
                .createTime(customer.getCreateTime())
                .account(accountToDTO(customer.getAccount()))
                .address(addressToDTO(customer.getAddress()))
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .build();
    }
    public DtoAccount accountToDTO(Account account){
        if (account==null) return null;
        return DtoAccount.builder()
                .id(account.getId())
                .createTime(account.getCreateTime())
                .accountNo(account.getAccountNo())
                .iban(account.getIban())
                .amount(account.getAmount())
                .build();

    }
    public DtoAddress addressToDTO(Address address){
        if (address==null) return null;
        return DtoAddress.builder()
                .id(address.getId())
                .createTime(address.getCreateTime())
                .city(address.getCity())
                .district(address.getDistrict())
                .neighborhood(address.getNeighborhood())
                .street(address.getStreet())
                .build();
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
