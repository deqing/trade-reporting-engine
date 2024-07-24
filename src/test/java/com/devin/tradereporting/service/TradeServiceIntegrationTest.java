package com.devin.tradereporting.service;

import com.devin.tradereporting.model.Trade;
import com.devin.tradereporting.repository.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@SpringBootTest
public class TradeServiceIntegrationTest {

    @Autowired
    private TradeRepository repository;

    @Autowired
    private XmlParser xmlParser;

    private TradeService service;

    @BeforeEach
    public void init() {
        repository.save(new Trade("EMU_BANK", "EMU_BANK", "100", "AUD"));
        repository.save(new Trade("EMU_BANK", "BISON_BANK", "200", "AUD"));
        repository.save(new Trade("BISON_BANK", "EMU_BANK", "300", "AUD"));  // report
        repository.save(new Trade("BONIS_BANK", "BISON_BANK", "400", "USD"));  // not report - seller and buyer are anagrams
        repository.save(new Trade("EMU_BANK", "BISON_BANK", "500", "USD"));  // report

        service = new TradeService(repository, xmlParser);
    }

    @Test
    void shouldGetTrades() {
        var results = service.getTrades();
        assertThat(results.size(), is(equalTo(2)));
        assertThat(results.getFirst().getPremiumAmount(), is(equalTo("300")));
        assertThat(results.get(1).getPremiumAmount(), is(equalTo("500")));
    }
}
