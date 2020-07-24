package me.adeptr.adepsk.reqn.skript;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Arrays;

import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.reqn.HttpResponse;

public class ExprResponseSpecificValues extends SimpleExpression<String> {

	static {
		PropertyExpression.register(ExprResponseSpecificValues.class, String.class, "%string% [response] header[ value][s]", "httpresponses");
	}

	private Expression<String> key;
	private Expression<HttpResponse> responses;

	@Override
	protected String[] get(Event e) {
		String key = this.key.getSingle(e);

		if (key == null) {
			return null;
		}

		return Arrays.stream(responses.getAll(e)).map(HttpResponse::getHeaders).map(h -> h.get(key))
				.toArray(String[]::new);
	}

	@Override
	public boolean isSingle() {
		return responses.isSingle();
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "specific header values";
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed,
			SkriptParser.ParseResult parseResult) {
		switch (matchedPattern) {
		case 0:
			key = (Expression<String>) exprs[0];
			responses = (Expression<HttpResponse>) exprs[1];
			break;
		case 1:
			responses = (Expression<HttpResponse>) exprs[0];
			key = (Expression<String>) exprs[1];
			break;
		}
		return true;
	}
}