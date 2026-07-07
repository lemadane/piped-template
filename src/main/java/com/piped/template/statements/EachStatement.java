package com.piped.template.statements;

public record EachStatement(
      String itemName,
      String keyName,
      String valueName,
      String collectionExpression,
      boolean mapLoop) {
}