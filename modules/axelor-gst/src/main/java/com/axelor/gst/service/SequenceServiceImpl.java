package com.axelor.gst.service;

import javax.inject.Inject;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.google.inject.persist.Transactional;

public class SequenceServiceImpl implements SequenceService {
  
  @Inject SequenceRepository sequenceRepository;

  @Override
  public String setNextNumber(Sequence sequence) {

    String prifix = sequence.getPrefix();
    String suffix = sequence.getSuffix();
    String nextNumber = null;
    int padding = sequence.getPadding();
    String temp = "0";

    for (int i = 2; i <= padding; i++) {
      temp = temp + "0";
    }
    nextNumber = prifix + temp + suffix;
    return nextNumber;
  }  
  
}
