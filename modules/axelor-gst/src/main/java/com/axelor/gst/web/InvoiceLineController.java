package com.axelor.gst.web;

import java.math.BigDecimal;
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
    BigDecimal setIGST = service.getIgst(invoice, invoiceLine); 
    BigDecimal setSGSTAndCGST = service.getSgstAndCgst(invoice, invoiceLine);
    response.setValue("igst", setIGST);
    request.getContext().put("igstvalue", setIGST);
    request.getContext().put("sGSTandCgst", setSGSTAndCGST); 
  }
  

  
}
