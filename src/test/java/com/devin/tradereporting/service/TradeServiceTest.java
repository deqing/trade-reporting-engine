package com.devin.tradereporting.service;

import com.devin.tradereporting.model.Trade;
import com.devin.tradereporting.repository.TradeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TradeServiceTest {

    @Mock
    private TradeRepository repository;

    @Mock
    private XmlParser xmlParser;

    @InjectMocks
    private TradeService service;

    @Test
    void shouldSaveTrades() {
        var trades = List.of(new Trade(), new Trade());
        when(xmlParser.parseXmlFiles(List.of("file1"))).thenReturn(trades);

        assertThat(service.saveTrades(List.of("file1")), is(equalTo(2)));

        verify(repository).saveAll(trades);
    }

    @Test
    void shouldGetTradesOfNotAnagrams() {
        var trades = List.of(
                new Trade("abc ", "ab c", "1", "AUD"),
                new Trade("abc", "abc1", "2", "AUD"),
                new Trade("abc", "cba", "3", "AUD")
        );

        when(repository.findAll(any(Specification.class))).thenReturn(trades);

        var results = service.getTrades();
        assertThat(results.size(), is(equalTo(1)));
        assertThat(results.getFirst().getPremiumAmount(), is(equalTo("2")));
    }
}
