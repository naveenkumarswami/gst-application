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
      BigDecimal netAmount = invoiceLine.getNetAmount();
      BigDecimal gstRate = invoiceLine.getGstRate();
      BigDecimal sgst=BigDecimal.ZERO,igst = BigDecimal.ZERO;

      if (!invoiceState.equals(companyState)) {
        igst = netAmount.multiply(gstRate).divide(new BigDecimal(100));        
        allRate.put(1, igst);
        allRate.put(2, BigDecimal.ZERO);
      }
      else if (invoiceState.equals(companyState)) {
        sgst = netAmount.multiply(gstRate.divide(new BigDecimal(2)).divide(new BigDecimal(100)));
        allRate.put(1, BigDecimal.ZERO);
        allRate.put(2, sgst);
      }
      else
      {
        allRate.put(1, BigDecimal.ZERO);
        allRate.put(2, BigDecimal.ZERO);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return allRate;
  }
}