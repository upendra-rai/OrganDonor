package com.in.pathshala.donarblood.dto;

public class ResponseDto {

	private String status;
	private String message;
	
	public ResponseDto() {}

	public ResponseDto(String status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ResponseDto [status=" + status + ", message=" + message + "]";
	}
	
}

