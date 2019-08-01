package com.axelor.gst.web;

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
    invoice = service.getIgstAndSgstAndCgst(invoice, invoiceLine);
    response.setValue("netAmount", invoice.getNetAmount());
    response.setValue("igst", invoice.getNetIgst());
    response.setValue("sgst", invoice.getNetSgst());
    response.setValue("cgst", invoice.getNetSgst());
    response.setValue("grossAmount", invoice.getGrossAmount());
  }
}
