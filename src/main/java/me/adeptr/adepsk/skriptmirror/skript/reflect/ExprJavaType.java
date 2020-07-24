package me.adeptr.adepsk.skriptmirror.skript.reflect;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.skriptmirror.JavaType;
import me.adeptr.adepsk.skriptmirror.LibraryLoader;

import org.bukkit.event.Event;

public class ExprJavaType extends SimpleExpression<JavaType> {
	static {
		Skript.registerExpression(ExprJavaType.class, JavaType.class, ExpressionType.COMBINED,
				"[the] [java] class %string%");
	}

	private Expression<String> className;

	@Override
	protected JavaType[] get(Event e) {
		String cls = className.getSingle(e);

		if (cls == null) {
			return null;
		}

		try {
			return new JavaType[] { new JavaType(LibraryLoader.getClassLoader().loadClass(cls)) };
		} catch (ClassNotFoundException ex) {
			return null;
		}
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends JavaType> getReturnType() {
		return JavaType.class;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "class " + className.toString(e, debug);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed,
			SkriptParser.ParseResult parseResult) {
		className = (Expression<String>) exprs[0];
		return true;
	}
}
