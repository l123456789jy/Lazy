package com.github.lazylibrary.util;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

/**
 * 录音
 */
public class RecorderControl {

	//录音
	private MediaRecorder mRecorder = null;
	/** 采样率 */
	private static int SAMPLE_RATE_IN_HZ = 8000;
	//播放录音
	private MediaPlayer mPlayer = null;
	private boolean playState = false; // 录音的播放状态
	private String mFileName;



	public MediaPlayer getmPlayer() {
		return mPlayer;
	}
	public void setmPlayer(MediaPlayer mPlayer) {
		this.mPlayer = mPlayer;
	}

	public RecorderControl(){

	}
	public RecorderControl(String name) {
		if(mRecorder == null){
			mRecorder = new MediaRecorder();
		}
		if(mPlayer == null){
			mPlayer = new MediaPlayer();
		}
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/menmen";
		//		mFileName += "/" + name + ".3gp";
		//		mFileName += "/" + name + ".mp3";
		mFileName += "/" + name + ".arm";
	}

	/*
	 * 开始录音
	 */
	public void startRecording() {
		// 实例化MediaRecorder
		if(mRecorder == null){
			mRecorder = new MediaRecorder();
		}
		// 设置音频源为MIC
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		// 设置输出文件的格式
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
		//		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		// 设置输出文件的名称
		mRecorder.setOutputFile(mFileName);
		//设置音频的编码格式
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		//		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
		//设置采样率
		mRecorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);

		try {
			//得到设置的音频来源，编码器，文件格式等等内容，在start()之前调用
			mRecorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			//开始录音
			mRecorder.start();
		} catch (Exception e) {
			mRecorder = null;
			mRecorder = new MediaRecorder();
		}
	}


	/**
	 *
	 * @return  String
	 */
	/*
	 * 停止录音
	 */
	public String stopRecording(){

		try {
			mRecorder.stop();
		} catch (Exception e) {
			//释放资源
			mRecorder = null;
			mRecorder = new MediaRecorder();
		}
		//释放资源
		mRecorder.release();
		mRecorder = null;

		return mFileName;
	}


	/**
	 *
	 * @param Filename  Filename
	 * @param completion  completion
	 */
	//
	public void startPlaying(String Filename,OnCompletionListener completion){

		if(!playState){
			if(mPlayer == null){
				mPlayer = new MediaPlayer();
			}

			try {
				mPlayer.setDataSource(Filename);
				mPlayer.prepare();
				playState = true;
				mPlayer.start();
				mPlayer.setOnCompletionListener(completion);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			if(mPlayer.isPlaying()){
				mPlayer.stop();
				playState = false;
				//				startPlaying(Filename);
			} else {
				playState = false;
			}

		}
	}
	/**
	 * 播放完后释放资源
	 */
	public void playingFinish(){
		Log.i("spoort_list", "RecorderControl 播放结束释放资源");
		if(playState){
			playState = false;
		}
		mPlayer.release();
		mPlayer = null;
	}

	/**
	 * 停止播放
	 * @return    boolean
	 */
	public boolean stopPlaying(){

		if(mPlayer!=null){
			//			if(mPlayer!=null&&mPlayer.isPlaying()){
			Log.i("spoort_list", "RecorderControl mPlayer.stop()");
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
			playState = false;
			return true;
		}else{
			Log.i("spoort_list", "RecorderControl mPlayer.stop() is null");
			return false;
		}
	}
	//当文件播放结束后调用此方法
	//	OnCompletionListener completion = new OnCompletionListener() {
	//		@Override
	//		public void onCompletion(MediaPlayer mp) {
	////			if(playState){
	////				playState = false;
	////			}
	//			Log.i("spoort_list", "RecorderControl 播放结束");
	//			mPlayer.release();
	//			mPlayer = null;
	//		}
	//	};

	/**
	 * 获取音量的大小
	 * @return   double
	 */
	public double getAmplitude() {
		if(mRecorder!=null){
			return (mRecorder.getMaxAmplitude());
		}else
			return 0;
	}
}
