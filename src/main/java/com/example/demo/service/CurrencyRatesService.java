package com.example.demo.service;

import com.example.demo.dto.CurrencyRatesResponse;

public interface CurrencyRatesService {
    public CurrencyRatesResponse getCurrencyRates(String startDate , String endDate);
}
