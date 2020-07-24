package me.adeptr.adepsk.utilities.files;

import java.io.File;

import javax.annotation.Nullable;

import org.bukkit.event.Event;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.Main;

/**
 * Created by tim740 on 03/04/2016
 */
public class ExprDiskSpace extends SimpleExpression<String> {
	private int ty;

	@Override
	@Nullable
	protected String[] get(Event e) {
		switch (ty) {
		case 0:
			return new String[] { Main.getFileSize(new File(File.separator).getTotalSpace()) };
		case 1:
			return new String[] { Main.getFileSize(new File(File.separator).getFreeSpace()) };
		case 2:
			return new String[] { Main.getFileSize(new File(File.separator).getUsableSpace()) };
		}
		return null;
	}

	@Override
	public boolean init(Expression<?>[] e, int i, Kleenean k, ParseResult p) {
		ty = p.mark;
		return true;
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
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
