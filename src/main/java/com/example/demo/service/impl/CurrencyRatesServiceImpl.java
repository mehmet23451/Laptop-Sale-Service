        package com.example.demo.service.impl;

        import com.example.demo.dto.CurrencyRatesResponse;
        import com.example.demo.exception.BaseException;
        import com.example.demo.exception.ErrorMessage;
        import com.example.demo.exception.MessageType;
        import com.example.demo.service.CurrencyRatesService;
        import org.springframework.core.ParameterizedTypeReference;
        import org.springframework.http.HttpEntity;
        import org.springframework.http.HttpMethod;
        import org.springframework.http.ResponseEntity;
        import org.springframework.stereotype.Service;
        import org.springframework.web.client.RestTemplate;
        import org.springframework.http.HttpHeaders;


        @Service
        public class CurrencyRatesServiceImpl implements CurrencyRatesService {
            @Override
            public CurrencyRatesResponse getCurrencyRates(String startDate, String endDate) {
                String rootURL = "https://evds2.tcmb.gov.tr/service/evds/";
                String series = "TP.DK.USD.A";
                String type = "json";

                String endpoint = rootURL + "series=" + series + "&startDate=" + startDate + "&endDate=" + endDate + "&type="
                        + type;

                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.set("key", "XsBxAxzaVo");

                HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

                try {
                    RestTemplate restTemplate = new RestTemplate();

                    ResponseEntity<CurrencyRatesResponse> response = restTemplate.exchange(endpoint, HttpMethod.GET, httpEntity,
                            new ParameterizedTypeReference<CurrencyRatesResponse>() {
                            });
                    if (response.getStatusCode().is2xxSuccessful()) {
                        return response.getBody();
                    }
                } catch (Exception e) {
                    throw new BaseException(new ErrorMessage(MessageType.CURRENY_RATES_IS_OCCURED, e.getMessage()));
                }
                return null;

            }
        }
