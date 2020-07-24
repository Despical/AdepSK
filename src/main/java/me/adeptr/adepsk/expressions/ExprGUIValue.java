package me.adeptr.adepsk.expressions;

import javax.annotation.Nullable;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.slot.CursorSlot;
import ch.njol.skript.util.slot.InventorySlot;
import ch.njol.skript.util.slot.Slot;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.effects.EffFormatGUI;
import me.adeptr.adepsk.effects.EffMakeGUI;
import me.adeptr.adepsk.effects.EffOnCloseGUI;
import me.adeptr.adepsk.gui.GUIHandler;
import me.adeptr.adepsk.gui.GUIInventory;
import me.adeptr.adepsk.util.EffectSection;
import me.adeptr.adepsk.util.InventoryUtils;
import me.adeptr.adepsk.util.Registry;

public class ExprGUIValue extends SimpleExpression<Object> {

	static {
		Registry.newSimple(ExprGUIValue.class,
				new String[] { "gui-slot", "gui-raw-slot", "gui-hotbar-slot", "gui-inventory", "gui-inventory-action",
						"gui-click-(type|action)", "gui-cursor[-item]", "gui-[(clicked|current)-]item", "gui-slot-type",
						"gui-player", "gui-players", "gui-inventory-name", "gui-slot-id", "gui" });
	}

	private int type = -1;
	private String toString = "gui-value";

	private boolean isDelayed = false;
	private boolean isOldGui = false;

	public Class<? extends Object> getReturnType() {
		switch (this.type) {
		case 0:
		case 1:
		case 2:
			return (Class) Number.class;
		case 3:
			return (Class) Inventory.class;
		case 4:
			return (Class) InventoryAction.class;
		case 5:
			return (Class) ClickType.class;
		case 6:
		case 7:
			return (Class) ItemStack.class;
		case 8:
			return (Class) InventoryType.SlotType.class;
		case 9:
		case 10:
			return (Class) Player.class;
		case 11:
		case 12:
			return (Class) String.class;
		case 13:
			return (Class) GUIInventory.class;
		}
		return Object.class;
	}

	public boolean isSingle() {
		return (this.type != 10);
	}

	public boolean init(Expression<?>[] arg0, int arg1, Kleenean arg2, SkriptParser.ParseResult arg3) {
		if (!EffectSection
				.isCurrentSection(new Class[] { EffMakeGUI.class, EffFormatGUI.class, EffOnCloseGUI.class })) {
			Skript.error("You can't use '" + arg3.expr
					+ "' outside of a 'make gui', 'format gui slot' or 'run when close' section.");
			return false;
		}
		if (EffectSection.isCurrentSection(new Class[] { EffFormatGUI.class })) {
			this.isOldGui = true;
		}
		this.isDelayed = arg2.isTrue();
		this.type = arg1;
		this.toString = arg3.expr;
		return true;
	}

	public String toString(@Nullable Event arg0, boolean arg1) {
		return this.toString;
	}

	@Nullable
	protected Object[] get(Event e) {
		GUIInventory gui = !this.isOldGui ? GUIHandler.getInstance().getGUIEvent(e) : null;
		if (e instanceof InventoryClickEvent) {
			Inventory c;
			if (!this.isOldGui && gui == null)
				return null;
			switch (this.type) {
			case 0:
				return (Object[]) new Number[] { Integer.valueOf(((InventoryClickEvent) e).getSlot()) };
			case 1:
				return (Object[]) new Number[] { Integer.valueOf(((InventoryClickEvent) e).getRawSlot()) };
			case 2:
				return (Object[]) new Number[] { Integer.valueOf(((InventoryClickEvent) e).getHotbarButton()) };
			case 3:
				return (Object[]) new Inventory[] { InventoryUtils.getClickedInventory((InventoryClickEvent) e) };
			case 4:
				return (Object[]) new InventoryAction[] { ((InventoryClickEvent) e).getAction() };
			case 5:
				return (Object[]) new ClickType[] { ((InventoryClickEvent) e).getClick() };
			case 6:
				return (Object[]) new ItemStack[] { ((InventoryClickEvent) e).getCursor() };
			case 7:
				return (Object[]) new ItemStack[] { ((InventoryClickEvent) e).getCurrentItem() };
			case 8:
				return (Object[]) new InventoryType.SlotType[] { ((InventoryClickEvent) e).getSlotType() };
			case 9:
				return (Object[]) new Player[] { (Player) ((InventoryClickEvent) e).getWhoClicked() };
			case 10:
				return ((InventoryClickEvent) e).getViewers().toArray();
			case 11:
				c = InventoryUtils.getClickedInventory((InventoryClickEvent) e);
				if (c != null)
					return (Object[]) new String[] { ((HumanEntity) c).getName() };
				break;
			case 12:
				if (gui != null)
					return (Object[]) new String[] { "" + gui.convertSlot(((InventoryClickEvent) e).getSlot()) };
				break;
			case 13:
				if (gui != null)
					return (Object[]) new GUIInventory[] { gui };
				break;
			}

		} else if (e instanceof InventoryCloseEvent && gui != null) {
			switch (this.type) {
			case 3:
				return (Object[]) new Inventory[] { ((InventoryCloseEvent) e).getInventory() };
			case 9:
				return (Object[]) new Player[] { (Player) ((InventoryCloseEvent) e).getPlayer() };
			case 10:
				return ((InventoryCloseEvent) e).getViewers().toArray();
			case 11:
				if (((InventoryCloseEvent) e).getInventory() != null)
					return (Object[]) new String[] { ((HumanEntity) ((InventoryCloseEvent) e).getInventory()).getName() };
				break;
			case 13:
				return (Object[]) new GUIInventory[] { gui };
			}

		}
		return null;
	}

	private Changer<Slot> changer = null;

	public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
		if (e instanceof InventoryClickEvent) {
			if (this.type == 7) {
				this.changer.change(new Slot[] { (Slot) new InventorySlot(((InventoryClickEvent) e).getInventory(),
						((InventoryClickEvent) e).getSlot()) }, delta, mode);
			} else if (this.type == 11) {
				GUIInventory gui = GUIHandler.getInstance().getGUIEvent(e);
				String newName = (delta != null && delta.length > 0) ? (String) delta[0] : null;
				if (newName != null && gui != null) {
					gui.changeProperties(newName, 0, null, 0);
				}
			} else {
				CursorSlot cursorSlot = new CursorSlot((Player) ((InventoryClickEvent) e).getWhoClicked());
				this.changer.change(new Slot[] { (Slot) cursorSlot }, delta, mode);
			}
		}
	}

	public Class<?>[] acceptChange(Changer.ChangeMode mode) {
		if (ScriptLoader.isCurrentEvent(InventoryCloseEvent.class))
			return null;
		if (this.type == 6 || this.type == 7) {
			if (!this.isDelayed) {
				if (this.changer == null)
					this.changer = (Changer<Slot>) Classes.getExactClassInfo(Slot.class).getChanger();
				return this.changer.acceptChange(mode);
			}
			Skript.error("You can't set the " + this.toString + " when the event is already passed.");
		} else if (this.type == 1) {
			return new Class[] { String.class };
		}
		return null;
	}
}
