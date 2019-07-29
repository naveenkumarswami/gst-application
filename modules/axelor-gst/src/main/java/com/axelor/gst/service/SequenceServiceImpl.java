package com.axelor.gst.service;


import javax.inject.Inject;
import com.axelor.db.Model;
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

  @Override
  @Transactional
  public String setReference(String modelName , String Reference) {

    Sequence sequence = sequenceRepository.all().filter("self.model.name = ?1",modelName).fetchOne();

    if (sequence != null) {

      String prifix = sequence.getPrefix();
      String suffix = sequence.getSuffix();
      String oldnextNumber = sequence.getNextNumber();
      String[] count = oldnextNumber.split(prifix);
      String[] myNumber = count[1].split(suffix);

      if (Reference == null) {

        int middlePadding = Integer.parseInt(1 + myNumber[0]) + 1;
        String newNumber = Integer.toString(middlePadding).substring(1);
        String newnextNumber = null;
        newnextNumber = prifix + newNumber + suffix;

        sequence.setNextNumber(newnextNumber);
        sequenceRepository.save(sequence);
        return oldnextNumber;
      } else {
        
        int middlePadding = Integer.parseInt(1 + myNumber[0]) - 1;
        String newNumber = Integer.toString(middlePadding).substring(1);
        return prifix+newNumber+suffix;
      }
    } else return null;
  }
  
  
}
