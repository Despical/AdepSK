package me.adeptr.adepsk.reqn.skript;

import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import me.adeptr.adepsk.reqn.HttpResponse;

public class ExprResponseCode extends SimplePropertyExpression<HttpResponse, Integer> {

	static {
		PropertyExpression.register(ExprResponseCode.class, Integer.class, "[response] [status] code[s]",
				"httpresponses");
	}

	@Override
	protected String getPropertyName() {
		return "status code";
	}

	@Override
	public Integer convert(HttpResponse httpResponse) {
		return httpResponse.getCode();
	}

	@Override
	public Class<? extends Integer> getReturnType() {
		return Integer.class;
	}
}