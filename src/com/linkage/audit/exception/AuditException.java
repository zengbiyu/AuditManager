/**
 * 
 */
package com.linkage.audit.exception;

import java.io.Serializable;


public class AuditException extends Exception implements Serializable {

	private static final long serialVersionUID = 3524864093058746047L;

 
	private String errorCode;
	 
	private String errorMsg;
	
	public AuditException(){
		super();
	}
	
	public AuditException(String a_errorCode,String a_errorMsg){
		this.errorCode = a_errorCode;
		this.errorMsg = a_errorMsg;
	}
	
	public String toString(){
		return "AuditException errorCode: "+errorCode+" -errorMsg: "+errorMsg;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
