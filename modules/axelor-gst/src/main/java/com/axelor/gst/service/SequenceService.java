package com.axelor.gst.service;

import com.axelor.gst.db.Sequence;

public interface SequenceService {

  public String setNextNumber(Sequence sequence);
  public String setReference(String modelName);
}
