package me.adeptr.adepsk.reqn.skript;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;

public class ExprURLEncode extends SimpleExpression<String> {

	static {
		Skript.registerExpression(ExprURLEncode.class, String.class, ExpressionType.SIMPLE, "(http|ur(i|l)) (safe|encoded|escaped) %strings%");
	}

	private Expression<String> str;

	private static String encode(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	protected String[] get(Event e) {
		return Arrays.stream(str.getAll(e)).map(ExprURLEncode::encode).toArray(String[]::new);
	}

	@Override
	public boolean isSingle() {
		return str.isSingle();
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "url encoded text";
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed,
			SkriptParser.ParseResult parseResult) {
		str = (Expression<String>) exprs[0];
		return true;
	}
}