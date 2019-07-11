package com.yoge.ad.service.search.mysql.directory;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;

/**
 * 文件变化监听器
 * @author geyoujia
 * @date 2019/07/09
 */
public class FileListener implements FileAlterationListener {
    @Override
    public void onStart(FileAlterationObserver fileAlterationObserver) {

    }

    @Override
    public void onDirectoryCreate(File file) {

    }

    @Override
    public void onDirectoryChange(File file) {

    }

    @Override
    public void onDirectoryDelete(File file) {

    }

    @Override
    public void onFileCreate(File file) {

    }

    @Override
    public void onFileChange(File file) {

    }

    @Override
    public void onFileDelete(File file) {

    }

    @Override
    public void onStop(FileAlterationObserver fileAlterationObserver) {

    }
}
