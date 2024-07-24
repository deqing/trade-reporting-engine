package com.devin.tradereporting.service;

import com.devin.tradereporting.model.Trade;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class XmlParser {

    public List<Trade> parseXmlFiles(List<String> filenames) {
        String BUYER_PATH = "//buyerPartyReference/@href";
        String SELLER_PATH = "//sellerPartyReference/@href";
        String AMOUNT_PATH = "//paymentAmount/amount";
        String CURRENCY_PATH = "//paymentAmount/currency";

        List<Trade> trades = new ArrayList<>();
        var factory = DocumentBuilderFactory.newInstance();
        var xpathFactory = XPathFactory.newInstance();

        try {
            var builder = factory.newDocumentBuilder();
            for (String filename : filenames) {
                var document = builder.parse(new File(filename));
                var xpath = xpathFactory.newXPath();
                String buyer = getValue(xpath, document, BUYER_PATH);
                String seller = getValue(xpath, document, SELLER_PATH);
                String amount = getValue(xpath, document, AMOUNT_PATH);
                String currency = getValue(xpath, document, CURRENCY_PATH);
                trades.add(new Trade(buyer, seller, amount, currency));
            }
        } catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException e) {
            throw new RuntimeException(e);
        }

        return trades;
    }

    private String getValue(XPath xpath, Document document, String path) throws XPathExpressionException {
        var xpathExpression = xpath.compile(path);
        return (String) xpathExpression.evaluate(document, XPathConstants.STRING);
    }
}
