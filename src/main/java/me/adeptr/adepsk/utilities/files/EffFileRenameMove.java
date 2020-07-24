package me.adeptr.adepsk.utilities.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.Main;
import me.adeptr.adepsk.utilities.files.event.EvtFileCopy;
import me.adeptr.adepsk.utilities.files.event.EvtFileMove;
import me.adeptr.adepsk.utilities.files.event.EvtFileRename;

/**
 * Created by tim740 on 21/03/2016
 */
public class EffFileRenameMove extends Effect {
	private Expression<String> path, name;
	private int ty;

	@Override
	protected void execute(Event e) {
		Path pth = Paths.get(Main.getDefaultPath(path.getSingle(e)));
		try {
			if (ty == 0) {
				EvtFileRename efn = new EvtFileRename(pth, name.getSingle(e));
				Bukkit.getServer().getPluginManager().callEvent(efn);
				if (!efn.isCancelled()) {
					pth.toFile().renameTo(new File(Main
							.getDefaultPath(path.getSingle(e).replaceAll(pth.toFile().getName(), name.getSingle(e)))));
				}
			} else if (ty == 1) {
				EvtFileMove efm = new EvtFileMove(pth, name.getSingle(e));
				Bukkit.getServer().getPluginManager().callEvent(efm);
				if (!efm.isCancelled()) {
					Files.move(pth, Paths
							.get(Main.getDefaultPath(name.getSingle(e) + File.separator + pth.toFile().getName())));
				}
			} else if (ty == 2) {
				EvtFileCopy efc = new EvtFileCopy(pth, name.getSingle(e));
				Bukkit.getServer().getPluginManager().callEvent(efc);
				if (!efc.isCancelled()) {
					Files.copy(pth, Paths
							.get(Main.getDefaultPath(name.getSingle(e) + File.separator + pth.toFile().getName())));
				}
			} else if (ty == 3) {
				EvtFileMove efm = new EvtFileMove(pth, name.getSingle(e));
				Bukkit.getServer().getPluginManager().callEvent(efm);
				if (!efm.isCancelled()) {
					copyDir(pth, Paths
							.get(Main.getDefaultPath(name.getSingle(e) + File.separator + pth.toFile().getName())));
					Files.walkFileTree(pth, new SimpleFileVisitor<Path>() {
						@Override
						public FileVisitResult visitFile(Path f, BasicFileAttributes attrs) throws IOException {
							Files.delete(f);
							return FileVisitResult.CONTINUE;
						}

						@Override
						public FileVisitResult postVisitDirectory(Path d, IOException exc) throws IOException {
							Files.delete(d);
							return FileVisitResult.CONTINUE;
						}
					});
				}
			} else {
				EvtFileCopy efc = new EvtFileCopy(pth, name.getSingle(e));
				Bukkit.getServer().getPluginManager().callEvent(efc);
				if (!efc.isCancelled()) {
					copyDir(pth, Paths
							.get(Main.getDefaultPath(name.getSingle(e) + File.separator + pth.toFile().getName())));
				}
			}
		} catch (Exception x) {
			Main.prSysE("File/Directory: '" + pth + "' doesn't exist!", getClass().getSimpleName(), x);
		}
	}

	private void copyDir(Path pth, Path pf) throws IOException {
		Files.walk(pth).forEach(mpath -> {
			try {
				Files.copy(mpath, Paths.get(mpath.toString().replace(pth.toString(), pf.toString())));
			} catch (IOException x) {
				Main.prSysE(x.getMessage(), getClass().getSimpleName(), x);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] e, int i, Kleenean k, ParseResult p) {
		path = (Expression<String>) e[0];
		name = (Expression<String>) e[1];
		ty = p.mark;
		return true;
	}

	@Override
	public String toString(@Nullable Event e, boolean b) {
		return getClass().getName();
	}
}