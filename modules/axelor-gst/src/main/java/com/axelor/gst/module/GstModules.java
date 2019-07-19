package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.gst.service.GstService;
import com.axelor.gst.service.GstServiceImpl;
import com.axelor.gst.service.PartyCardView;
import com.axelor.gst.service.ProductCardView;

public class GstModules extends AxelorModule {

  @Override
  protected void configure() {
    bind(GstService.class).to(GstServiceImpl.class);
    bind(PartyRepository.class).to(PartyCardView.class);
    bind(ProductRepository.class).to(ProductCardView.class);
    
  }
  
}
