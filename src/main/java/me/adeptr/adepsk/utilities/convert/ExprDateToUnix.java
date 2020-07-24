package me.adeptr.adepsk.utilities.convert;

import javax.annotation.Nullable;

import org.bukkit.event.Event;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Date;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.Main;

/**
 * Created by tim740 on 30/03/2016
 */
public class ExprDateToUnix extends SimpleExpression<Number> {
	private Expression<Date> id;

	@Override
	@Nullable
	protected Number[] get(Event e) {
		try {
			return new Number[] { id.getSingle(e).getTimestamp() / 1000L };
		} catch (Exception x) {
			Main.prSysE(x.getMessage(), getClass().getSimpleName(), x);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] e, int i, Kleenean k, ParseResult p) {
		id = (Expression<Date>) e[0];
		return true;
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public String toString(@Nullable Event e, boolean b) {
		return getClass().getName();
	}
}
