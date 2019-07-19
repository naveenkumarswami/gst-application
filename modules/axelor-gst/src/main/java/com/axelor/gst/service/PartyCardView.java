package com.axelor.gst.service;

import java.util.Map;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.repo.PartyRepository;

public class PartyCardView extends PartyRepository {
  

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
  
}
