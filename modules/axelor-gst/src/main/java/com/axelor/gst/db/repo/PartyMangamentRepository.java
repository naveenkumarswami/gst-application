package com.axelor.gst.db.repo;

import java.util.Map;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.service.SequenceService;

public class PartyMangamentRepository extends PartyRepository {

  @Inject SequenceService service;

  @Override
  public Map<String, Object> populate(Map<String, Object> json, Map<String, Object> context) {
    if (!context.containsKey("json-enhance")) {
      return json;
    }
    try {
      Long id = (Long) json.get("id");
      Party party = find(id);
      json.put("address", party.getAddressList().get(0));
      json.put("contact", party.getContactList().get(0));

    } catch (Exception e) {
      System.err.println(e.getStackTrace());
    }
    return json;
  }

  @Override
  public Party save(Party party) {
    
    if (party.getReference() == null) {
      String getNextNumber = service.setReference(Party.class.getSimpleName());
      if (getNextNumber != null) {
        party.setReference(getNextNumber);
      } else {
        throw new PersistenceException("no sequence is specified for the Party");
      }
    }
    return super.save(party);
  }
}
