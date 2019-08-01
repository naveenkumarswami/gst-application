package com.axelor.gst.service;

import java.util.List;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;

public interface InvoiceService {

  public Company setDefaultCompany(Invoice invoice);
  public Contact getContact(Invoice invoice);
  public Address getInvoiceAddress(Invoice invoice);
  public Address getShippingAddress(Invoice invoice);
  public Invoice calculateRates(Invoice invoice);
  public List<InvoiceLine> getInvoiceLineList(Invoice invoice);
}
