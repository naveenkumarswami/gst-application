package com.axelor.gst.service;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;

public interface InvoiceLineService {

  public Invoice getIgstAndSgstAndCgst(Invoice invoice, InvoiceLine invoiceLine);
}
