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
public class ExprGetCorpse extends SimpleExpression<CorpseData> {

	static {
		Registry.newSimple(ExprGetCorpse.class, new String[] { "[the] corpse with ID %string%" });
	}

	private Expression<String> ID;

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends CorpseData> getReturnType() {
		return CorpseData.class;
	}

	@Override
	public String toString(Event event, boolean b) {
		return "[the] corpse with ID %string%";
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] e, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		ID = (Expression<String>) e[0];
		return true;
	}

	@Override
	@Nullable
	protected CorpseData[] get(Event e) {
		if (CorpseManager.contains(ID.getSingle(e)) && ID != null) {
			return new CorpseData[] { CorpseManager.get(ID.getSingle(e)) };
		}
		return null;
	}
}