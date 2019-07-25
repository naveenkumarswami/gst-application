package com.axelor.gst.service;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;

public interface SequenceService {
  
  public String setNextNumber(Sequence sequence);
 
}
