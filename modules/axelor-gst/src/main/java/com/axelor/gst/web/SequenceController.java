package com.axelor.gst.web;

import javax.inject.Inject;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.service.SequenceService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;


public class SequenceController {
  
  @Inject SequenceService service;
  
  public void getSequence(ActionRequest request, ActionResponse response) {
    Sequence sequence = request.getContext().asType(Sequence.class);
    String getNextNumber = service.setNextNumber(sequence);
    response.setValue("nextNumber", getNextNumber);
  }
  
}
