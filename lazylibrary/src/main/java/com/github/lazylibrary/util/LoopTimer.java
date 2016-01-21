/*
 * Copyright (C) 2013 Peng fei Pan <sky@xiaopan.me>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.lazylibrary.util;

import android.os.Handler;

/**
 * 循环定时器
 */
public class LoopTimer {
	private int intervalMillis;	//间隔时间
	private boolean running;	//运行状态
	private Handler handler;	//消息处理器
	private ExecuteRunnable executeRunnable;	//执行Runnable
	
	/**
	 * 创建一个循环定时器
	 * @param handler
	 * @param runnable
	 * @param intervalMillis
	 */
	public LoopTimer(Handler handler, Runnable runnable, int intervalMillis) {
		this.handler = handler;
		this.intervalMillis = intervalMillis;
		executeRunnable = new ExecuteRunnable(runnable);
	}
	
	/**
	 * 创建一个循环定时器
	 * @param runnable
	 * @param intervalMillis
	 */
	public LoopTimer(Runnable runnable, int intervalMillis) {
		this(new Handler(), runnable, intervalMillis);
	}
	
	/**
	 * 立即启动
	 */
	public void start(){
		running = true;
		handler.removeCallbacks(executeRunnable);
		handler.post(executeRunnable);
	}
	
	/**
	 * 延迟指定间隔毫秒启动
	 */
	public void delayStart(){
		running = true;
		handler.removeCallbacks(executeRunnable);
		handler.postDelayed(executeRunnable, intervalMillis);
	}
	
	/**
	 * 立即停止
	 */
	public void stop(){
		running = false;
		handler.removeCallbacks(executeRunnable);
	}

	/**
	 * 获取间隔毫秒
	 * @return
	 */
	public int getIntervalMillis() {
		return intervalMillis;
	}

	/**
	 * 设置间隔毫秒
	 * @param intervalMillis
	 */
	public void setIntervalMillis(int intervalMillis) {
		this.intervalMillis = intervalMillis;
	}

	/**
	 * 是否正在运行
	 * @return
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * 设置消息处理器
	 * @param handler
	 */
	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	/**
	 * 设置执行内容
	 * @param runnable
	 */
	public void setRunnable(Runnable runnable) {
		executeRunnable.setRunnable(runnable);
	}
	
	/**
	 * 执行Runnable
	 */
	private class ExecuteRunnable implements Runnable {
		private Runnable runnable;
		
		public ExecuteRunnable(Runnable runnable){
			this.runnable = runnable;
		}
		
		@Override
		public void run() {
			if(running && runnable != null){
				runnable.run();
			}
		}

		public void setRunnable(Runnable runnable) {
			this.runnable = runnable;
		}
	}
}
