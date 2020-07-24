package me.adeptr.adepsk;

import java.io.File;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import ch.njol.skript.util.Direction;
import me.adeptr.adepsk.gui.GUIManager;
import me.adeptr.adepsk.gui.SkriptGUIEvent;
import me.adeptr.adepsk.misc.ActionBarAPI;
import me.adeptr.adepsk.misc.ActionBarNew;
import me.adeptr.adepsk.misc.ActionBarOld;
import me.adeptr.adepsk.misc.Title;
import me.adeptr.adepsk.skriptmirror.LibraryLoader;
import me.adeptr.adepsk.skriptmirror.ParseOrderWorkarounds;
import me.adeptr.adepsk.util.EnumClassInfo;
import me.adeptr.adepsk.util.EventRegistry;
import me.adeptr.adepsk.utilities.Reg;
import me.adeptr.adepsk.utilities.util.EffReloadConfig;
import me.adeptr.adepsk.yaml.utils.yaml.SkriptYamlConstructor;
import me.adeptr.adepsk.yaml.utils.yaml.SkriptYamlRepresenter;
import me.adeptr.adepsk.yaml.utils.yaml.YAMLProcessor;

/**
 * @author Despical
 * <p>
 * Created at 14.07.2020
 */
public class Main extends JavaPlugin {

	static Main plugin;
	private int serverVersion;
	public final static HashMap<String, YAMLProcessor> YAML_STORE = new HashMap<String, YAMLProcessor>();
	public final static Logger LOGGER = Bukkit.getServer() != null ? Bukkit.getLogger() : Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static SkriptYamlRepresenter representer;
	private static SkriptYamlConstructor constructor;
	private static GUIManager gui;
	private String version;
	private Title title;
	private ActionBarAPI actionbar;
	public static boolean startedFollowingHologramTasks = false;
	public static Map<Integer, Map<Hologram, Direction[]>> followingHolograms = new HashMap<>();
	public static Map<Entity, List<Hologram>> followingHologramsEntities = new ConcurrentHashMap<>();
	private static RowSetFactory rowSetFactory;
	public static Set<Hologram> followingHologramsList = new HashSet<>();
	private static final boolean hasMVdW = Skript.classExists("be.maximvdw.placeholderapi.PlaceholderAPI");
	private static final boolean hasPapi = Skript.classExists("me.clip.placeholderapi.expansion.PlaceholderExpansion");

