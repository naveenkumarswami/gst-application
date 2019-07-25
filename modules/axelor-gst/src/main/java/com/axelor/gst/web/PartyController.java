package com.axelor.gst.web;

import javax.inject.Inject;
import com.axelor.gst.db.Party;
import com.axelor.gst.service.PartyService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class PartyController {
  
  @Inject PartyService service;
  
  public void setReferenceParty(ActionRequest request, ActionResponse response) {
    Party party = request.getContext().asType(Party.class);
    String getNextNumber = service.setReferenceParty(party);
    if (getNextNumber != null) response.setValue("reference", getNextNumber);
    else response.addError("reference", "no sequence is specified for the Party");
  }
  
}
