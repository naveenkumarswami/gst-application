package com.axelor.gst.service;

import java.math.BigDecimal;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;

public interface GstService {
  
  public Contact getContact(Invoice invoice);
  public Address getInvoiceAddress(Invoice invoice);
  public Address getShippingAddress(Invoice invoice);
  public BigDecimal getIGST(Invoice invocie , InvoiceLine invoiceLine); 
  public BigDecimal getSGSTandCGST (Invoice invocie , InvoiceLine invoiceLine);
  public Company setDefalutComany(Invoice invoice);
  public String setNextNumber(Sequence sequence);
  public String setReferenceParty(Party party);
  public String setReferenceInvoice(Invoice invoice);
  
}
