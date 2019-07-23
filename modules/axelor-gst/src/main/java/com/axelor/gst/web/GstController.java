package com.axelor.gst.web;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import javax.inject.Inject;
import com.axelor.app.AppSettings;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
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

    List<Integer> requestIds = (List<Integer>) request.getContext().get("_ids");
    String totalIdSelect = requestIds.toString();
    totalIdSelect = totalIdSelect.substring(1, totalIdSelect.length() - 1);
    System.out.println(totalIdSelect);
    request.getContext().put("totalProduct", totalIdSelect);
  }

  public void getContactAddress(ActionRequest request, ActionResponse response) {

    Invoice invoice = request.getContext().asType(Invoice.class);
    Contact contact = service.getContact(invoice);

    // 2nd method to find the address
    //    System.out.println(
    //        "invoice add "
    //            + invoice
    //                .getParty()
    //                .getAddressList()
    //                .stream()
    //                .filter(a -> a.getType().equals("invoice") || a.getType().equals("default"))
    //                .findFirst());

    Address invocieAddress = service.getInvoiceAddress(invoice);
    Address shippingAddress = service.getShippingAddress(invoice);

    request.getContext().put("primaryContact", contact);
    request.getContext().put("defalultinvoiceAddress", invocieAddress);
    request.getContext().put("defalultshippingAddress", shippingAddress);
  }

  public void getCompanyAndInvoiceState(ActionRequest request, ActionResponse response) {

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

  public void getDefalutCompany(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    Company company = service.setDefalutComany(invoice);
    System.out.println(company);
    response.setValue("company", company);
  }

  public void getCompanySta(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    request.getContext().put("companyState", invoice.getCompany().getAddress().getState());
  }

  public void setAllValues(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);

    List<InvoiceLine> invoiceLine = invoice.getInvoiceItemsList();

    // using long or double
    /*double netAmount =
        invoiceLine
            .stream()
            .mapToDouble(getNetAmount -> getNetAmount.getNetAmount().doubleValue())
            .sum();
    BigDecimal NetIgst =
        invoiceLine.stream().map(getIgst -> getIgst.getIgst()).reduce(BigDecimal.ZERO , BigDecimal :: add);
    double netCgst =
        invoiceLine.stream().mapToDouble(getCgst -> getCgst.getCgst().doubleValue()).sum();
    double netSgst =
        invoiceLine.stream().mapToDouble(getSgst -> getSgst.getSgst().doubleValue()).sum();
    double grossAmount = invoiceLine.stream().mapToDouble(getGrossAmount -> getGrossAmount.getGrossAmount().doubleValue()).sum(); */

    response.setValue(
        "netAmount",
        invoiceLine
            .stream()
            .map(rate -> rate.getNetAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add));
    response.setValue(
        "netIgst",
        invoiceLine.stream().map(rate -> rate.getIgst()).reduce(BigDecimal.ZERO, BigDecimal::add));
    response.setValue(
        "netCsgt",
        invoiceLine.stream().map(rate -> rate.getCgst()).reduce(BigDecimal.ZERO, BigDecimal::add));
    response.setValue(
        "netSgst",
        invoiceLine.stream().map(rate -> rate.getSgst()).reduce(BigDecimal.ZERO, BigDecimal::add));
    response.setValue(
        "grossAmount",
        invoiceLine
            .stream()
            .map(rate -> rate.getGrossAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add));
  }

  public void getSequence(ActionRequest request, ActionResponse response) {

    Sequence sequence = request.getContext().asType(Sequence.class);
    String getNextNumber = service.setNextNumber(sequence);
    response.setValue("nextNumber", getNextNumber);
  }

  public void setReferenceParty(ActionRequest request, ActionResponse response) {
    Party party = request.getContext().asType(Party.class);
    String getNextNumber = service.setReferenceParty(party);
    if (getNextNumber != null) response.setValue("reference", getNextNumber);
    else response.addError("reference", "no sequence is specified for the Party");
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

  public void getDynamicImagePath(ActionRequest request, ActionResponse response) {

    AppSettings appSettings = AppSettings.get();
    String uploaddir = appSettings.get("file.upload.dir");
    System.err.println(uploaddir);
    request.getContext().put("setImagePath", uploaddir);
  }

  public void getTotalQtyAndPrice(ActionRequest request, ActionResponse response) {

    Invoice invoice = request.getContext().asType(Invoice.class);

    List<InvoiceLine> invoiceLine = invoice.getInvoiceItemsList();

    request.getContext().put("totalQty", invoiceLine.stream().mapToInt(qty -> qty.getQty()).sum());
    
    System.out.println(invoiceLine.stream().mapToInt(qty -> qty.getQty()).sum() ); 

    request
        .getContext()
        .put(
            "totalPrice",
            invoiceLine.stream().mapToInt(price -> price.getPrice().intValue()).sum());
    System.out.println(invoiceLine.stream().mapToInt(price -> price.getPrice().intValue()).sum());
  }
}
