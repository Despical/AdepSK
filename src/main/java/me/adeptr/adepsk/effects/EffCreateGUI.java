package me.adeptr.adepsk.effects;

import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.gui.GUIHandler;
import me.adeptr.adepsk.gui.GUIInventory;
import me.adeptr.adepsk.gui.SkriptGUIEvent;
import me.adeptr.adepsk.util.LazyEffectSection;
import me.adeptr.adepsk.util.Registry;

public class EffCreateGUI extends LazyEffectSection {
	static {
		Registry.newEffect(EffCreateGUI.class,
				"create [a] [new] gui [[with id] %-string%] with %inventory% [and shape %-strings%]",
				"(change|edit) %guiinventory%");
	}

	// public static EffCreateGUI lastInstance = null;
	// public GUIInventory gui = null;
	private Expression<GUIInventory> exprGui;
	private Expression<Inventory> inv;
	private Expression<String> str, id;

	@Override
	public void execute(Event e) {
		GUIHandler.getInstance().setGUIEvent(e, null);
		if (exprGui == null) { // It will create a new one
			Inventory inv = this.inv.getSingle(e);
			if (inv != null /*
							 * && inv.getType() != InventoryType.PLAYER && inv.getType() !=
							 * InventoryType.CRAFTING
							 */) {
				GUIInventory gui = new GUIInventory(inv);
				if (str != null)
					gui.shape(str.getArray(e));
				else
					gui.shapeDefault();
				String id = this.id != null ? this.id.getSingle(e) : null;
				if (id != null && !id.isEmpty())
					GUIHandler.getInstance().setGUI(id, gui);
				GUIHandler.getInstance().setGUIEvent(e, gui);
			}
		} else { // It will edit one
			GUIInventory gui = exprGui.getSingle(e);
			if (gui != null) {
				GUIHandler.getInstance().setGUIEvent(e, gui);
			}
		}
	}

	@Override
	public String toString(Event event, boolean b) {
		return "create gui";
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] arg, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		if (checkIfCondition()) {
			return false;
		}
		if (i > 0) {
			if (!hasSection()) {
				Skript.error(
						"You can't edit a gui inventory using an empty section, you need to change at least a slot or a property.");
				return false;
			}
			exprGui = (Expression<GUIInventory>) arg[0];
		} else {
			id = (Expression<String>) arg[0];
			inv = (Expression<Inventory>) arg[1];
			str = (Expression<String>) arg[2];
		}
		// Just a safe check, to make sure the listener was registered when this is
		// loaded
		SkriptGUIEvent.getInstance().register();
		return true;
	}
}