package me.adeptr.adepsk.expressions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.bukkit.event.Event;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.util.Registry;

public class ExprRegexPattern extends SimpleExpression<Pattern> {

	public static Map<String, Pattern> patterns = null;

	static {
		Registry.newMatchesEverything(ExprRegexPattern.class, "<.+> [regex] pattern");
	}

	private void init() {
		if (patterns == null) {
			patterns = new HashMap<>();
			patterns.put("uuid", Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"));
			patterns.put("variable", Pattern.compile("(?<=^|,)\\s*([^\",]*|\"([^\"]|\"\")*\")\\s*(,|$)"));
			patterns.put("list", SkriptParser.listSplitPattern);
		}
	}

	private Pattern result;

	@Override
	protected Pattern[] get(Event event) {
		return new Pattern[] { result };
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Pattern> getReturnType() {
		return Pattern.class;
	}

	@Override
	public String toString(Event event, boolean b) {
		return null;
	}

	@Override
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		init();
		String name = parseResult.regexes.get(0).group(0).toLowerCase();
		if ((result = patterns.get(name)) != null)
			return true;
		Skript.error("Doesn't exist any regex pattern called '" + name + "'.");
		return false;
	}
}