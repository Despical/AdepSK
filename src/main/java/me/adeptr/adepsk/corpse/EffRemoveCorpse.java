package me.adeptr.adepsk.corpse;

import javax.annotation.Nullable;

import org.bukkit.event.Event;
import org.golde.bukkit.corpsereborn.nms.Corpses.CorpseData;

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
public class EffRemoveCorpse extends Effect {

	static {
		Registry.newEffect(EffRemoveCorpse.class, "(delete|remove|unregister) corpse %corpse%");
	}

	private Expression<CorpseData> corpse;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] e, int arg1, Kleenean arg2, ParseResult arg3) {
		corpse = (Expression<CorpseData>) e[0];
		return true;
	}

	@Override
	public String toString(@Nullable Event arg0, boolean arg1) {
		return "(delete|remove|unregister) corpse %corpse%";
	}

	@Override
	protected void execute(Event e) {
		if (corpse != null) {
			CorpseManager.removeCorpse(corpse.getSingle(e));
		}
	}
}