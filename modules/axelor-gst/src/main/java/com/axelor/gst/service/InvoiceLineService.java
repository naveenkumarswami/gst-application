package com.axelor.gst.service;

import java.util.List;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;

public interface InvoiceLineService {

  public Invoice getIgstAndSgstAndCgst(Invoice invoice, InvoiceLine invoiceLine);
  public List<InvoiceLine> putSelectedProduct(List<Integer> productIds);
}
