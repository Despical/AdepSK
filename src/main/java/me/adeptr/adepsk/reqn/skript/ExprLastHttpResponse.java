package me.adeptr.adepsk.reqn.skript;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.reqn.HttpResponse;

public class ExprLastHttpResponse extends SimpleExpression<HttpResponse> {

	static {
		Skript.registerExpression(ExprLastHttpResponse.class, HttpResponse.class, ExpressionType.SIMPLE, "[the] [last[ly]] [received] [http] [web] response");
	}

	@Override
	protected HttpResponse[] get(Event e) {
		if (EffRequest.lastResponse == null) {
			return new HttpResponse[0];
		}
		return new HttpResponse[] { EffRequest.lastResponse };
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends HttpResponse> getReturnType() {
		return HttpResponse.class;
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "last received http response";
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed,
			SkriptParser.ParseResult parseResult) {
		return true;
	}
}