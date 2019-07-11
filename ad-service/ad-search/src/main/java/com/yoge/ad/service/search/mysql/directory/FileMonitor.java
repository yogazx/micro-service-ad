package com.yoge.ad.service.search.mysql.directory;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

/**
 * 文件夹监控入口
 * @author geyoujia
 * @date 2019/07/09
 */
public class FileMonitor {

    FileAlterationMonitor fileAlterationMonitor = null;

    /**
     *
     * @param interval 监控时间间隔
     */
    public FileMonitor(long interval) {
        fileAlterationMonitor = new FileAlterationMonitor(interval);
    }

    public void monitor(String path, FileAlterationListener listener) {
        FileAlterationObserver observer = new FileAlterationObserver(new File(path));
        fileAlterationMonitor.addObserver(observer);
        observer.addListener(listener);
    }

    public void stop() throws Exception {
        fileAlterationMonitor.stop();
    }

    public void start() throws Exception {
        fileAlterationMonitor.start();
    }

}
