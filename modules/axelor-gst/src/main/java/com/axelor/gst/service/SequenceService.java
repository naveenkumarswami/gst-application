package com.axelor.gst.service;

import com.axelor.db.Model;
import com.axelor.gst.db.Sequence;

public interface SequenceService {
  
  public String setNextNumber(Sequence sequence);
  public String setReference(String modelName , String Reference);
 
}
