package com.axelor.gst.service;

import java.math.BigDecimal;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.State;
import com.axelor.gst.db.repo.CompanyRepository;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.db.repo.SequenceRepository;
import com.google.inject.persist.Transactional;

public class GstServiceImpl implements GstService {

  @Inject EntityManager em;

  @Inject SequenceRepository sequenceRepository;
  @Inject PartyRepository partyRepository;

  @Override
  @Transactional
  public Contact getContact(Invoice invoice) {

    String partyName = invoice.getParty().getName();

    Contact contact = new Contact();

    /* Query<Party> contact =
    new PartyRepository()
        .all()
        .filter(
            "left join Contact contact on self.contactList = contact."
                + " self.ContactList='"
                + partyName
                + "'");*/

    /* Query query = em.createNativeQuery("select contact from gst_party as party "
    + "left join gst_contact as contact on contact.id ="
    + "any (select cl.contact_list from gst_party_contact_list as cl where cl.gst_party = contact.id)"
    + " where party.name='party1'");*/

    Query query =
        em.createQuery(
            "select contact from Party party left join party.contactList contact where party.name='"
                + partyName
                + "' and contact.type='Primary'");

    //    System.out.println(query1.getSingleResult());

    //       query.getResultList();

    // System.err.println(Beans.get(ContactRepository.class).all().filter("self.type='Primary'").fetch());

    // System.out.println(contactList);
    //     System.err.println(contact);
    //    System.err.println(Arrays.asList(query1.getResultList().subList(0, 1)));

    try {
      //    List<Contact> result = query1.getResultList();
      contact = (Contact) query.getResultList().get(0);

      /*for(Contact c : result)
      {
        contact = c;
        break;
      }*/

      //    System.out.println(result);

      return contact;
    } catch (Exception e) {
      System.err.println("null invoice address");
    }
    return null;
  }

  @Override
  @Transactional
  public Address getInvoiceAddress(Invoice invoice) {

    Address address = new Address();
    String partyName = invoice.getParty().getName();

    Query query =
        em.createQuery(
            "select address from Party party left join party.addressList address where party.name='"
                + partyName
                + "' and (address.type='default' or address.type = 'invoice')");

    try {
      address = (Address) query.getResultList().get(0);
      return address;

    } catch (IndexOutOfBoundsException e) {
      System.err.println("null shipping address");
      return null;
    }
  }

  @Override
  @Transactional
  public Address getShippingAddress(Invoice invoice) throws IndexOutOfBoundsException {
    Address address = new Address();
    String partyName = invoice.getParty().getName();
    Boolean isUseInvoiceAddressAsShippingAddress = invoice.getIsUseInvocieAddressAsShipping();

    Query query =
        em.createQuery(
            "select address from Party party left join party.addressList address where party.name='"
                + partyName
                + "' and (address.type='default' or address.type = 'shipping')");

    try {
      if (isUseInvoiceAddressAsShippingAddress == true) {
        address = (Address) query.getResultList().get(0);
        return address;
      } else return null;

    } catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public BigDecimal getIGST(Invoice invocie, InvoiceLine invoiceLine) {

    try {
      State companyState = invocie.getCompany().getAddress().getState();
      System.out.println(companyState);
      State invoiceState = invocie.getInvoiceAddress().getState();
      System.out.println(invoiceState);
      BigDecimal netAmount = invoiceLine.getNetAmount();
      BigDecimal gstRate = invoiceLine.getGstRate();
      BigDecimal igst = new BigDecimal(0);

      if (invoiceState.equals(companyState)) {
        igst = netAmount.multiply(gstRate);
        System.err.println(igst);
        return igst;
      }
    } catch (Exception e) {

      e.printStackTrace();
    }
    return new BigDecimal(0);
  }

  @Override
  public BigDecimal getSGSTandCGST(Invoice invocie, InvoiceLine invoiceLine) {

    try {
      State companyState = invocie.getCompany().getAddress().getState();
      State invoiceState = invocie.getInvoiceAddress().getState();
      BigDecimal netAmount = invoiceLine.getNetAmount();
      BigDecimal gstRate = invoiceLine.getGstRate();

      BigDecimal sgst = new BigDecimal(0);
      BigDecimal divideByTwo = new BigDecimal(2);

      if (invoiceState.equals(companyState)) {
        sgst = netAmount.multiply(gstRate.divide(divideByTwo));
        System.err.println(sgst);
        return sgst;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new BigDecimal(0);
  }

  @Override
  public Company setDefalutComany(Invoice invoice) {

    Company company = new CompanyRepository().all().fetchOne();
    System.out.println(company);

    return company;
  }

  @Override
  public String setNextNumber(Sequence sequence) {

    String prifix = sequence.getPrefix();
    String suffix = sequence.getSuffix();
    String nextNumber = null;
    int padding = sequence.getPadding();
    String temp = "0";

    for (int i = 2; i <= padding; i++) {
      temp = temp + "0";
    }
    nextNumber = prifix + temp + suffix;
    return nextNumber;
  }

  @Override
  @Transactional
  public String setReferenceParty(Party party) {

    Sequence sequence = sequenceRepository.all().filter("self.model.name = 'Party'").fetchOne();
    //    Query query =
    //        em.createQuery("select sequence from Sequence sequence where
    // sequence.model.name='Party'");

    //    Sequence sequence = (Sequence) query.getResultList().get(0);

    if (sequence != null) {

      String prifix = sequence.getPrefix();
      String suffix = sequence.getSuffix();
      String oldnextNumber = sequence.getNextNumber();
      String[] count = oldnextNumber.split(prifix);
      String[] myNumber = count[1].split(suffix);

      if (party.getReference() == null) {

        int middlePadding = Integer.parseInt(1 + myNumber[0]) + 1;
        String newNumber = Integer.toString(middlePadding).substring(1);
        System.out.println(newNumber);

        String newnextNumber = null;
        newnextNumber = prifix + newNumber + suffix;

        sequence.setNextNumber(newnextNumber);
        sequenceRepository.save(sequence);
        return oldnextNumber;
      } else {
        
        int middlePadding = Integer.parseInt(1 + myNumber[0]) - 1;
        String newNumber = Integer.toString(middlePadding).substring(1);
       
        return prifix+newNumber+suffix;
      }
    } else return null;
  }
}
