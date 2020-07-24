package me.adeptr.adepsk.expressions;

import org.bukkit.event.Event;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.util.Collect;

public class ExprNewLine extends SimpleExpression<String> {

	static {
		Skript.registerExpression(ExprNewLine.class, String.class, ExpressionType.SIMPLE, "(nl|new[ ]line|\\|\\|)");
	}
	
    @Override
    protected String[] get(Event event) {
        return Collect.asArray("\n");
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(Event event, boolean b) {
        return "null";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        return true;
    }
}