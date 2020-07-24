package me.adeptr.adepsk.utilities.convert;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import javax.annotation.Nullable;

import org.bukkit.event.Event;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Date;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.Main;

/**
 * Created by tim740 on 31/01/2017
 */
public class ExprDateParsed extends SimpleExpression<Date> {
	private Expression<String> id, format;

	@Override
	@Nullable
	protected Date[] get(Event e) {
		String s = id.getSingle(e);
		try {
			String ddf = new SimpleDateFormat().toPattern();
			if (format != null)
				ddf = format.getSingle(e);
			LocalDateTime ldt = LocalDateTime.parse(s, DateTimeFormatter.ofPattern(ddf));
			return new Date[] { new Date(
					(ldt.toEpochSecond(ZoneOffset.ofTotalSeconds(ldt.getSecond())) + ldt.getSecond()) * 1000) };
		} catch (Exception x) {
			Main.prSysE(x.getMessage(), getClass().getSimpleName(), x);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] e, int i, Kleenean k, ParseResult p) {
		id = (Expression<String>) e[0];
		format = (Expression<String>) e[1];
		return true;
	}

	@Override
	public Class<? extends Date> getReturnType() {
		return Date.class;
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
