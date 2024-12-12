package com.cf.cfteam.exceptions.security;

public class InvalidTwoFactorCodeException extends RuntimeException {
  public InvalidTwoFactorCodeException() {
    super("two-factor.code_invalid");
  }
}