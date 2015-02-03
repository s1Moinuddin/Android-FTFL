package com.shuvo.musicplayer.service;

import java.util.ArrayList;
import java.util.Random;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.shuvo.musicplayer.AudioActivity;
import com.shuvo.musicplayer.R;
import com.shuvo.musicplayer.util.Song;

//Create a Service

public class MusicService extends Service implements
		MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
		MediaPlayer.OnCompletionListener {

	// media player
	private MediaPlayer player;
	// song list
	private ArrayList<Song> songs;
	// current position
	private int songPosn;
	private String songTitle="";
	private static final int NOTIFY_ID = 1;
	private final IBinder musicBind = new MusicBinder();
	//for shuffle song list
	private boolean shuffle=false;
	private Random rand;

	public void onCreate() {
		// create the service
		super.onCreate();
		// initialize position
		songPosn = 0;
		// create player
		player = new MediaPlayer();

		initMusicPlayer();
		
		rand=new Random();

	}

	public void initMusicPlayer() {
		// set player properties
		player.setWakeMode(getApplicationContext(),
				PowerManager.PARTIAL_WAKE_LOCK);
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);

		player.setOnPreparedListener(this);
		player.setOnCompletionListener(this);
		player.setOnErrorListener(this);	
	}

	public void setList(ArrayList<Song> theSongs) {
		this.songs = theSongs;
	}

	/*
	 * This will form part of the interaction between the Activity and Service
	 * classes, for which we also need a Binder instance.
	 */
	public class MusicBinder extends Binder {
		public MusicService getService() {
			return MusicService.this;
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return musicBind;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		player.stop();
		player.release();
		return false;
	}

	// Begin Playback
	public void playSong() {
		// play a song
		player.reset(); /* we will also use this code when the user is playing
						 subsequent songs */
		//get song
		Song playSong = songs.get(songPosn);
		//get title
		songTitle=playSong.getTitle();
		//get id
		long currSong = playSong.getID();
		//set uri
		Uri trackUri = ContentUris.withAppendedId(
		  android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
		  currSong);
		
		try{
			  player.setDataSource(getApplicationContext(), trackUri);
			}
			catch(Exception e){
			  Log.e("MUSIC SERVICE", "Error setting data source", e);
			}
		//calling the asynchronous method of the MediaPlayer to prepare it
		player.prepareAsync();
	}
	

	@Override
	public void onCompletion(MediaPlayer mp) {
		
		if(player.getCurrentPosition()>0){
		    mp.reset();
		    playNext();
		  }
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
	  mp.reset();
	  return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		//start playback
		  player.start();
		  Intent notIntent = new Intent(this, AudioActivity.class);
		  notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		  PendingIntent pendInt = PendingIntent.getActivity(this, 0,
		    notIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		   
		  Notification.Builder builder = new Notification.Builder(this);
		   
		  builder.setContentIntent(pendInt)
		    .setSmallIcon(R.drawable.play)
		    .setTicker(songTitle)
		    .setOngoing(true)
		    .setContentTitle("Playing")
		    .setContentText(songTitle);
		
		@SuppressWarnings("deprecation")
		Notification not = builder.getNotification();
		   
		  startForeground(NOTIFY_ID, not);
	}
	
	@Override
	public void onDestroy() {
	  stopForeground(true);
	}
	//In order for the user to select songs
	public void setSong(int songIndex){
		  songPosn=songIndex;
		}
	
	public int getPosn(){
		  return player.getCurrentPosition();
		}
		 
		public int getDur(){
		  return player.getDuration();
		}
		 
		public boolean isPng(){
		  return player.isPlaying();
		}
		 
		public void pausePlayer(){
		  player.pause();
		}
		 
		public void seek(int posn){
		  player.seekTo(posn);
		}
		 
		public void go(){
		  player.start();
		}
		
		public void playPrev(){
			  songPosn--;
			  if(songPosn<0)
				  songPosn = songs.size()-1;
			  playSong();
			}
		//skip to next
		public void playNext(){
			
			if(shuffle){
			    int newSong = songPosn;
			    while(newSong==songPosn){
			      newSong=rand.nextInt(songs.size());
			    }
			    songPosn=newSong;
			  }
			  else{
			    songPosn++;
			    if(songPosn>=songs.size())
			    	songPosn=0;
			  }
			  playSong();
		}
		
		public void setShuffle(){
			  if(shuffle) shuffle=false;
			  else shuffle=true;
			}
	
	

}
