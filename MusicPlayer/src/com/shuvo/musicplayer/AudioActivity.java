package com.shuvo.musicplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.shuvo.musicplayer.adapter.SongAdapter;
import com.shuvo.musicplayer.controller.MusicController;
import com.shuvo.musicplayer.service.MusicService;
import com.shuvo.musicplayer.service.MusicService.MusicBinder;
import com.shuvo.musicplayer.util.Song;

import android.widget.MediaController.MediaPlayerControl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class AudioActivity extends Activity implements MediaPlayerControl {

	private ArrayList<Song> songList;
	private ListView songView;
	// Start the Service
	private MusicService musicSrv;
	private Intent playIntent;
	private boolean musicBound = false;
	// media controller
	private MusicController controller;
	private boolean paused = false, playbackPaused = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio);

		songView = (ListView) findViewById(R.id.song_list);
		songList = new ArrayList<Song>();

		getSongList();
		Collections.sort(songList, new Comparator<Song>() {
			public int compare(Song a, Song b) {
				return a.getTitle().compareTo(b.getTitle());
			}
		});

		SongAdapter songAdt = new SongAdapter(this, songList);
		songView.setAdapter(songAdt);

		setController();

	}

	public void getSongList() {
		// retrieve song info
		ContentResolver musicResolver = getContentResolver();
		Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor musicCursor = musicResolver.query(musicUri, null, null, null,
				null);

		if (musicCursor != null && musicCursor.moveToFirst()) {
			// get columns
			int titleColumn = musicCursor
					.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
			int idColumn = musicCursor
					.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
			int artistColumn = musicCursor
					.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);
			// add songs to list
			do {
				long thisId = musicCursor.getLong(idColumn);
				String thisTitle = musicCursor.getString(titleColumn);
				String thisArtist = musicCursor.getString(artistColumn);
				songList.add(new Song(thisId, thisTitle, thisArtist));
			} while (musicCursor.moveToNext());
		}

		musicCursor.close();
	}

	// connect to the service
	private ServiceConnection musicConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			MusicBinder binder = (MusicBinder) service;
			// get service
			musicSrv = binder.getService();
			// pass list
			musicSrv.setList(songList);
			musicBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			musicBound = false;
		}
	};

	@Override
	protected void onStart() {
		super.onStart();
		if (playIntent == null) {
			playIntent = new Intent(this, MusicService.class);
			bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
			startService(playIntent);
		}
	}

	/*
	 * We will use these to cope with the user returning to the app after
	 * leaving it and interacting with the controls when playback itself is
	 * paused.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		paused = true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (paused) {
			setController();
			paused = false;
		}
	}

	/*
	 * This will ensure that the controller displays when the user returns to
	 * the app. Override onStop to hide it
	 */
	@Override
	protected void onStop() {
		controller.hide();
		super.onStop();
	}

	/*
	 * We set the song position as the tag for each item in the list view when
	 * we defined the Adapter class. We retrieve it here and pass it to the
	 * Service instance before calling the method to start the playback.
	 */
	public void songPicked(View view){
		  musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
		  musicSrv.playSong();
		  if(playbackPaused){
		    setController();
		    playbackPaused=false;
		  }
		  controller.show(0);
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.audio, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// menu item selected
		switch (item.getItemId()) {
		case R.id.action_shuffle:
			musicSrv.setShuffle();
			break;
		case R.id.action_end:
			stopService(playIntent);
			musicSrv = null;
			System.exit(0);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * Pressing the back button will not exit the app, since we will assume that
	 * the user wants playback to continue unless they select the end button.
	 */
	@Override
	protected void onDestroy() {
		stopService(playIntent);
		musicSrv = null;
		super.onDestroy();
	}

	private void setController() {
		// set the controller up
		controller = new MusicController(this);
		controller.setPrevNextListeners(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				playNext();
			}
		}, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				playPrev();
			}
		});

		controller.setMediaPlayer(this);
		controller.setAnchorView(findViewById(R.id.song_list));
		controller.setEnabled(true);
	}

	/*
	 * If the user interacts with the controls while playback is paused, the
	 * MediaPlayer object may behave unpredictably. To cope with this, we will
	 * set and use the playbackPaused flag
	 */
	// play next
	private void playNext() {
		musicSrv.playNext();
		if (playbackPaused) {
			setController();
			playbackPaused = false;
		}
		controller.show(0);
	}

	// play previous
	private void playPrev() {
		musicSrv.playPrev();
		if (playbackPaused) {
			setController();
			playbackPaused = false;
		}
		controller.show(0);
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		musicSrv.go();
	}

	@Override
	public void pause() {
	  playbackPaused = true;
	  musicSrv.pausePlayer();
	}

	@Override
	public int getDuration() {
		// TODO Auto-generated method stub
		if (musicSrv != null && musicBound && musicSrv.isPng())
			return musicSrv.getDur();
		else
			return 0;
	}

	@Override
	public int getCurrentPosition() {
		// TODO Auto-generated method stub
		if (musicSrv != null && musicBound && musicSrv.isPng())
			return musicSrv.getPosn();
		else
			return 0;
	}

	@Override
	public void seekTo(int pos) {
		// TODO Auto-generated method stub
		musicSrv.seek(pos);
	}

	@Override
	public boolean isPlaying() {
		// TODO Auto-generated method stub
		if (musicSrv != null && musicBound)
			return musicSrv.isPng();
		return false;
	}

	@Override
	public int getBufferPercentage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canPause() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canSeekForward() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getAudioSessionId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
