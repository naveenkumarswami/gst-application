package com.axelor.gst.web;

import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.repo.InvoiceRepository;
import com.axelor.gst.service.InvoiceService;
import com.axelor.gst.service.ProductService;
import com.axelor.gst.service.SequenceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class InvoiceController {
  @Inject InvoiceService service;
  @Inject ProductService productService;
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
    
    String[] model = request.getModel().split("\\.");
    
    if (invoice.getReference() == null && invoice.getStatus().equals(InvoiceRepository.STATUS_VALIDATED)) {
      String getNextNumber = sequenceService.setReference(model[model.length-1]);
      if (getNextNumber != null) {
        response.setValue("reference", getNextNumber);
      } else response.addError("reference", "no sequence is specified for the Invoice");
    }
  }

  public void setAllValues(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    invoice = service.calculateRates(invoice);

    try {
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

  @SuppressWarnings("unchecked")
  public void getSelectedProduct(ActionRequest request, ActionResponse response) {

    List<Integer> selectedIds = (List<Integer>) request.getContext().get("SelectProductIds");
    List<InvoiceLine> invoiceLineList = productService.putSelectedProduct(selectedIds);
    response.setValue("party", request.getContext().get("party_name"));
    response.setValue("invoiceItemsList", invoiceLineList);
  }

  public void getPartyContactListAndAddressList(ActionRequest request, ActionResponse response) {

    Party party = request.getContext().asType(Invoice.class).getParty();

    Map<String, String> getIds = service.getDomainIds(party);

    response.setAttr("partyContact", "domain", "self.id IN " + getIds.get("contacIds"));

    response.setAttr("invoiceAddress", "domain", "self.id IN " + getIds.get("addressIds"));

    response.setAttr("shippingAddress", "domain", "self.id IN " + getIds.get("addressIds"));
  }
}
