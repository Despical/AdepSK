package me.adeptr.adepsk.expressions;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.util.Slot;
import ch.njol.util.coll.CollectionUtils;
import tk.shanebee.bee.api.NBTApi;

public class ExprObjectNBT extends SimplePropertyExpression<Object, String> {

    private static final NBTApi NBT_API;

	static {
		register(ExprObjectNBT.class, String.class, "[(entity|item|slot|block|tile[(-| )]entity|file)(-| )]nbt", "block/entity/itemstack/itemtype/slot/string");
        NBT_API = new NBTApi();
    }

    @Override
    public String convert(Object o) {
        if (o instanceof Slot) {
            return NBT_API.getNBT(((Slot) o));
        } else if (o instanceof ItemStack) {
            return NBT_API.getNBT((ItemStack) o);
        } else if (o instanceof ItemType) {
            return NBT_API.getNBT((ItemType) o);
        } else if (o instanceof Entity) {
            return NBT_API.getNBT((Entity) o);
        } else if (o instanceof Block) {
            return NBT_API.getNBT((Block) o);
        } else if (o instanceof String) {
            return NBT_API.getNBT((String) o);
        }
        return null;
    }

    @Override
    public Class<?>[] acceptChange(final ChangeMode mode) {
        if (mode == ChangeMode.ADD || mode == ChangeMode.SET || mode == ChangeMode.RESET)
            return CollectionUtils.array(String.class);
        return null;
    }

    @Override
    public void change(Event event, Object[] delta, ChangeMode mode) {
        Object o = getExpr().getSingle(event);
        String value = delta != null ? ((String) delta[0]) : "{}";
        if (!NBT_API.validateNBT(value)) {
            return;
        }
        switch (mode) {
            case ADD:
                if (o instanceof Slot) {
                    NBT_API.addNBT((Slot) o, value);
                } else if (o instanceof ItemStack) {
                    NBT_API.addNBT((ItemStack) o, value);
                } else if (o instanceof ItemType) {
                    NBT_API.addNBT((ItemType) o, value);
                } else if (o instanceof Entity) {
                    NBT_API.addNBT((Entity) o, value);
                } else if (o instanceof Block) {
                    NBT_API.addNBT(((Block) o), value);
                } else if (o instanceof String) {
                    NBT_API.addNBT(((String) o), value);
                }
                break;
            case SET:
            case RESET:
                if (o instanceof Slot) {
                    NBT_API.setNBT((Slot) o, value);
                } else if (o instanceof ItemStack) {
                    NBT_API.setNBT((ItemStack) o, value);
                } else if (o instanceof ItemType) {
                    NBT_API.setNBT((ItemType) o, value);
                } else if (o instanceof Entity) {
                    NBT_API.setNBT((Entity) o, value);
                } else if (o instanceof Block) {
                    NBT_API.setNBT(((Block) o), value);
                } else if (o instanceof String) {
                    NBT_API.setNBT(((String) o), value);
                }
                break;
            default:
                assert false;
        }
    }

    @Override
    protected String getPropertyName() {
        return "object nbt";
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
}