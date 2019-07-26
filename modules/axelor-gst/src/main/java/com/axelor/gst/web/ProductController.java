package com.axelor.gst.web;

import java.util.List;
import javax.inject.Inject;
import com.axelor.app.AppSettings;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.service.ProductService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class ProductController {
  
  @Inject ProductService service;
  
  @SuppressWarnings("unchecked")
  public void getSelectedProduct(ActionRequest request, ActionResponse response) {
   response.setCanClose(true);
    if (request.getContext().get("_ids") == null) {
      return;
    }

    List<Integer> requestIds = (List<Integer>) request.getContext().get("_ids");
    List<InvoiceLine> invoiceLineList =service.putSelectedProduct(requestIds);
    request.getContext().put("totalSelectedProduct", invoiceLineList);
  }
  
  public void getDynamicImagePath(ActionRequest request, ActionResponse response) {

    AppSettings appSettings = AppSettings.get();
    String uploaddir = appSettings.get("file.upload.dir");
    System.err.println(uploaddir);
    request.getContext().put("setImagePath", uploaddir);
  }
  
  @SuppressWarnings("unchecked")
  public void createInvoice(ActionRequest request, ActionResponse response) {

    if (request.getContext().get("_ids") == null) {
      response.setError("No Product Found!!");
      return;
    }

    List<Integer> requestIds = (List<Integer>) request.getContext().get("_ids");
    String totalIdSelect = requestIds.toString();
    totalIdSelect = totalIdSelect.substring(1, totalIdSelect.length() - 1);
    System.out.println(totalIdSelect);
    request.getContext().put("totalProduct", totalIdSelect);
  }
  
}
