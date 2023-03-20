package com.exatask.platform.api.services;

import com.exatask.platform.dto.requests.AppRequest;
import com.exatask.platform.dto.requests.ListRequest;
import com.exatask.platform.dto.responses.AppResponse;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.ArrayList;
import java.util.List;

public abstract class AppService<I extends AppRequest, O extends AppResponse> {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  public abstract O process(I request);

  protected List<Link> getLinks(ListRequest request, long count, Object invocationMethod) {

    int page = request.getPage();
    long totalPages = count / request.getLimit();
    List<Link> links = new ArrayList<>();

    if (page < totalPages) {

      request.setPage(page + 1);
      Link next = WebMvcLinkBuilder.linkTo(invocationMethod)
          .withRel(IanaLinkRelations.NEXT);
      links.add(next);

      request.setPage((int) totalPages);
      Link last = WebMvcLinkBuilder.linkTo(invocationMethod)
          .withRel(IanaLinkRelations.LAST);
      links.add(last);
    }

    if (page > 1) {

      request.setPage(page - 1);
      Link prev = WebMvcLinkBuilder.linkTo(invocationMethod)
          .withRel(IanaLinkRelations.PREV);
      links.add(prev);

      request.setPage(1);
      Link first = WebMvcLinkBuilder.linkTo(invocationMethod)
          .withRel(IanaLinkRelations.FIRST);
      links.add(first);
    }

    return links;
  }
}
