package me.adeptr.adepsk.utilities.convert;

import java.io.IOException;
import java.io.StringReader;
import java.util.Objects;
import java.util.Properties;

import javax.annotation.Nullable;

import org.bukkit.event.Event;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.Main;

/**
 * Created by tim740 on 07/03/16
 */
public class ExprFromUnicode extends SimpleExpression<String> {
	private Expression<String> str;
	private int ty;

	@Override
	@Nullable
	protected String[] get(Event e) {
		String out = "";
		Properties p = new Properties();
		try {
			p.load(new StringReader("key=" + str.getSingle(e)));
		} catch (IOException x) {
			Main.prSysE(x.getMessage(), getClass().getSimpleName(), x);
		}
		if (ty == 0) {
			out = p.getProperty("key");
		} else {
			String iout = p.getProperty("key");
			for (String c : iout.split("")) {
				if (Objects.equals(out, "")) {
					out = (Integer.toString(c.charAt(0)));
				} else {
					out += "," + Integer.toString(c.charAt(0));
				}
			}
		}
		return new String[] { out };
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] e, int i, Kleenean k, ParseResult p) {
		ty = p.mark;
		str = (Expression<String>) e[0];
		return true;
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
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