package me.adeptr.adepsk.skriptmirror;

public class JavaCallException extends RuntimeException {
	public JavaCallException(String message) {
		super(message);
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}
