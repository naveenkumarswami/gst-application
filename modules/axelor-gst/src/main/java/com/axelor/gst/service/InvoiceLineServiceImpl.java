package com.axelor.gst.service;

import java.math.BigDecimal;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;

public class InvoiceLineServiceImpl implements InvoiceLineService {
  
  @Override
  public BigDecimal getIgst(Invoice invoice, InvoiceLine invoiceLine) {

    try {
      String companyState = invoice.getCompany().getAddress().getState().getName();
      String invoiceState = invoice.getInvoiceAddress().getState().getName();
      BigDecimal netAmount = invoiceLine.getNetAmount();
      BigDecimal gstRate = invoiceLine.getGstRate();
      BigDecimal igst = new BigDecimal(0);

      if (!invoiceState.equals(companyState)) {
        igst = netAmount.multiply(gstRate).divide(new BigDecimal(100));        
        return igst;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new BigDecimal(0);
  }

  @Override
  public BigDecimal getSgstAndCgst(Invoice invoice, InvoiceLine invoiceLine) {

    try {
      String companyState = invoice.getCompany().getAddress().getState().getName();
      String invoiceState = invoice.getInvoiceAddress().getState().getName();
      BigDecimal netAmount = invoiceLine.getNetAmount();
      BigDecimal gstRate = invoiceLine.getGstRate();

      BigDecimal sgst = BigDecimal.ZERO;

      if (invoiceState.equals(companyState)) {
        sgst = netAmount.multiply(gstRate.divide(new BigDecimal(2)).divide(new BigDecimal(100)));
        return sgst;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return BigDecimal.ZERO;
  }
  
}
