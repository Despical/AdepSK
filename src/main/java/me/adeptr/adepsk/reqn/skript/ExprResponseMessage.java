package me.adeptr.adepsk.reqn.skript;

import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import me.adeptr.adepsk.reqn.HttpResponse;

public class ExprResponseMessage extends SimplePropertyExpression<HttpResponse, String> {

	static {
		PropertyExpression.register(ExprResponseMessage.class, String.class, "[response] [status] (message|reason)[s]", "httpresponses");
	}

	@Override
	protected String getPropertyName() {
		return "status message";
	}

	@Override
	public String convert(HttpResponse httpResponse) {
		return httpResponse.getMessage();
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}
}