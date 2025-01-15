package com.kynsoft.finamer.invoicing.domain.dto.InvoiceXml;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "GeneralData")
@XmlAccessorType(XmlAccessType.FIELD)
public class GeneralData {
    @XmlAttribute(name = "Ref")
    private String ref;

    @XmlAttribute(name = "Type")
    private String type = "FacturaComercial";

    @XmlAttribute(name = "Date")
    private String date;

    @XmlAttribute(name = "Currency")
    private String currency = "USD";

    @XmlAttribute(name = "TaxIncluded")
    private boolean taxIncluded = true;

    @XmlAttribute(name = "Status")
    private String status = "";

    public void setDate(LocalDate date) {
        this.date = date != null ? date.format(DateTimeFormatter.ISO_DATE) : null;
    }
}