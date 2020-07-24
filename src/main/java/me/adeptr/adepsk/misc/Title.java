package me.adeptr.adepsk.misc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.entity.Player;

import me.adeptr.adepsk.util.ReflectionUtils;

public class Title {

	ReflectionUtils reflection = new ReflectionUtils();

	public void sendTitle(Player player, String title, String subtitle, int fadein, int fadeout, int duration)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
		Class<?> PacketPlayOutTitle = ReflectionUtils.getNMSClass("PacketPlayOutTitle");
		Class<?> IChatBaseComponent = ReflectionUtils.getNMSClass("IChatBaseComponent");
		Class<?> ChatSerializer = ReflectionUtils.getNMSClass("IChatBaseComponent$ChatSerializer");
		Class<?> EnumTitleAction = ReflectionUtils.getNMSClass("PacketPlayOutTitle$EnumTitleAction");
		Object basetitle = ChatSerializer.getMethod("a", String.class).invoke(null, "{\"text\": \"" + title + "\"}");
		Object endtitle = PacketPlayOutTitle.getConstructor(EnumTitleAction, IChatBaseComponent)
				.newInstance(EnumTitleAction.getField("TITLE").get(null), basetitle);
		Object basesubtitle = ChatSerializer.getMethod("a", String.class).invoke(null,
				"{\"text\": \"" + subtitle + "\"}");
		Object endsubtitle = PacketPlayOutTitle.getConstructor(EnumTitleAction, IChatBaseComponent)
				.newInstance(EnumTitleAction.getField("SUBTITLE").get(null), basesubtitle);
		Object endtime = PacketPlayOutTitle.getConstructor(int.class, int.class, int.class).newInstance(fadein,
				duration, fadeout);
		Method sendPacket = ReflectionUtils.getConnection(player).getClass().getMethod("sendPacket",
				ReflectionUtils.getNMSClass("Packet"));
		sendPacket.invoke(ReflectionUtils.getConnection(player), endtitle);
		sendPacket.invoke(ReflectionUtils.getConnection(player), endsubtitle);
		sendPacket.invoke(ReflectionUtils.getConnection(player), endtime);
	}
}