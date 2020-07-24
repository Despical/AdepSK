package me.adeptr.adepsk.reqn.skript;

import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import me.adeptr.adepsk.reqn.HttpResponse;

public class ExprResponseStatus extends SimplePropertyExpression<HttpResponse, String> {

	static {
		PropertyExpression.register(ExprResponseStatus.class, String.class, "[response] status[(es| line[s])]", "httpresponses");
	}

	@Override
	protected String getPropertyName() {
		return "status";
	}

	@Override
	public String convert(HttpResponse httpResponse) {
		return httpResponse.getStatusLine();
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}
}
