package org.example.bankramenserver.domain.user.exception;

import org.example.bankramenserver.domain.auth.exception.MissingTokenException;
import org.example.bankramenserver.global.error.exception.ErrorCode;
import org.example.bankramenserver.global.error.exception.GlobalException;

public class UserInfoNotFound extends GlobalException {

    public static final GlobalException EXCEPTION = new MissingTokenException();

    public UserInfoNotFound() { super(ErrorCode.USER_INFO_NOT_FOUND); }
}
