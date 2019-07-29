package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.Map;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;

public interface InvoiceLineService {
  
  public Map<Integer, BigDecimal> getIgstAndSgstAndCgst(Invoice invoice , InvoiceLine invoiceLine);
}
