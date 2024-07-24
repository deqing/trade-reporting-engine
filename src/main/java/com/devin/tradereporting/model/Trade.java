package com.devin.tradereporting.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
public class Trade {
    @Id
    @GeneratedValue
    private int id;

    private String buyerParty;
    private String sellerParty;
    private String premiumAmount;
    private String premiumCurrency;

    public Trade(String buyer, String seller, String amount, String currency) {
        buyerParty = buyer;
        sellerParty = seller;
        premiumAmount = amount;
        premiumCurrency = currency;
    }
}
