package me.adeptr.adepsk.effects;

import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryCloseEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.gui.GUIHandler;
import me.adeptr.adepsk.gui.GUIInventory;
import me.adeptr.adepsk.util.EffectSection;
import me.adeptr.adepsk.util.Registry;
import me.adeptr.adepsk.util.VariableUtil;

public class EffOnCloseGUI extends EffectSection {
	static {
		Registry.newEffect(EffOnCloseGUI.class, "run (when|while) clos(e|ing) [[the] gui]");
	}

	@Override
	public void execute(Event e) {
		if (hasSection()) {
			GUIInventory gui = GUIHandler.getInstance().getGUIEvent(e);
			if (gui != null) {
				VariableUtil var = VariableUtil.getInstance();
				Object vars = var.copyVariables(e);
				gui.onClose(event -> {
					var.pasteVariables(event, vars);
					runSection(event);
				});
			}
		}
	}

	@Override
	public String toString(Event event, boolean b) {
		return "run when close";
	}

	@Override
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		if (checkIfCondition()) {
			return false;
		}
		if (!isCurrentSection(EffCreateGUI.class)) {
			Skript.error("You can't make a gui close action outside of 'create/edit gui' effect.");
			return false;
		}
		if (!hasSection()) {
			Skript.error("An empty action can't be executed when the gui is closing.");
			return false;
		}
		loadSection("gui close", false, InventoryCloseEvent.class);
		return true;
	}
}