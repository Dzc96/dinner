package com.appointment.dinner.handle;


import com.appointment.dinner.exception.BaseException;
import com.appointment.dinner.message.R;
import com.sun.media.sound.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * 全局异常处理类
 * @author feng.zhe.lin
 */
@ControllerAdvice("com.appointment.dinner")
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

	private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	/**
	 * 自定义错误统一处理
	 * 程序中的自定义错误都应该继承BaseException类
	 * @param ex
	 * @return
	 */
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(BaseException.class)
	public R<?> baseExceptionHandler(BaseException ex) {
		log.error(ex.getMessage(), ex);
		return new R<>(ex);
	}

	/**
	 * 数据类型不匹配统一处理
	 * @param ex
	 * @return
	 */
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(InvalidFormatException.class)
	public R<?> invalidFormatExceptionHandler(InvalidFormatException ex) {
		log.error(ex.getMessage(), ex);
		return new R<>(ex);
	}

	/**
	 * http参数解析错误统一处理
	 * @param ex
	 * @return
	 */
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public R<?> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex) {
		StringBuilder errorMsg = new StringBuilder();
		errorMsg.append("数据格式不匹配:");
		errorMsg.append(ex.getCause().getMessage());
		log.error(ex.getMessage(), ex);
		R<Object> r = new R<>();
		r.setCode(R.FAIL);
		r.setMsg(errorMsg.toString());
		return r;
	}

	/**
	 * 数据验证不通过统一处理
	 * @param ex
	 * @return
	 */
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public R<?> methodArgumentNotValidExcetionHandler(MethodArgumentNotValidException ex) {
		StringBuilder errorMsg = new StringBuilder();
		errorMsg.append("数据验证失败:");
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {

			errorMsg.append(error.getDefaultMessage());
			errorMsg.append(";");
		}
		log.error(errorMsg.toString(), ex);
		R<Object> r = new R<>();
		r.setCode(R.FAIL);
		r.setMsg(errorMsg.toString());
		return r;
	}

	/**
	 * 其他错误统一处理
	 * @param ex
	 * @return
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public R<?> otherExceptionHandler(Exception ex) {
		log.error(ex.getMessage(), ex);
		return new R<>(new Exception("unexpected error"));
	}
}