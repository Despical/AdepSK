package me.adeptr.adepsk.utilities.files;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bukkit.event.Event;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.Main;

/**
 * Created by tim740 on 18/03/2016
 */
public class CondIsFile extends Condition {
	private Expression<String> path;

	@Override
	public boolean check(Event e) {
		Path pth = Paths.get(Main.getDefaultPath(path.getSingle(e)));
		if (!Files.exists(pth))
			return false;
		Boolean b = !Files.isRegularFile(pth);
		return (isNegated() ? !b : b);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] e, int i, Kleenean k, SkriptParser.ParseResult p) {
		path = (Expression<String>) e[0];
		setNegated(i == 1);
		return true;
	}

	@Override
	public String toString(Event e, boolean b) {
		return getClass().getName();
	}
}
