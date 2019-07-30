package com.axelor.gst.service;

import javax.inject.Inject;
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
    if (suffix == null) nextNumber = prifix + temp;
    else nextNumber = prifix + temp + suffix;
    return nextNumber;
  }

  @Override
  @Transactional
  public String setReference(String modelName) {

    Sequence sequence =
        sequenceRepository.all().filter("self.model.name = ?1", modelName).fetchOne();

    if (sequence != null) {

      String prifix = sequence.getPrefix();
      String suffix = sequence.getSuffix();
      if (suffix == null) suffix = "test";
      String oldnextNumber = sequence.getNextNumber();
      String[] count = oldnextNumber.split(prifix);
      String[] myNumber = count[1].split(suffix);

      int middlePadding = Integer.parseInt(1 + myNumber[0]) + 1;
      String newNumber = Integer.toString(middlePadding).substring(1);
      String newnextNumber = null;
      if (suffix == "test") newnextNumber = prifix + newNumber;
      else newnextNumber = prifix + newNumber + suffix;

      sequence.setNextNumber(newnextNumber);
      sequenceRepository.save(sequence);
      return oldnextNumber;

    } else return null;
  }
}
