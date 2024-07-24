package com.devin.tradereporting.controller;

import com.devin.tradereporting.model.SaveTradesRequest;
import com.devin.tradereporting.model.Trade;
import com.devin.tradereporting.service.TradeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trades")
public class TradeController {

    private final TradeService service;

    TradeController(TradeService service) {
        this.service = service;
    }

    @GetMapping
    public List<Trade> getTrades() {
        return service.getTrades();
    }

    @PostMapping
    public ResponseEntity<String> saveTrades(@RequestBody SaveTradesRequest request) {
        int savedTrades = service.saveTrades(request.getFilenames());
        String body = String.format("Saved %d trades", savedTrades);

        if (savedTrades > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("No trades are saved");
        }
    }
}