	@Override
	public void onEnable() {
		plugin = this;
		String initServerVer = Bukkit.getServer().getClass().getPackage().getName().substring(23);
		serverVersion = Integer.parseInt(Character.toString(initServerVer.charAt(3)));
		if (serverVersion == 1 && Integer.parseInt(Character.toString(initServerVer.charAt(4))) >= 0) {
			serverVersion = Integer.parseInt(Integer.parseInt(Character.toString(initServerVer.charAt(3))) + ""
				+ Integer.parseInt(Character.toString(initServerVer.charAt(4))));
		}
		representer = new SkriptYamlRepresenter();
		constructor = new SkriptYamlConstructor();
		saveDefaultConfig();
		Boolean hasSkript = plugin.getServer().getPluginManager().isPluginEnabled("Skript");
		if (!hasSkript || !Skript.isAcceptRegistrations()) {
			if (!hasSkript)
				log("Error 404 - Skript not found.", Level.SEVERE);
			else
				log("AdepSK can't be loaded when the server is already loaded.", Level.SEVERE);
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		SkriptAddon adepsk = Skript.registerAddon(this).setLanguageFileDirectory("lang");
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
		if (version.equals("v1_8_R3")) {
			actionbar = new ActionBarOld();
		} else {
			actionbar = new ActionBarNew();
		}
		title = new Title();
		EnumClassInfo.create(Sound.class, "sound").register();
		try {
			rowSetFactory = RowSetProvider.newFactory();
			adepsk.loadClasses(getClass().getPackage().getName(), "db", "utilities", "json", "effects",
				"expressions", "yaml", "conditions", "events", "misc");
			if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
				adepsk.loadClasses(getClass().getPackage().getName(), "placeholder");
				log("Hooking into PAPI placeholders.");
			}
			if (Bukkit.getPluginManager().getPlugin("CorpseReborn") != null) {
				adepsk.loadClasses(getClass().getPackage().getName(), "corpse");
				log("Hooking into Corpses.");
			}
			if (Bukkit.getPluginManager().getPlugin("Protocollib") != null && Bukkit.getPluginManager().getPlugin("HolographicDisplays") != null) {
				adepsk.loadClasses(getClass().getPackage().getName(), "holo");
				log("Hooking into skript-holo.");
			}
			adepsk.loadClasses("me.adeptr.adepsk.reqn.skript");
			adepsk.loadClasses("me.adeptr.adepsk.skriptmirror");
			Path dataFolder = Main.getInstance().getDataFolder().toPath();
			LibraryLoader.loadLibraries(dataFolder);
			ParseOrderWorkarounds.reorderSyntax();
			Bukkit.getConsoleSender().sendMessage("[AdepSK] Plugin is enabled.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (getConfig().getBoolean("loadConversions", true)) {
				Reg.convert();
			}
			if (getConfig().getBoolean("loadFiles", true)) {
				Reg.files();
			}
			if (getConfig().getBoolean("loadUrls", true)) {
				Reg.url();
			}
			if (getConfig().getBoolean("loadUtilities", true)) {
				Reg.utils();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		new EventRegistry().run();
		Skript.registerEffect(EffReloadConfig.class, "reload %string%'s config", "reload config of %string%");
	}

	@Override
	public void onDisable() {
		SkriptGUIEvent.getInstance().unregisterAll();
		if (gui != null)
			gui.clearAll();
		HandlerList.unregisterAll(this);
		Bukkit.getScheduler().cancelTasks(this);
	}
	
	public boolean is1_8_R() {
		return version.equalsIgnoreCase("v1_8_R2") || version.equalsIgnoreCase("v1_8_R3");
	}
	
	public static Main getInstance() {
		return JavaPlugin.getPlugin(Main.class);
	}

	public SkriptYamlRepresenter getRepresenter() {
		return representer;
	}

	public SkriptYamlConstructor getConstructor() {
		return constructor;
	}

	public static void log(String msg) {
		log(msg, Level.INFO);
	}

	public static void log(String msg, Level lvl) {
		plugin.getLogger().log(lvl, msg);
	}

	public static void warn(String error) {
		LOGGER.warning("[AdepSK] " + error);
	}

	public static void error(String error) {
		LOGGER.severe("[AdepSK] " + error);
	}

	public static RowSetFactory getRowSetFactory() {
		return rowSetFactory;
	}

	public int getServerVersion() {
		return serverVersion;
	}

	public static boolean hasMVdW() {
		return hasMVdW;
	}

	public static boolean hasPapi() {
		return hasPapi;
	}

	public static GUIManager getGUIManager() {
		if (gui == null)
			gui = new GUIManager(getInstance());
		return gui;
	}

	public Title getTitle() {
		return title;
	}

	public ActionBarAPI getActionbar() {
		return actionbar;
	}

	public static void prSysE(String s, String c) {
		Bukkit.getServer().getLogger().severe("[AdepSK] v" + getVer() + ": " + s + " (" + c + ".class)");
		if (Bukkit.getPluginManager().getPlugin("AdepSK").getConfig().getBoolean("broadcastErrors", true)) {
			Bukkit.broadcast(ChatColor.RED + "[AdepSK: WARN]" + ChatColor.GRAY + " v" + getVer() + ": " + s + " (" + c + ".class)", "adepsk.error");
		}
	}

	public static void prSysE(String s, String c, Exception e) {
		if (Bukkit.getPluginManager().getPlugin("AdepSK").getConfig().getBoolean("debug", true)) {
			e.printStackTrace();
		} else {
			prSysE(s, c);
		}
	}

	public static void prSysI(String s) {
		Bukkit.getServer().getLogger().info("[AdepSK] v" + getVer() + ": " + s);
	}

	private static String getVer() {
		return Bukkit.getPluginManager().getPlugin("AdepSK").getDescription().getVersion();
	}

	public static String getFileSize(double i) {
		DecimalFormat df = new DecimalFormat("#.##");
		if (i < 1024) {
			return (i + " B").replaceFirst(".0", "");
		} else if (i < 1048576) {
			return df.format(i / 1024) + " KB";
		} else if (i < 1073741824) {
			return df.format(i / 1048576) + " MB";
		} else if (i < 1099511627776L) {
			return df.format(i / 1073741824) + " GB";
		} else {
			return df.format(i / 1099511627776L) + " TB";
		}
	}

	public static void downloadFile(Path pth, String url) {
		try {
			Files.copy(new URL(url).openStream(), pth);
		} catch (FileAlreadyExistsException x) {
			prSysE("File Already Exists: '" + pth + "' cannot download into a file that already exists!", "Utils", x);
		} catch (Exception x) {
			prSysE("Error downloading from: '" + url + "' Is the site down?", "Utils", x);
		}
	}

	public static String getDefaultPath(String pth) {
		if (!Bukkit.getPluginManager().getPlugin("AdepSK").getConfig().getBoolean("useRootAsDefaultPath", false)) {
			String dp = Paths.get("").normalize().toAbsolutePath().toString();
			if (pth.contains(dp)) {
				return (pth + File.separator);
			} else {
				return (dp + File.separator + pth);
			}
		} else {
			return pth;
		}
	}
}