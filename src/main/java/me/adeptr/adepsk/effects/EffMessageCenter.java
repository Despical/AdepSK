package me.adeptr.adepsk.effects;

import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.util.Registry;
import me.despical.commonsbox.miscellaneous.MiscUtils;

/**
 * @author Despical
 * <p>
 * Created at 17.07.2020
 */
public class EffMessageCenter extends Effect {
	
	static {
		Registry.newEffect(EffMessageCenter.class, "(message|send [message]) center[ed] %strings% to %players%");
	}

	private Expression<String> message;
	private Expression<Player> player;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] e, int arg1, Kleenean arg2, ParseResult arg3) {
		message = (Expression<String>) e[0];
		player = (Expression<Player>) e[1];
		return true;
	}

	@Override
	public String toString(@Nullable Event paramEvent, boolean paramBoolean) {
		return "(message|send [message]) center[ed] %strings% to %players%";
	}

	@Override
	protected void execute(Event e) {
		for (Player player : player.getAll(e)) {
			for (String message : message.getAll(e)) {
				MiscUtils.sendCenteredMessage(player, message);
			}
		}
	}
}