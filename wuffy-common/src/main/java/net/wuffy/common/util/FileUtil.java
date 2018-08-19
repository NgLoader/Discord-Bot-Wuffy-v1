package net.wuffy.common.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author Ingrim4
 */
public class FileUtil {

	public static Path create(Path path) throws IOException {
		if(Files.isDirectory(path))
			Files.createDirectories(path);
		else {
			Files.createDirectories(path.getParent());
			if(Files.notExists(path))
				Files.createFile(path);
		}
		return path;
	}

	public static void copy(Path source, Path target) throws IOException {
		if(Files.notExists(source))
			return;

		Files.walkFileTree(source, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Path targetPath = target.resolve(source.relativize(file));

				Files.copy(file, targetPath, StandardCopyOption.REPLACE_EXISTING);

				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				Path targetPath = target.resolve(source.relativize(dir));
				if(Files.notExists(targetPath))
					Files.createDirectories(targetPath);

				return FileVisitResult.CONTINUE;
			}
		});
	}

	public static void delete(Path path) throws IOException {
		if(Files.notExists(path))
			return;

		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}
}