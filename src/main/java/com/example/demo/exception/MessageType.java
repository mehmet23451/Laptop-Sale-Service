package com.example.demo.exception;
import lombok.Getter;
@Getter

public enum MessageType {
    NO_RECORD_EXIST("1004" , "kayıt bulunamadı"),
    TOKEN_IS_EXPIRED("1005" , "tokenın süresi bitmiştir"),
    USERNAME_NOT_FOUND("1006" , "username bulunamadı"),
    USERNAME_OR_PASSWORD_INVALID("1007" , "kullanıcı adı veya şifre hatalı"),
    REFRESH_TOKEN_NOT_FOUND("1008" , "refresh token bulunamadı"),
    REFRESH_TOKEN_IS_EXPIRED("1009" , "refresh tokenın süresi bitmiştir"),
    CURRENY_RATES_IS_OCCURED("1010" , "döviz kuru alınamadı"),
    CUSTOMER_AMOUNT_IS_NOT_ENOUGH("1011" , "müşterinin parası yeterli değildir"),
    LAPTOP_STATUS_IS_ALREADY_SALED("1012" , "laptop satılmış göründüğü için satılamaz"),
    LAPTOP_NOT_FOUND("1013","laptop bulunamadı"),
    CUSTOMER_NOT_FOUND("1014","müşteri bulunamadı"),
    SELLER_NOT_FOUND("1015","satıcı bulunamadı"),
    CONVERT_FAILED("1016","haftasonu olduğu için kur çekilemiyor"),
    SELLER_AND_LAPTOP_NOT_VALID("1017", "satıcının böyle bir laptopu yok"),
    GENERAL_EXCEPTION("9999" , "genel bir hata oluştu");


    private String code;
    private String message;

    MessageType(String code , String message) {
        this.code =code;
        this.message = message;
    }
}
