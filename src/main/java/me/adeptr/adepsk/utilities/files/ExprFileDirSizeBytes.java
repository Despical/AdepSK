package me.adeptr.adepsk.utilities.files;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;
import org.bukkit.event.Event;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.Main;

/**
 * Created by tim740 on 17/03/2016
 */
public class ExprFileDirSizeBytes extends SimpleExpression<Number> {
	private Expression<String> path;
	private int ty;

	@Override
	@Nullable
	protected Number[] get(Event e) {
		Path pth = Paths.get(Main.getDefaultPath(path.getSingle(e)));
		try {
			if (ty == 0) {
				return new Number[] { Files.size(pth) };
			} else {
				return new Number[] { FileUtils.sizeOfDirectory(pth.toFile()) };
			}
		} catch (Exception x) {
			Main.prSysE("File: '" + pth + "' doesn't exist!", getClass().getSimpleName());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] e, int i, Kleenean k, ParseResult p) {
		path = (Expression<String>) e[0];
		ty = p.mark;
		return true;
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public String toString(@Nullable Event e, boolean b) {
		return getClass().getName();
	}
}
