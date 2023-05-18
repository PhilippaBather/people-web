package com.batherphilippa.peopledbweb.web.formatter;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Component
public class CurrencyFormatter implements Formatter<BigDecimal> {
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.UK);

    @Override
    public BigDecimal parse(String text, Locale locale) throws ParseException {
        return null;
    }

    @Override
    public String print(BigDecimal object, Locale locale) {
        return currencyFormatter.format(object);
    }
}
