package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.repo.CompanyRepository;
import com.axelor.gst.db.repo.SequenceRepository;
import com.google.inject.persist.Transactional;

public class InvoiceServiceImpl implements InvoiceService {

  @Inject CompanyRepository companyRepository;
  @Inject SequenceRepository sequenceRepository;
  @Inject InvoiceLineService invoiceLineService;

  @Override
  @Transactional
  public Contact getContact(Invoice invoice) {
    try {
      Contact contact =
          invoice
              .getParty()
              .getContactList()
              .stream()
              .filter(a -> a.getType().equals("Primary"))
              .findFirst()
              .get();
      return contact;
    } catch (Exception e) {
      System.err.println("null contact");
    }
    return null;
  }

  @Override
  @Transactional
  public Address getInvoiceAddress(Invoice invoice) {

    try {
      Address address =
          invoice
              .getParty()
              .getAddressList()
              .stream()
              .filter(a -> a.getType().equals("default") || a.getType().equals("invoice"))
              .findFirst()
              .get();
      return address;

    } catch (Exception e) {
      System.err.println("null invoice address");
      return null;
    }
  }

  @Override
  @Transactional
  public Address getShippingAddress(Invoice invoice) {
    try {
      Address address =
          invoice
              .getParty()
              .getAddressList()
              .stream()
              .filter(a -> a.getType().equals("default") || a.getType().equals("shipping"))
              .findFirst()
              .get();
      return address;

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Company setDefaultCompany(Invoice invoice) {

    Company company = companyRepository.all().fetchOne();
    return company;
  }

  @Override
  public Map<Integer, BigDecimal> calculateRates(Invoice invoice) {

    List<InvoiceLine> invoiceLine = invoice.getInvoiceItemsList();
    Map<Integer, BigDecimal> rateSet = new HashMap<>();

    BigDecimal netAmount, igst, cgst, sgst, grossAmount;

    netAmount =
        invoiceLine
            .stream()
            .map(rate -> rate.getNetAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    igst =
        invoiceLine.stream().map(rate -> rate.getIgst()).reduce(BigDecimal.ZERO, BigDecimal::add);

    cgst =
        invoiceLine.stream().map(rate -> rate.getCgst()).reduce(BigDecimal.ZERO, BigDecimal::add);
    sgst =
        invoiceLine.stream().map(rate -> rate.getSgst()).reduce(BigDecimal.ZERO, BigDecimal::add);
    grossAmount =
        invoiceLine
            .stream()
            .map(rate -> rate.getGrossAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    rateSet.put(1, netAmount);
    rateSet.put(2, igst);
    rateSet.put(3, cgst);
    rateSet.put(4, sgst);
    rateSet.put(5, grossAmount);

    return rateSet;
  }

  @Override
  public List<InvoiceLine> getInvoiceLineList(Invoice invoice) {
    
    List<InvoiceLine> invoiceLineList = invoice.getInvoiceItemsList();
    
    List<InvoiceLine> updateInvoiceLineList = new ArrayList<InvoiceLine>();
    
    for(InvoiceLine line : invoiceLineList)
    {
      Map<Integer, BigDecimal> allRate =invoiceLineService.getIgstAndSgstAndCgst(invoice, line);
       line.setNetAmount(allRate.get(3));
       line.setIgst(allRate.get(1));
       line.setSgst(allRate.get(2));
       line.setCgst(allRate.get(2));
       line.setGrossAmount(allRate.get(4));
       updateInvoiceLineList.add(line);
    }
       
    return updateInvoiceLineList;
  }
}
