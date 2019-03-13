package com.appointment.dinner.exception;


import com.appointment.dinner.CommonConstants.CommonConstants;

/**
* Description : 进行逻辑验证不通过时产生的错误
*/
public class LogicalVerificationException extends BaseException {
	private static final long serialVersionUID = 1L;
	private static final String ERROR_MSG = "逻辑验证错误";

	public LogicalVerificationException() {
		super(LogicalVerificationException.ERROR_MSG, CommonConstants.EX_OTHER_CODE);
	}
	
	public LogicalVerificationException(String message) {
		super(message, CommonConstants.EX_OTHER_CODE);
	}
}

