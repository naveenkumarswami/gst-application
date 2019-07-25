package com.axelor.gst.service;

import javax.inject.Inject;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.google.inject.persist.Transactional;

public class PartyServiceImpl implements PartyService {
  
  @Inject SequenceRepository sequenceRepository;
  
  @Override
  @Transactional
  public String setReferenceParty(Party party) {

    Sequence sequence = sequenceRepository.all().filter("self.model.name = 'Party'").fetchOne();

    if (sequence != null) {

      String prifix = sequence.getPrefix();
      String suffix = sequence.getSuffix();
      String oldnextNumber = sequence.getNextNumber();
      String[] count = oldnextNumber.split(prifix);
      String[] myNumber = count[1].split(suffix);

      if (party.getReference() == null) {

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
