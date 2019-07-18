package com.axelor.gst.web;

import java.math.BigDecimal;
import java.util.List;
import javax.inject.Inject;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.service.GstService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class GstController {

  @Inject GstService service;

  @SuppressWarnings("unchecked")
  public void createInvoice(ActionRequest request, ActionResponse response) {

    if (request.getContext().get("_ids") == null) {
      return;
    }

    List<Long> requestIds = (List<Long>) request.getContext().get("_ids");
    String totalIdSelect = requestIds.toString();
    System.out.println(requestIds);
    request.getContext().put("totalProduct", totalIdSelect);
  }

  public void getContactAddress(ActionRequest request, ActionResponse response) {

    Invoice invoice = request.getContext().asType(Invoice.class);
    Contact contact = service.getContact(invoice);
    Address invocieAddress = service.getInvoiceAddress(invoice);
    Address shippingAddress = service.getShippingAddress(invoice);

    request.getContext().put("primaryContact", contact);
    request.getContext().put("defalultinvoiceAddress", invocieAddress);
    request.getContext().put("defalultshippingAddress", shippingAddress);
    
    System.err.println(invocieAddress );
    System.err.println(shippingAddress);
  }
  
  public void getCompanyAndInvoiceState(ActionRequest request, ActionResponse response)
  {
    
    InvoiceLine invoiceLine = request.getContext().asType(InvoiceLine.class);
    Invoice invoice = request.getContext().getParent().asType(Invoice.class);
    
    BigDecimal setIGST = service.getIGST(invoice, invoiceLine);
    BigDecimal setSGSTAndCGST = service.getSGSTandCGST(invoice, invoiceLine);
    
    /*response.setValue("igst", setIGST);
    response.setValue("sgst", setSGSTAndCGST);
    response.setValue("cgst", setSGSTAndCGST);*/
    request.getContext().put("igstvalue", setIGST);
    request.getContext().put("sGSTandCgst", setSGSTAndCGST);
    
  }
  
  public void getDefalutCompany(ActionRequest request, ActionResponse response)
  {
    Invoice invoice = request.getContext().asType(Invoice.class);
    Company company = service.setDefalutComany(invoice);
    System.out.println(company ); 
    response.setValue("company",company);
    
  }
  
  public void getCompanySta(ActionRequest request, ActionResponse response)
  {
    Invoice invoice = request.getContext().asType(Invoice.class);
    
    request.getContext().put("companyState", invoice.getCompany().getAddress().getState()); 
  }
  
  
  
  
}
