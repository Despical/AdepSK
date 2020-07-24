package me.adeptr.adepsk.utilities.files;

import java.nio.charset.Charset;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.Nullable;

import org.bukkit.event.Event;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.Main;

/**
 * Created by tim740 on 17/03/2016
 */
public class ExprFileLines extends SimpleExpression<Number> {
	private Expression<String> path;

	@Override
	@Nullable
	protected Number[] get(Event e) {
		Path pth = Paths.get(Main.getDefaultPath(path.getSingle(e)));
		try {
			return new Number[] { Files.lines(pth, Charset.defaultCharset()).count() };
		} catch (NoSuchFileException x) {
			Main.prSysE("File: '" + pth + "' doesn't exist!", getClass().getSimpleName(), x);
		} catch (AccessDeniedException x) {
			Main.prSysE("File: '" + pth + "' is read only!", getClass().getSimpleName(), x);
		} catch (Exception x) {
			Main.prSysE("File: '" + pth + "' " + x.getMessage(), getClass().getSimpleName(), x);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] e, int i, Kleenean k, ParseResult p) {
		path = (Expression<String>) e[0];
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
