package com.axelor.gst.web;

import java.math.BigDecimal;
import java.util.Map;
import javax.inject.Inject;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.service.InvoiceLineService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class InvoiceLineController {
  
  @Inject InvoiceLineService service;
  
  public void getCompanyAndInvoiceState(ActionRequest request, ActionResponse response) {

    InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);   
    Invoice invoice = request.getContext().getParent().asType(Invoice.class); 
    Map<Integer, BigDecimal> allRate = service.getIgstAndSgstAndCgst(invoice, invoiceLine);
    response.setValue("igst", allRate.get(1));
    request.getContext().put("igstvalue", allRate.get(2));
    request.getContext().put("sGSTandCgst", allRate.get(2)); 
  }
  

  
}
