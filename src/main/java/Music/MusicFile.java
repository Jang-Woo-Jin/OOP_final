package Music;

import java.util.ArrayList;
import java.io.*;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import FileIO.FileIO;

public class MusicFile extends Mp3File {
	private int playCount;

	private String fileName, fileAddress, fileInformationAddress;

	private Lyric lyrics;
	private String lyricsFileAddress, lyricsFileName;

	private ArrayList<String> recentPlay;

	private final String MUSICINFODELIMITER = "/";
	private final String INFOFILEADDRESS = "";

	private String singer, composer, name, album, genre, comment;
	private byte[] imagedata;
	
	
	private ID3v1 id3v1Tag;
	private ID3v2 id3v2Tag;
	private boolean isV1Tag = false, isV2Tag = false;

	public MusicFile(String musicFileName, String musicFileAddress) throws UnsupportedTagException, InvalidDataException, IOException {
		super(musicFileAddress+"//"+musicFileName+".mp3");
		if (this.hasId3v1Tag()){
			isV1Tag = true;
			id3v1Tag = this.getId3v1Tag();
		}
		if (this.hasId3v2Tag()){
			isV2Tag = true;
			id3v2Tag = this.getId3v2Tag();
		}
		this.fileAddress = musicFileAddress;
		this.fileName = musicFileName;
		setMusicInformation();
		
	}
	public MusicFile(String a){
		this.fileName = fileName;
	}
	public String[] getMusicFileInformation() {
		String[] tempInformation = FileIO.readTextFile(fileInformationAddress, getName());
		String[] information = tempInformation[0].split("/");
		return information;
	}

	public void setMusicFileInformation() {
		String[] writeInformation = null;
		FileIO.writeTextFile(this.fileInformationAddress, fileName,
				writeInformation, MUSICINFODELIMITER);
	}

	public void setMusicInformation() {
		if(isV1Tag){
			id3v1Tag.getArtist();
		}
		if(isV2Tag){
			
		}
		setMusicFileInformation();
	}

	// ~~~~~~~~~~~~~~~ relation with recent play time
	public void addRecentPlay(String information) {
		recentPlay.add(information);
	}
	public void deleteRecentPlay(int index) {
		recentPlay.remove(index);
	}
	public void deleteRecentPlay(String information) {
		recentPlay.remove(information);
	}
	public void deleteAllRecentPlay() {
		recentPlay.clear();
	}
	
	// ~~~~~~~~~~~~~~~~ Getter & Setter for private value
	public Lyric getLyrics() {
		return lyrics;
	}
	public String getLyricsFileAddress() {
		return lyricsFileAddress;
	}
	public String getLyricsFileName() {
		return lyricsFileName;
	}
	public void setLyrics(Lyric lyrics, String lyricsFileName,
			String lyricsFileAddress) {
		this.lyrics = lyrics;
		this.lyricsFileName = lyricsFileName;
		this.lyricsFileAddress = lyricsFileAddress;
	}
	public ArrayList<String> getRecentPlay() {
		return recentPlay;
	}
	public void setRecentPlay(ArrayList<String> recentPlay) {
		this.recentPlay = recentPlay;
	}
	public int getPlayCount() {
		return this.playCount;
	}
	public void setPlayCount(int playCount) {
		this.playCount = playCount;
	}

	public String getName(){
		return this.fileName;
    }

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public String getComposer() {
		return composer;
	}

	public void setComposer(String composer) {
		this.composer = composer;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public byte[] getImagedata() {
		return imagedata;
	}

	public void setImagedata(byte[] imagedata) {
		this.imagedata = imagedata;
	}

}