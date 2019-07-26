package com.axelor.gst.web;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.service.InvoiceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class InvoiceController {

  @Inject InvoiceService service;

  public void getContactAddress(ActionRequest request, ActionResponse response) {

    Invoice invoice = request.getContext().asType(Invoice.class);
    Contact contact = service.getContact(invoice);
    Address invocieAddress = service.getInvoiceAddress(invoice);

    request.getContext().put("primaryContact", contact);
    request.getContext().put("defalultinvoiceAddress", invocieAddress);
    if (invoice.getIsUseInvocieAddressAsShipping() != true) {
      Address shippingAddress = service.getShippingAddress(invoice);
      request.getContext().put("defalultshippingAddress", shippingAddress);
    } else {
      request.getContext().put("defalultshippingAddress", invocieAddress);
    }
  }

  public void getDefalutCompany(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    Company company = service.setDefalutComany(invoice);
    response.setValue("company", company);
  }

  public void setReferenceInvoice(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    if (invoice.getStatus().equals("Validated")) {
      String getNextNumber = service.setReferenceInvoice(invoice);
      if (getNextNumber != null) {
        response.setValue("reference", getNextNumber);
      } else response.addError("reference", "no sequence is specified for the Invoice");
    }
  }

  public void setAllValues(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    Map<Integer, BigDecimal> rateSet = service.calculateRates(invoice);

    response.setValue("netAmount", rateSet.get(1));
    response.setValue("netIgst", rateSet.get(2));
    response.setValue("netCsgt", rateSet.get(3));
    response.setValue("netSgst", rateSet.get(4));
    response.setValue("grossAmount", rateSet.get(5));
  }
}
