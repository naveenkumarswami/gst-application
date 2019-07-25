package com.axelor.gst.service;

import java.math.BigDecimal;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;

public interface InvoiceLineService {
  
  public BigDecimal getIGST(Invoice invocie , InvoiceLine invoiceLine); 
  public BigDecimal getSGSTandCGST (Invoice invocie , InvoiceLine invoiceLine);
  
}
