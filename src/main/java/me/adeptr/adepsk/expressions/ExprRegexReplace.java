package me.adeptr.adepsk.expressions;

import java.util.regex.Pattern;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.util.Regex;
import me.adeptr.adepsk.util.Registry;

public class ExprRegexReplace extends SimpleExpression<String> {

	private Expression<String> regex;

	static {
		Registry.newSimple(ExprRegexReplace.class, new String[] { "regex replace (all|every|first|) [pattern] %string% with [group[s]] %string% in %string%" });
	}

	private Expression<String> with;

	private Expression<String> from;

	private boolean isFirst = false;
	
	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}
	
	@Override
	public boolean isSingle() {
		return true;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean init(Expression<?>[] arg, int arg1, Kleenean arg2, ParseResult arg3) {
		this.regex = arg[0].getConvertedExpression(new Class[] { Object.class });
		this.with = (Expression) arg[1];
		this.from = (Expression) arg[2];
		this.isFirst = arg3.expr.toLowerCase().startsWith("regex replace first");
		return true;
	}

	public String toString(@Nullable Event arg0, boolean arg1) {
		return null;
	}

	@Nullable
	protected String[] get(Event e) {
		String with = (String) this.with.getSingle(e);
		String from = (String) this.from.getSingle(e);
		Pattern p = Regex.getInstance().getPattern(this.regex.getSingle(e));
		if (from != null && with != null && p != null)
			return new String[] { Regex.getInstance().regexReplace(p, with, from, this.isFirst) };
		return null;
	}
}