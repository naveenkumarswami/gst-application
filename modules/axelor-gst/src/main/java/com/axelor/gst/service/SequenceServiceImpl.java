package com.axelor.gst.service;


import com.axelor.gst.db.Sequence;

public class SequenceServiceImpl implements SequenceService {

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
