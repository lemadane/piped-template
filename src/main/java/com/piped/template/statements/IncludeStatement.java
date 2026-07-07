package com.piped.template.statements;

public record IncludeStatement(
      String templateName,
      String contextExpression,
      boolean hasContextExpression) {
}