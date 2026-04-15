package com.yesmind.stok.core.domain.data;

import lombok.Getter;

@Getter
public enum ReferenceTools {

  PRODUCT("PRD", "T", 2025),
  STATION("STD", "T", 2025),
  CLIENT("CLT", "E", 2025);
  private String prefix;
  private String suffix;
  private int incrementFirstYear;

  ReferenceTools(String prefix, String suffix, int incrementer) {
    this.prefix = prefix;
    this.suffix = suffix;
    this.incrementFirstYear = incrementer;
  }
}
