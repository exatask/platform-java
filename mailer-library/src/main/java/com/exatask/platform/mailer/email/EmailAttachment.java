package com.exatask.platform.mailer.email;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.File;
import java.net.URL;

@Data
@Builder
@Accessors(chain = true)
public class EmailAttachment {

  private String fileName;

  private File file;

  private URL url;
}
