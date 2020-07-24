package me.adeptr.adepsk.utilities.files;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.Main;
import me.adeptr.adepsk.utilities.files.event.EvtUnzip;

/**
 * Created by tim740 on 19/03/2016
 */
public class EffUnzip extends Effect {
	private Expression<String> file, zip;

	@Override
	protected void execute(Event e) {
		Path Fzip = Paths.get(Main.getDefaultPath(zip.getSingle(e)));
		Path pth = Paths.get(Main.getDefaultPath(file.getSingle(e)));
		EvtUnzip euz = new EvtUnzip(Fzip, pth.toString());
		Bukkit.getServer().getPluginManager().callEvent(euz);
		if (!euz.isCancelled()) {
			try {
				final Map<String, String> env = new HashMap<>();
				env.put("create", "true");
				if (Files.notExists(pth)) {
					Files.createDirectories(pth);
				}
				try (FileSystem zfs = FileSystems.newFileSystem(
						URI.create("jar:file:/" + Fzip.normalize().toAbsolutePath().toString().replace("\\", "/")),
						env)) {
					Files.walkFileTree(zfs.getPath("/"), new SimpleFileVisitor<Path>() {
						@Override
						public FileVisitResult visitFile(Path ftc, BasicFileAttributes ats) throws IOException {
							Files.copy(ftc, Paths.get(pth.toString(), ftc.toString()),
									StandardCopyOption.REPLACE_EXISTING);
							return FileVisitResult.CONTINUE;
						}

						@Override
						public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes ats) throws IOException {
							final Path dtc = Paths.get(pth.toString(), dir.toString());
							if (Files.notExists(dtc)) {
								Files.createDirectory(dtc);
							}
							return FileVisitResult.CONTINUE;
						}
					});
				}
			} catch (Exception x) {
				Main.prSysE("ZipFile: '" + Fzip + "' doesn't exist, or doesn't have write permission!",
						getClass().getSimpleName(), x);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] e, int i, Kleenean k, SkriptParser.ParseResult p) {
		zip = (Expression<String>) e[0];
		file = (Expression<String>) e[1];
		return true;
	}

	@Override
	public String toString(@Nullable Event e, boolean b) {
		return getClass().getName();
	}
}
