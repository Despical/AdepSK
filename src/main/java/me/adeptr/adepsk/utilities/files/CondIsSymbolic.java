package me.adeptr.adepsk.utilities.files;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.bukkit.event.Event;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.Main;

/**
 * Created by tim740 on 15/08/2016
 */
public class CondIsSymbolic extends Condition {
	private Expression<String> path;

	@Override
	public boolean check(Event e) {
		Boolean pth = Files.isSymbolicLink(Paths.get(Main.getDefaultPath(path.getSingle(e))));
		return (isNegated() ? !pth : pth);
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
