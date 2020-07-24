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
public class ExprCorpseAll extends SimpleExpression<CorpseData> {

	static {
		Registry.newSimple(ExprCorpseAll.class, "[(the|all)] [of] [the] corpse[s]");
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends CorpseData> getReturnType() {
		return CorpseData.class;
	}

	@Override
	public String toString(Event event, boolean b) {
		return "[(the|all)] [of] [the] corpse[s]";
	}

	@Override
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		return true;
	}

	@Override
	@Nullable
	protected CorpseData[] get(Event e) {
		return CorpseManager.getAll();
	}
}