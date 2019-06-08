package com.kakao.app.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

//KakaoApiController에 에러는 DefaultErrorAttributes클래스로 떨어진다.
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DefaultErrorAttributes implements ErrorAttributes, HandlerExceptionResolver, Ordered {

	private static final String ERROR_ATTRIBUTE = DefaultErrorAttributes.class.getName() + ".ERROR";

	private final boolean includeException;

	/**
	 * Create a new {@link DefaultErrorAttributes} instance that does not include the
	 * "exception" attribute.
	 */
	public DefaultErrorAttributes() {
		this(false);
	}

	/**
	 * Create a new {@link DefaultErrorAttributes} instance.
	 * @param includeException whether to include the "exception" attribute
	 */
	public DefaultErrorAttributes(boolean includeException) {
		this.includeException = includeException;
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		storeErrorAttributes(request, ex);
		return null;
	}

	private void storeErrorAttributes(HttpServletRequest request, Exception ex) {
		request.setAttribute(ERROR_ATTRIBUTE, ex);
	}



	private void addStatus(Map<String, Object> errorAttributes, RequestAttributes requestAttributes) {
		Integer status = getAttribute(requestAttributes, "javax.servlet.error.status_code");
		if (status == null) {
			errorAttributes.put("status", 999);
			errorAttributes.put("error", "None");
			return;
		}
		System.out.println("status : "+status);
		errorAttributes.put("status", status);
		try {
			errorAttributes.put("error", HttpStatus.valueOf(status).getReasonPhrase());
		}
		catch (Exception ex) {
			// Unable to obtain a reason
			System.out.println("statusex : "+status);
			errorAttributes.put("error", "Http Status " + status);
		}
	}

	private void addErrorDetails(Map<String, Object> errorAttributes, RequestAttributes requestAttributes,
			boolean includeStackTrace) {
		Throwable error = getError(requestAttributes);
		if (error != null) {
			while (error instanceof ServletException && error.getCause() != null) {
				error = ((ServletException) error).getCause();
			}
			if (this.includeException) {
				errorAttributes.put("exception", error.getClass().getName());
			}
			addErrorMessage(errorAttributes, error);
			if (includeStackTrace) {
				addStackTrace(errorAttributes, error);
			}
		}
		Object message = getAttribute(requestAttributes, "javax.servlet.error.message");
		if ((!StringUtils.isEmpty(message) || errorAttributes.get("message") == null)
				&& !(error instanceof BindingResult)) {
			errorAttributes.put("message", StringUtils.isEmpty(message) ? "No message available" : message);
		}
	}

	private void addErrorMessage(Map<String, Object> errorAttributes, Throwable error) {
		BindingResult result = extractBindingResult(error);
		if (result == null) {
			
			errorAttributes.put("message", error.getMessage());
			return;
		}
		if (result.hasErrors()) {
			errorAttributes.put("errors", result.getAllErrors());
			errorAttributes.put("message", "Validation failed for object='" + result.getObjectName()
					+ "'. Error count: " + result.getErrorCount());
		}
		else {
			errorAttributes.put("message", "No errors");
		}
	}

	private BindingResult extractBindingResult(Throwable error) {
		if (error instanceof BindingResult) {
			return (BindingResult) error;
		}
		if (error instanceof MethodArgumentNotValidException) {
			return ((MethodArgumentNotValidException) error).getBindingResult();
		}
		return null;
	}

	private void addStackTrace(Map<String, Object> errorAttributes, Throwable error) {
		StringWriter stackTrace = new StringWriter();
		error.printStackTrace(new PrintWriter(stackTrace));
		stackTrace.flush();
		errorAttributes.put("trace", stackTrace.toString());
	}

	private void addPath(Map<String, Object> errorAttributes, RequestAttributes requestAttributes) {
		String path = getAttribute(requestAttributes, "javax.servlet.error.request_uri");
		if (path != null) {
			System.out.println("path :"+path);
			errorAttributes.put("path", path);
		}
	}


	@SuppressWarnings("unchecked")
	private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
		return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
	}

	@Override
	public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
		Map<String, Object> errorAttributes = new LinkedHashMap<>();
		//errorAttributes.put("timestamp", new Date());
		addStatus(errorAttributes, requestAttributes);
		//addErrorDetails(errorAttributes, requestAttributes, includeStackTrace);
		//addPath(errorAttributes, requestAttributes);
		return errorAttributes;

	}

	@Override
	public Throwable getError(RequestAttributes requestAttributes) {
		Throwable exception = getAttribute(requestAttributes, ERROR_ATTRIBUTE);
		if (exception == null) {
			exception = getAttribute(requestAttributes, "javax.servlet.error.exception");
		}
		return exception;
	}

}