package com.exatask.platform.api.utilities;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.tags.Tag;

import java.util.List;

public interface ApiSwaggerUtility {

  Components getComponents();

  Contact getContact();

  List<Tag> getTags();
}
