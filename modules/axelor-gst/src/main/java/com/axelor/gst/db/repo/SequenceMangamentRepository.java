package com.axelor.gst.db.repo;

import javax.inject.Inject;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.service.SequenceService;

public class SequenceMangamentRepository extends SequenceRepository {

  @Inject SequenceService service;

  @Override
  public Sequence save(Sequence entity) {

    if (entity.getNextNumber() == null) {
      String getNextNumber = service.setNextNumber(entity);
      entity.setNextNumber(getNextNumber);
    }
    return super.save(entity);
  }
}
