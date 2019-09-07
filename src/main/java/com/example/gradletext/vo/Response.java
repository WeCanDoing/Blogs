/**
 * 
 */
package com.example.gradletext.vo;

/**
 * @author singjumprap
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @date:   2019年5月16日 下午5:01:35  
 */
public class Response {
  private boolean success;//处理是否成功
  private String message;//处理后的消息提示
  private Object body;//返回数据
  
/**
 * @param success
 * @param message
 * @param body
 */
public Response(boolean success, String message, Object body) {
	super();
	this.success = success;
	this.message = message;
	this.body = body;
}
/**
 * @param success
 * @param message
 */
public Response(boolean success, String message) {
	super();
	this.success = success;
	this.message = message;
}
public boolean isSuccess() {
	return success;
}
public void setSuccess(boolean success) {
	this.success = success;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public Object getBody() {
	return body;
}
public void setBody(Object body) {
	this.body = body;
}
	
}
