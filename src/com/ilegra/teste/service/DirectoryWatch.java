package com.ilegra.teste.service;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import com.ilegra.teste.controller.FileController;

public class DirectoryWatch {

	public static final String DIRECTORY_TO_WATCH = "data/in";
	
	
	public DirectoryWatch() {
		
	}
	
	public void start(){
		Path toWatch = Paths.get(DIRECTORY_TO_WATCH);
		if (toWatch == null) {
			throw new UnsupportedOperationException("Directory not found");
		}

		try {

			WatchService myWatcher = toWatch.getFileSystem().newWatchService();

			MyWatchQueueReader fileWatcher = new MyWatchQueueReader(myWatcher);
			Thread th = new Thread(fileWatcher, "FileWatcher");
			th.start();

			toWatch.register(myWatcher, ENTRY_CREATE, ENTRY_MODIFY);
			th.join();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static class MyWatchQueueReader implements Runnable {

		/** the watchService that is passed in from above */
		private WatchService myWatcher;
		public FileController fc;
		
		public MyWatchQueueReader(WatchService myWatcher) {
			this.myWatcher = myWatcher;
			fc = new FileController();
		}

		@SuppressWarnings("rawtypes")
		@Override
		public void run() {
			try {

				WatchKey key = myWatcher.take();
				while (key != null) {

					for (WatchEvent event : key.pollEvents()) {
						System.out.printf("Received %s event for file: %s\n", event.kind(), event.context());
						if(event.kind().equals(ENTRY_CREATE)){

							fc.readFile(event.context().toString());
						}
					}
					key.reset();
					key = myWatcher.take();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Stopping thread");
		}
	}

}
