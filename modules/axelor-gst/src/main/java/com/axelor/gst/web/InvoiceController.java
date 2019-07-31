package com.axelor.gst.web;

import java.util.List;
import javax.inject.Inject;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.service.InvoiceService;
import com.axelor.gst.service.SequenceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class InvoiceController {

  @Inject InvoiceService service;
  @Inject SequenceService sequenceService;

  public void getContactAddress(ActionRequest request, ActionResponse response) {

    Invoice invoice = request.getContext().asType(Invoice.class);
    Contact contact = service.getContact(invoice);
    Address invocieAddress = service.getInvoiceAddress(invoice);

    response.setValue("partyContact", contact);
    response.setValue("invoiceAddress", invocieAddress);
    try {
      if (invoice.getIsUseInvocieAddressAsShipping() != true
          || invoice.getIsUseInvocieAddressAsShipping() == null) {
        Address shippingAddress = service.getShippingAddress(invoice);
        response.setValue("shippingAddress", shippingAddress);
      } else {
        response.setValue("shippingAddress", invocieAddress);
      }
    } catch (NullPointerException e) {
      response.setValue("shippingAddress", invocieAddress);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setReferenceInvoice(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    if (invoice.getReference()==null||invoice.getStatus().equals("Validated")) {
      String getNextNumber = sequenceService.setReference("Invoice");
      if (getNextNumber != null) {
        response.setValue("reference", getNextNumber);
      } else response.addError("reference", "no sequence is specified for the Invoice");
    }
  }

  public void setAllValues(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    invoice = service.calculateRates(invoice);

    try {
      /*response.setValue("netAmount", invoice.getNetAmount());
      response.setValue("netIgst", invoice.getNetIgst());
      response.setValue("netCsgt", invoice.getNetCsgt());
      response.setValue("netSgst", invoice.getNetSgst());
      response.setValue("grossAmount", invoice.getGrossAmount());*/
      response.setValues(invoice);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void updateInvoiceLineList(ActionRequest request, ActionResponse response) {

    Invoice invoice = request.getContext().asType(Invoice.class);
    List<InvoiceLine> getInvoiceLineList = service.getInvoiceLineList(invoice);
    response.setValue("invoiceItemsList", getInvoiceLineList);
  }
}
