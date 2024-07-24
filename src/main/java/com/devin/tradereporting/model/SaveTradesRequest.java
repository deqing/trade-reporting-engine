package com.devin.tradereporting.model;

import lombok.Data;

import java.util.List;

/**
 * Request that contains XML file paths to be parsed.
 */
@Data
public class SaveTradesRequest {
    private List<String> filenames;
}
