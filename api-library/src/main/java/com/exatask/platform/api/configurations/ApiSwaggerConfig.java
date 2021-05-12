package com.exatask.platform.api.configurations;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.tags.Tag;

import java.util.List;

public interface ApiSwaggerConfig {

  Components getComponents();

  Contact getContact();

  List<Tag> getTags();
}
