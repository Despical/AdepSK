package me.adeptr.adepsk.expressions;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.event.Event;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.util.Collect;
import me.adeptr.adepsk.util.Markup;
import me.adeptr.adepsk.util.Registry;

public class ExprKeyString extends SimpleExpression<String> {

	static {
		Registry.newSimple(ExprKeyString.class, "random %number% (char|long|length) string from [charset] %string%");
	}
	
	private Expression<Number> length;
	private Expression<String> charset;

	@Override
	protected String[] get(Event event) {
		Number l = length.getSingle(event);
		Markup mk = new Markup(charset.getSingle(event));
		if (l == null || mk == null)
			return null;
		int amt = l.intValue();
		String chars = mk.toString();
		return Collect.asArray(getKey(amt, chars));
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public String toString(Event event, boolean b) {
		return "random";
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		length = (Expression<Number>) expressions[0];
		charset = (Expression<String>) expressions[1];
		return true;
	}

	public static String getKey(int length, String charset) {
		ArrayList<ArrayList<Integer>> charranges = new ArrayList<>();
		Pattern regex = Pattern.compile("(.)-(.)");
		Matcher m = regex.matcher(charset);
		while (m.find()) {
			ArrayList<Integer> range = new ArrayList<>();
			int first = m.group(1).charAt(0);
			int second = m.group(2).charAt(0);
			range.add(Math.min(first, second));
			range.add(Math.max(first, second));
			charranges.add(range);
		}
		Random rng = new Random();
		String out = "";
		while (0 < length--) {
			ArrayList<Integer> current = charranges.get(rng.nextInt(charranges.size()));
			out += Character.toString((char) (rng.nextInt((current.get(1) - current.get(0)) + 1) + current.get(0)));
		}
		return out;
	}
}