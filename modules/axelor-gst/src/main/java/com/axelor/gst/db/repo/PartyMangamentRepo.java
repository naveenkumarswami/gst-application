package com.axelor.gst.db.repo;

import static org.junit.Assert.fail;
import static org.junit.Assume.assumeNoException;
import java.util.Map;
import javax.inject.Inject;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.service.SequenceService;

public class PartyMangamentRepo extends PartyRepository {
  
  @Inject SequenceService service;

  @Override
  public Map<String, Object> populate(Map<String, Object> json, Map<String, Object> context) {
    if (!context.containsKey("json-enhance")) {
      return json;
    }
    try {
      Long id = (Long) json.get("id");
      Party party = find(id);
      //json.put("address", partner.getAddress().get(0));  // here thoese variable used which used in template tag
      json.put("address", party.getAddressList().get(0));
      json.put("contact", party.getContactList().get(0));
      
    } catch (Exception e) {
      System.err.println(e.getStackTrace()); 
    }

    return json;
  }
  
  @Override
  public Party save(Party party){
    
    System.out.println("hello" ); 
    String getNextNumber = service.setReference("Party", party.getReference());

    if (getNextNumber != null) 
    {
      party.setReference(getNextNumber);
    }
    else 
    {  
      throw new IllegalMonitorStateException("no sequence is specified for the Party");
    }
    return super.save(party);
  }
}
