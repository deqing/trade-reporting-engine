package com.devin.tradereporting.service;

import com.devin.tradereporting.exception.XmlParsingException;
import com.devin.tradereporting.model.Trade;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${eventXmlFilesPath}")
    private String path;

    @Value("${xml.path.buyer}")
    private String buyerPath;

    @Value("${xml.path.seller}")
    private String sellerPath;

    @Value("${xml.path.currency}")
    private String currencyPath;

    @Value("${xml.path.amount}")
    private String amountPath;

    public List<Trade> parseXmlFiles(List<String> filenames) {

        List<Trade> trades = new ArrayList<>();
        var factory = DocumentBuilderFactory.newInstance();
        var xpathFactory = XPathFactory.newInstance();

        try {
            var builder = factory.newDocumentBuilder();
            for (String filename : filenames) {
                var document = builder.parse(new File(path + filename));
                var xpath = xpathFactory.newXPath();
                String buyer = getValue(xpath, document, buyerPath);
                String seller = getValue(xpath, document, sellerPath);
                String amount = getValue(xpath, document, amountPath);
                String currency = getValue(xpath, document, currencyPath);
                trades.add(new Trade(buyer, seller, amount, currency));
            }
        } catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException e) {
            throw new XmlParsingException("Error parsing XML file: " + e.getMessage(), e.getCause());
        }

        return trades;
    }

    private String getValue(XPath xpath, Document document, String path) throws XPathExpressionException {
        var xpathExpression = xpath.compile(path);
        return (String) xpathExpression.evaluate(document, XPathConstants.STRING);
    }
}
