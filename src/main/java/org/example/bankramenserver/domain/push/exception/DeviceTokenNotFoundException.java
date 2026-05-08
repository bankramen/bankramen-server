package org.example.bankramenserver.domain.push.exception;

import org.example.bankramenserver.global.error.exception.ErrorCode;
import org.example.bankramenserver.global.error.exception.GlobalException;

public class DeviceTokenNotFoundException extends GlobalException {

    public static final DeviceTokenNotFoundException EXCEPTION = new DeviceTokenNotFoundException();

    private DeviceTokenNotFoundException() {
        super(ErrorCode.DEVICE_TOKEN_NOT_FOUND);
    }
}