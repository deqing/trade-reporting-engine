package com.devin.tradereporting.service;

import com.devin.tradereporting.model.Trade;
import com.devin.tradereporting.model.TradeQuery;
import com.devin.tradereporting.repository.TradeRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TradeService {

    private final TradeRepository repository;
    private final XmlParser xmlParser;

    public TradeService(TradeRepository repository, XmlParser xmlParser) {
        this.repository = repository;
        this.xmlParser = xmlParser;
    }

    /**
     * Parse XML files and save to DB.
     * @param filenames XML file paths which contains trades
     * @return number of trades that have been saved into DB
     */
    public int saveTrades(List<String> filenames) {
        var trades = xmlParser.parseXmlFiles(filenames);
        repository.saveAll(trades);
        return trades.size();
    }

    /**
     * Get all trades that match specific filter from DB.
     * @return filtered trades
     */
    public List<Trade> getTrades() {
        var query = createQuery();  // This can be replaced and/or combined with other queries
        return repository.findAll(query).stream()
                .filter(trade -> !isAnagram(trade.getBuyerParty(), trade.getSellerParty()))
                .toList();
    }

    private Specification<Trade> createQuery() {
        var sellerIsEmu = new TradeQuery("sellerParty", "EMU_BANK");
        var currencyIsAUD = new TradeQuery("premiumCurrency", "AUD");
        var emu = sellerIsEmu.and(currencyIsAUD);

        var sellerIsBison = new TradeQuery("sellerParty", "BISON_BANK");
        var currencyIsUSD = new TradeQuery("premiumCurrency", "USD");
        var bison = sellerIsBison.and(currencyIsUSD);

        return Specification.anyOf(emu, bison);
    }

    private boolean isAnagram(String str1, String str2) {
        if (str1.length() != str2.length()) {
            return false;
        }

        char[] charArray1 = str1.toCharArray();
        char[] charArray2 = str2.toCharArray();

        Arrays.sort(charArray1);
        Arrays.sort(charArray2);

        return Arrays.equals(charArray1, charArray2);
    }
}
