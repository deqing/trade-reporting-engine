package com.devin.tradereporting.model;

import lombok.Data;

import java.util.List;

@Data
public class SaveTradesRequest {
    private List<String> filenames;
}
