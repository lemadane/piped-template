package com.piped.template.exceptions;

public class TemplateRenderException extends RuntimeException {
   public TemplateRenderException(String message) {
      super(message);
   }

   public TemplateRenderException(String message, Throwable cause) {
      super(message, cause);
   }
   
}
