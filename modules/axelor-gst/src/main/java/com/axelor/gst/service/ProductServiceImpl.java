package com.axelor.gst.service;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.repo.InvoiceLineRepository;
import com.axelor.gst.db.repo.ProductRepository;
import com.google.inject.persist.Transactional;

public class ProductServiceImpl implements ProductService {
  
  @Inject ProductRepository productRepository;
  @Inject InvoiceLineRepository invoiceLineRepository;
  
  @Override
  @Transactional
  public List<InvoiceLine> putSelectedProduct(List<Integer> productIds) {
    
    List<InvoiceLine> invoiceLineList = new ArrayList<>();
    
    for(Integer id : productIds)
    {
      Product product = productRepository.find(id.longValue());
      InvoiceLine invoiceLine = new InvoiceLine();
      invoiceLine.setProduct(product);
      invoiceLine.setItem("["+product.getCode()+"]"+product.getName());
      invoiceLine.setGstRate(product.getGstRate());
      invoiceLine.setPrice(product.getSalePrice());
      invoiceLineRepository.save(invoiceLine);
      invoiceLineList.add(invoiceLine);
    }
        
    return invoiceLineList;
  }
  
}
