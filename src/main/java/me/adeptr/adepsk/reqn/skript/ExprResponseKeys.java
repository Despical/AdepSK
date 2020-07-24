package me.adeptr.adepsk.reqn.skript;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.reqn.HttpResponse;

public class ExprResponseKeys extends SimpleExpression<String> {

	static {
		PropertyExpression.register(ExprResponseKeys.class, String.class, "[response] header key[s]", "httpresponses");
	}

	private Expression<HttpResponse> responses;

	@Override
	protected String[] get(Event e) {
		return Arrays.stream(responses.getAll(e)).map(HttpResponse::getHeaders).map(Map::entrySet).flatMap(Collection::stream).map(Map.Entry::getKey).toArray(String[]::new);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "header keys";
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed,
			SkriptParser.ParseResult parseResult) {
		responses = (Expression<HttpResponse>) exprs[0];
		return true;
	}
}