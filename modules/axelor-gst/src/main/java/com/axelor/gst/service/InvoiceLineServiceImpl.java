package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.State;

public class InvoiceLineServiceImpl implements InvoiceLineService {

  @Override
  public Invoice getIgstAndSgstAndCgst(Invoice invoice, InvoiceLine invoiceLine) {
    try {
      State companyState = invoice.getCompany().getAddress().getState();
      State invoiceState = invoice.getInvoiceAddress().getState();
      BigDecimal netAmount =
          (new BigDecimal(invoiceLine.getQty())).multiply(invoiceLine.getPrice());
      invoice.setNetAmount(netAmount);
      BigDecimal gstRate = invoiceLine.getGstRate();
      BigDecimal sgst = BigDecimal.ZERO, igst = BigDecimal.ZERO;

      if (!invoiceState.equals(companyState)) {
        igst = netAmount.multiply(gstRate).divide(new BigDecimal(100));
        invoice.setNetIgst(igst);
        invoice.setNetSgst(BigDecimal.ZERO);
      } else if (invoiceState.equals(companyState)) {
        sgst = netAmount.multiply(gstRate.divide(new BigDecimal(2)).divide(new BigDecimal(100)));
        invoice.setNetIgst(BigDecimal.ZERO);
        invoice.setNetSgst(sgst);
      } else {
        invoice.setNetIgst(BigDecimal.ZERO);
        invoice.setNetSgst(BigDecimal.ZERO);
      }
      invoice.setGrossAmount(netAmount.add(igst).add(sgst).multiply(new BigDecimal(2)));

    } catch (Exception e) {
      e.printStackTrace();
    }
    return invoice;
  }
}
