package com.axelor.gst.service;

import java.util.Map;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.repo.ProductRepository;

public class ProductCardView extends ProductRepository {
  @Override
  public Map<String, Object> populate(Map<String, Object> json, Map<String, Object> context) {
    if (!context.containsKey("json-enhance")) {
      return json;
    }
    try {
      Long id = (Long) json.get("id");
      Product product = find(id);
      //json.put("address", partner.getAddress().get(0));  // here thoese variable used which used in template tag
      json.put("hasImage", product.getImage() != null);
      json.put("category", product.getCategory());
      
    } catch (Exception e) {
      System.err.println(e.getStackTrace()); 
    }

    return json;
  }
  
  
}
