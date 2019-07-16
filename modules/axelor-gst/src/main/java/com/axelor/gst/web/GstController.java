package com.axelor.gst.web;

import java.util.List;
import com.axelor.gst.db.Product;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.rpc.Context;

public class GstController {
  
  public void createInvoice(ActionRequest request, ActionResponse response) {

    if (request.getContext().get("_ids") == null) {
      return;
    }

    List<Long> requestIds = (List<Long>)request.getContext().get("_ids");
    
    String totalIdSelect = requestIds.toString();

    System.out.println(requestIds);
    
    request.getContext().put("totalProduct", totalIdSelect);
    
  }
  
}
