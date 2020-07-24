package me.adeptr.adepsk.corpse;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;
import org.golde.bukkit.corpsereborn.nms.Corpses.CorpseData;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.util.Registry;

/**
 * @author Despical
 * <p>
 * Created at 16.07.2020
 */
public class ExprCorpsePlayer extends SimpleExpression<String> {

	static {
		Registry.newSimple(ExprCorpsePlayer.class,
				new String[] { "[the] name of corpse %corpse%", "corpse %corpse%'s name" });
	}

	private Expression<CorpseData> corpse;

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
		return "[the] player of corpse %corpse%";
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] e, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		corpse = (Expression<CorpseData>) e[0];
		return true;
	}

	@Override
	@Nullable
	protected String[] get(Event e) {
		if (corpse != null) {
			return new String[] { corpse.getSingle(e).getCorpseName() };
		}
		return null;
	}
}