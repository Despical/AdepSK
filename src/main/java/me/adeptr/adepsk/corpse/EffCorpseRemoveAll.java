package me.adeptr.adepsk.corpse;

import javax.annotation.Nullable;

import org.bukkit.event.Event;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.util.Registry;

/**
 * @author Despical
 * <p>
 * Created at 16.07.2020
 */
public class EffCorpseRemoveAll extends Effect {

	static {
		Registry.newEffect(EffCorpseRemoveAll.class, "(delete|unregister|remove) (the|all) [of] [the] corpse[s]");
	}

	@Override
	public boolean init(Expression<?>[] e, int arg1, Kleenean arg2, ParseResult arg3) {
		return true;
	}

	@Override
	public String toString(@Nullable Event arg0, boolean arg1) {
		return "(delete|unregister|remove) (the|all) [of] [the] corpse[s]";
	}

	@Override
	protected void execute(Event e) {
		CorpseManager.unregisterAll();
	}
}