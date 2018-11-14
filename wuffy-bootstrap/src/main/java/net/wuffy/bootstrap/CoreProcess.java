package net.wuffy.bootstrap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import net.wuffy.common.Defaults;
import net.wuffy.common.util.ITickable;

public class CoreProcess implements ITickable {

	private CoreConfig coreConfig;

	private File path;

	private Process process;

	private Consumer<CoreProcess> onStop;

	public CoreProcess(CoreConfig coreConfig, Consumer<CoreProcess> onStop) {
		this.coreConfig = coreConfig;
		this.onStop = onStop;

		this.path = Paths.get(String.format("./work/%s", this.coreConfig.uuid.toString())).toFile();
	}

	public void start() throws IOException {
		List<String> command = new ArrayList<>(Arrays.asList(
				Defaults.JAVA.toString(),
				"-jar", "wuffy.jar" ));
		command.addAll(this.coreConfig.parameters);

		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.directory(path);
		processBuilder.redirectError(Paths.get(this.path.getPath(), "error.log").toFile());
		processBuilder.redirectInput(Paths.get(this.path.getPath(), "info.log").toFile());
		this.process = processBuilder.start();
	}

	@Override
	public void update() {
		if(this.process != null && !this.process.isAlive()) {
			this.process = null;

			this.onStop.accept(this);
		}
	}

	public boolean isRunning() {
		return this.process != null;
	}

	public void stop() {
		this.exec("exit");
	}

	public void exec(String command) {
		if(this.process == null)
			return;

		try {
			process.getOutputStream().write(String.format("%s\n", command).getBytes());
			process.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}