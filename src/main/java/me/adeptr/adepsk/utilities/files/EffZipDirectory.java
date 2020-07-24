package me.adeptr.adepsk.utilities.files;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.Main;
import me.adeptr.adepsk.utilities.files.event.EvtFileZip;

/**
 * Created by tim740 on 22/07/2016
 */
public class EffZipDirectory extends Effect {
	private Expression<String> file, zip;

	@Override
	protected void execute(Event e) {
		Path Dpth = Paths.get(Main.getDefaultPath(file.getSingle(e)));
		Path Fzip = Paths.get(Main.getDefaultPath(zip.getSingle(e)));
		EvtFileZip efz = new EvtFileZip(Fzip, Dpth.toString());
		Bukkit.getServer().getPluginManager().callEvent(efz);
		if (!efz.isCancelled()) {
			try {
				final Map<String, String> env = new HashMap<>();
				env.put("create", "true");
				try (final FileSystem zfs = FileSystems.newFileSystem(
						URI.create("jar:file:/" + Fzip.normalize().toAbsolutePath().toString().replace("\\", "/")),
						env); final Stream<Path> files = Files.walk(Dpth)) {
					final Path rt = zfs.getPath("/");
					files.forEach(cf -> {
						try {
							final Path to = rt.resolve(Dpth.relativize(cf).toString());
							if (Files.isDirectory(cf)) {
								Files.createDirectories(to);
							} else {
								Files.copy(cf, to);
							}
						} catch (Exception x) {
							Main.prSysE(x.getMessage(), getClass().getSimpleName(), x);
						}
					});
				}
			} catch (FileSystemAlreadyExistsException x) {
				Main.prSysE("ZipFile: '" + Fzip + "' already exists!", getClass().getSimpleName(), x);
			} catch (Exception x) {
				Main.prSysE("Directory: '" + Dpth + "' doesn't exist, or doesn't have write permission!",
						getClass().getSimpleName(), x);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] e, int i, Kleenean k, SkriptParser.ParseResult p) {
		file = (Expression<String>) e[0];
		zip = (Expression<String>) e[1];
		return true;
	}

	@Override
	public String toString(@Nullable Event e, boolean b) {
		return getClass().getName();
	}
}
