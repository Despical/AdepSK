package me.adeptr.adepsk.utilities.files;

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
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.Main;
import me.adeptr.adepsk.utilities.files.event.EvtFileDeletion;

/**
 * Created by tim740 on 16/03/2016
 */
public class EffDeleteFile extends Effect {
	private Expression<String> path;
	private int ty;

	@Override
	protected void execute(Event e) {
		Path pth = Paths.get(Main.getDefaultPath(path.getSingle(e)));
		EvtFileDeletion efd = new EvtFileDeletion(pth);
		Bukkit.getServer().getPluginManager().callEvent(efd);
		if (!efd.isCancelled()) {
			if (ty == 0) {
				try {
					Files.delete(pth);
				} catch (Exception x) {
					Main.prSysE("File: '" + pth + "' doesn't exist!", getClass().getSimpleName(), x);
				}
			} else {
				try {
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
				} catch (Exception x) {
					Main.prSysE("Directory: '" + pth + "' doesn't exist!", getClass().getSimpleName(), x);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] e, int i, Kleenean k, SkriptParser.ParseResult p) {
		path = (Expression<String>) e[0];
		ty = p.mark;
		return true;
	}

	@Override
	public String toString(@Nullable Event e, boolean b) {
		return getClass().getName();
	}
}
