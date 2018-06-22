package de.ngloader.core.util;

import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.sun.management.OperatingSystemMXBean;

/**
 * 
 * @author Ingrim4
 */
public class HardwareUtil {

	private static final OperatingSystemMXBean OS_BEAN = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

	public static int getAvailableProcessors() {
		return OS_BEAN.getAvailableProcessors();
	}

	public static double getCpuLoad() {
		var cpuLoad = OS_BEAN.getSystemCpuLoad();
		return cpuLoad < 0 ? 0.0D : cpuLoad;
	}

	public static double getAverageCpuLoad() {
		var cpuLoad = OS_BEAN.getSystemLoadAverage();
		return cpuLoad < 0 ? 0.0D : cpuLoad;
	}

	public static long getTotalMemory() {
		return OS_BEAN.getTotalPhysicalMemorySize();
	}

	public static long getFreeMemory() {
		return OS_BEAN.getFreePhysicalMemorySize();
	}

	public static long getTotalSwap() {
		return OS_BEAN.getTotalSwapSpaceSize();
	}

	public static long getFreeSwap() {
		return OS_BEAN.getFreeSwapSpaceSize();
	}

	public static long getTotalDiskSpace() {
		try {
			var fileStore = Files.getFileStore(Paths.get(""));
			return fileStore.getTotalSpace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0L;
	}

	public static long getFreeDiskSpace() {
		try {
			var fileStore = Files.getFileStore(Paths.get(""));
			return fileStore.getUsableSpace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0L;
	}
}
