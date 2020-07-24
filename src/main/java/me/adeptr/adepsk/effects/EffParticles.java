package me.adeptr.adepsk.effects;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.util.EnumParticleGetter;
import me.adeptr.adepsk.util.Registry;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class EffParticles extends Effect {
	 
	static {
		Registry.newEffect(EffParticles.class, "show %number% of %string% particle[s] at %location% for %players% [offset with %number%, %number% (and|,) %number%]");
	}
	
	private Expression<Number> partNum;
    private Expression<Player> player;
    private Expression<Location> location;
    private Expression<String> type;
    private Expression<Number> xoffset;
    private Expression<Number> yoffset;
    private Expression<Number> zoffset;


    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exp, int arg1, Kleenean arg2, ParseResult arg3) {
        partNum = (Expression<Number>) exp[0];
        type = (Expression<String>) exp[1];
        location = (Expression<Location>) exp[2];
        xoffset = (Expression<Number>) exp[4];
        yoffset = (Expression<Number>) exp[5];
        zoffset = (Expression<Number>) exp[6];
        player = (Expression<Player>) exp[3];
        return true;
    }

    @Override
    public String toString(@Nullable Event arg0, boolean arg1) {
        return "show";
    }

    @Override
    protected void execute(Event evt) {
        float hx = 0f;
        float hy = 0f;
        float hz = 0f;
        float id = 0;
        int[] array = new int[0];
        if (xoffset != null) {
            hx = xoffset.getSingle(evt).floatValue();
        }
        if (yoffset != null) {
            hy = yoffset.getSingle(evt).floatValue();
        }
        if (zoffset != null) {
            hz = zoffset.getSingle(evt).floatValue();
        }
        String core = type.getSingle(evt);
        if (core.toUpperCase().replace(" ", "_").contains("BLOCK_CRACK")
                || core.toUpperCase().replace(" ", "_").contains("BLOCK_DUST")) {
            int index = type.getSingle(evt).lastIndexOf("_");
            try {
                id = Integer.parseInt(type.getSingle(evt).substring(index + 1));
            } catch (Exception exception) {
                Skript.error("Could not parse datavalue!");
                id = 0;
            }
            core = core.substring(0, index);
            array = new int[1];
        }
        EnumParticle part = EnumParticleGetter.get(core);
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(part, true, (float) location.getSingle(evt).getX(), (float) location.getSingle(evt).getY(), (float) location.getSingle(evt).getZ(), hx, hy, hz, id, partNum.getSingle(evt).intValue(), array);
        for (Player p : player.getAll(evt)) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }
}