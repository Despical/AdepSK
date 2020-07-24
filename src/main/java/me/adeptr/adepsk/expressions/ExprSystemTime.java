package me.adeptr.adepsk.expressions;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.util.Registry;

public class ExprSystemTime extends SimpleExpression<Long> {

	static {
		Registry.newSimple(ExprSystemTime.class, "current system milliseconds");
	}
	
	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Long> getReturnType() {
		return Long.class;
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		return true;
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "current system milliseconds";
	}

	@Override
	@Nullable
	protected Long[] get(Event e) {
		return new Long[]{System.currentTimeMillis()};
	}
}