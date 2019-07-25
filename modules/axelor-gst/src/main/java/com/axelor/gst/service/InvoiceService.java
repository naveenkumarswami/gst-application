package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.Map;
import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;

public interface InvoiceService {
  
  public Company setDefalutComany(Invoice invoice);
  public Contact getContact(Invoice invoice);
  public Address getInvoiceAddress(Invoice invoice);
  public Address getShippingAddress(Invoice invoice);
  public String setReferenceInvoice(Invoice invoice);
  public Map<Integer, BigDecimal> calculateRates(Invoice invoice);
  
}
