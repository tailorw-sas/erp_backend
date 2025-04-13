package com.kynsoft.finamer.invoicing.domain.dto.InvoiceXml;

import com.kynsoft.finamer.invoicing.domain.dto.BaseXml;
import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "TotalSummary")
@XmlAccessorType(XmlAccessType.FIELD)
public class TotalSummary extends BaseXml {
    @XmlAttribute(name = "GrossAmount")
    private double grossAmount;

    @XmlAttribute(name = "Discounts")
    private double discounts = 0.0;

    @XmlAttribute(name = "SubTotal")
    private double subTotal;

    @XmlAttribute(name = "Tax")
    private double tax = 0.0;

    @XmlAttribute(name = "Total")
    private double total;

    @Override
    public String toString() {
        return String.format(Locale.US,
                "<TotalSummary GrossAmount=\"%.2f\" Discounts=\"%.2f\" SubTotal=\"%.2f\" Tax=\"%.2f\" Total=\"%.2f\"/>",
                grossAmount, discounts, subTotal, tax, total);
    }
}