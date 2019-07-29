package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;

public class InvoiceLineServiceImpl implements InvoiceLineService {

  @Override
  public Map<Integer, BigDecimal> getIgstAndSgstAndCgst(Invoice invoice, InvoiceLine invoiceLine) {
    Map<Integer, BigDecimal> allRate = new HashMap<>();
    try {
      String companyState = invoice.getCompany().getAddress().getState().getName();
      String invoiceState = invoice.getInvoiceAddress().getState().getName();
      BigDecimal netAmount =
          (new BigDecimal(invoiceLine.getQty())).multiply(invoiceLine.getPrice());
      allRate.put(3, netAmount);
      BigDecimal gstRate = invoiceLine.getGstRate();
      BigDecimal sgst = BigDecimal.ZERO, igst = BigDecimal.ZERO;

      if (!invoiceState.equals(companyState)) {
        igst = netAmount.multiply(gstRate).divide(new BigDecimal(100));
        allRate.put(1, igst);
        allRate.put(2, BigDecimal.ZERO);
      } else if (invoiceState.equals(companyState)) {
        sgst = netAmount.multiply(gstRate.divide(new BigDecimal(2)).divide(new BigDecimal(100)));
        allRate.put(1, BigDecimal.ZERO);
        allRate.put(2, sgst);
      } else {
        allRate.put(1, BigDecimal.ZERO);
        allRate.put(2, BigDecimal.ZERO);
      }
      allRate.put(4, netAmount.add(igst).add(sgst).multiply(new BigDecimal(2)));

    } catch (Exception e) {
      e.printStackTrace();
    }
    return allRate;
  }
}
