package com.axelor.gst.web;

import java.util.List;
import javax.inject.Inject;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.service.ProductService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class ProductController {
  
  @Inject ProductService service;
  
  @SuppressWarnings("unchecked")
  public void getSelectedProduct(ActionRequest request, ActionResponse response) {

    if (request.getContext().get("_ids") == null) {
      return;
    }


    List<Integer> requestIds = (List<Integer>) request.getContext().get("_ids");
    
    List<InvoiceLine> invoiceLineList =service.putSelectedProduct(requestIds);
    
    request.getContext().put("totalSelectedProduct", invoiceLineList);
  }
  
}
