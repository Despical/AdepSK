package me.adeptr.adepsk.expressions;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.PlayerInventory;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.ExpressionType;
import me.adeptr.adepsk.util.Collect;
import me.adeptr.adepsk.util.Registry;

public class ExprHotbarSlot extends SimplePropertyExpression<Player, Number> {
	
	static {
		Registry.newProperty(ExprHotbarSlot.class, "(current|selected) hotbar slot [id]", "players");
	}
	
	@Override
	protected String getPropertyName() {
		return "selected hotbar slot";
	}

	@Override
	public Number convert(Player player) {
		return player.getInventory().getHeldItemSlot();
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

	@Override
	public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
		Number n = delta[0] == null ? 0 : (Number) delta[0];
		PlayerInventory i = getExpr().getSingle(e).getInventory();
		switch (mode) {
		case ADD:
			i.setHeldItemSlot((n.intValue() + i.getHeldItemSlot()) % 9);
			break;
		case SET:
			i.setHeldItemSlot(n.intValue());
			break;
		case REMOVE:
			i.setHeldItemSlot((n.intValue() - i.getHeldItemSlot()) % 9);
			break;
		case RESET:
			i.setHeldItemSlot(0);
			break;
		default:
			assert false;
		}
	}

	@Override
	public Class<?>[] acceptChange(Changer.ChangeMode mode) {
		return mode == Changer.ChangeMode.ADD || mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.REMOVE || mode == Changer.ChangeMode.RESET ? Collect.asArray(Number.class) : null;
	}
}