package me.adeptr.adepsk.utilities.files;

import java.io.IOException;

import javax.annotation.Nullable;

import org.apache.commons.io.IOUtils;
import org.bukkit.event.Event;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.Main;

/**
 * Created by tim740 on 10/04/17
 */
public class EffRunCmd extends Effect {
	static String o = "";
	private Expression<String> cmdr;

	@Override
	protected void execute(Event e) {
		String cmd = cmdr.getSingle(e);
		try {
			Process rp = Runtime.getRuntime().exec(cmd);
			rp.waitFor();
			o = IOUtils.toString(rp.getInputStream());
		} catch (IOException | InterruptedException x) {
			Main.prSysE("Can't run Command: '" + cmd + "' - '" + x.getMessage(), getClass().getSimpleName() + "'", x);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] e, int i, Kleenean k, ParseResult p) {
		cmdr = (Expression<String>) e[0];
		return true;
	}

	@Override
	public String toString(@Nullable Event e, boolean b) {
		return getClass().getName();
	}
}