package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.State;
import com.axelor.gst.db.repo.InvoiceLineRepository;
import com.axelor.gst.db.repo.ProductRepository;
import com.google.inject.persist.Transactional;

public class InvoiceLineServiceImpl implements InvoiceLineService {
  
  @Inject ProductRepository productRepository;
  @Inject InvoiceLineRepository invoiceLineRepository;

  @Override
  public Invoice getIgstAndSgstAndCgst(Invoice invoice, InvoiceLine invoiceLine) {
    try {
      State companyState = invoice.getCompany().getAddress().getState();
      State invoiceState = invoice.getInvoiceAddress().getState();
      BigDecimal netAmount =
          (new BigDecimal(invoiceLine.getQty())).multiply(invoiceLine.getPrice());
      invoice.setNetAmount(netAmount);
      BigDecimal gstRate = invoiceLine.getGstRate();
      BigDecimal sgst = BigDecimal.ZERO, igst = BigDecimal.ZERO;

      if (!invoiceState.equals(companyState)) {
        igst = netAmount.multiply(gstRate).divide(new BigDecimal(100));
        invoice.setNetIgst(igst);
        invoice.setNetSgst(BigDecimal.ZERO);
      } else if (invoiceState.equals(companyState)) {
        sgst = netAmount.multiply(gstRate.divide(new BigDecimal(2)).divide(new BigDecimal(100)));
        invoice.setNetIgst(BigDecimal.ZERO);
        invoice.setNetSgst(sgst);
      } else {
        invoice.setNetIgst(BigDecimal.ZERO);
        invoice.setNetSgst(BigDecimal.ZERO);
      }
      invoice.setGrossAmount(netAmount.add(igst).add(sgst).add(sgst));

    } catch (Exception e) {
      e.printStackTrace();
    }
    return invoice;
  }
  
  @Override
  @Transactional
  public List<InvoiceLine> putSelectedProduct(List<Integer> productIds) {

    if (productIds == null) return null;

    List<InvoiceLine> invoiceLineList = new ArrayList<>();
    for (Integer id : productIds) {
      Product product = productRepository.find(id.longValue());
      InvoiceLine invoiceLine = new InvoiceLine();
      invoiceLine.setProduct(product);
      invoiceLine.setItem("[" + product.getCode() + "]" + product.getName());
      invoiceLine.setGstRate(product.getGstRate());
      invoiceLine.setPrice(product.getSalePrice());
      invoiceLineRepository.save(invoiceLine);
      invoiceLineList.add(invoiceLine);
    }

    return invoiceLineList;
  }
  
}
