package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.CompanyRepository;
import com.axelor.gst.db.repo.SequenceRepository;
import com.google.inject.persist.Transactional;

public class InvoiceServiceImpl implements InvoiceService {

  @Inject CompanyRepository companyRepository;
  @Inject SequenceRepository sequenceRepository;

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
  public Company setDefalutComany(Invoice invoice) {

    Company company = companyRepository.all().fetchOne();
    return company;
  }

  @Override
  @Transactional
  public String setReferenceInvoice(Invoice invoice) {
    Sequence sequence = sequenceRepository.all().filter("self.model.name = 'Invoice'").fetchOne();

    if (sequence != null) {

      String prifix = sequence.getPrefix();
      String suffix = sequence.getSuffix();
      String oldnextNumber = sequence.getNextNumber();
      String[] count = oldnextNumber.split(prifix);
      String[] myNumber = count[1].split(suffix);

      if (invoice.getReference() == null) {

        int middlePadding = Integer.parseInt(1 + myNumber[0]) + 1;
        String newNumber = Integer.toString(middlePadding).substring(1);
        String newnextNumber = null;
        newnextNumber = prifix + newNumber + suffix;

        sequence.setNextNumber(newnextNumber);
        sequenceRepository.save(sequence);
        return oldnextNumber;
      } else {

        int middlePadding = Integer.parseInt(1 + myNumber[0]) - 1;
        String newNumber = Integer.toString(middlePadding).substring(1);

        return prifix + newNumber + suffix;
      }
    } else return null;
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
}
