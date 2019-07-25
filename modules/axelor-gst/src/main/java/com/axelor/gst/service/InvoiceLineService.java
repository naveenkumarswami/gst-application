package com.axelor.gst.service;

import java.math.BigDecimal;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;

public interface InvoiceLineService {
  
  public BigDecimal getIgst(Invoice invoice , InvoiceLine invoiceLine); 
  public BigDecimal getSgstAndCgst (Invoice invoice , InvoiceLine invoiceLine);
  
}
