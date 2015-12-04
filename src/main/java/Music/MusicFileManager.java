package Music;

import FileIO.FileIO;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MusicFileManager {

    // Singleton Pattern
    private static MusicFileManager uniqueInstance;

    private final String FILE_INFO_ADDRESS = System.getProperty("user.home") + "/Desktop/";
    private final String FILE_INFO_NAME = "abc";//"MusicInfoFile";
    private ArrayList<MusicFile> musicFileList = new ArrayList<MusicFile>();
    private RecentPlayList recentPlayList = new RecentPlayList();
    private FavoriteMusicList favoriteMusicList = new FavoriteMusicList();
    private ArrayList<MusicFile> choosePlayList = new ArrayList<MusicFile>();

    public static MusicFileManager getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new MusicFileManager();
        }
        return uniqueInstance;
    }

    public void addMusicFileInDirectory(final String fileAddress) {
        ArrayList<String> musicFileNameList = FileIO.readAllFileInPath(fileAddress);
        FileIO.makeDirectory(FILE_INFO_ADDRESS);
        for (String iter : musicFileNameList) {
            String fileName = iter;
            try {
                musicFileList.add(new MusicFile(fileName, fileAddress, getMusicInfoFile(fileName, fileAddress)));
            } catch (UnsupportedTagException | IOException | InvalidDataException e) {
                e.printStackTrace();
            }
        }
        ArrayList<String> infoFileInfo = new ArrayList<String>();

        for (MusicFile iter : musicFileList) {
            infoFileInfo.add(iter.getSaveInfo());
        }

        FileIO.writeTextFile(FILE_INFO_ADDRESS, FILE_INFO_NAME, infoFileInfo, "");
        favoriteMusicList.FSort();

    }

    private String[] getMusicInfoFile(final String fileName, final String fileAddress) {
        ArrayList<String> informationString = FileIO.readTextFile(FILE_INFO_ADDRESS, FILE_INFO_NAME);
        String[] information = new String[5];

        for (String iter : informationString) {
            information = iter.split("/");
            if (information[1].equals(fileName)) {
                return information;
            }
        }
        information[0] = "0";
        information[1] = fileName;
        information[2] = fileAddress;
        information[3] = "null";
        information[4] = "null";
        return information;
    }

    public void setMusicFileInformation() {
        String[] writeInformation = null;
        //FileIO.writeTextFile(this.fileInformationAddress, fileName,
        //		writeInformation, MUSICINFODELIMITER);
    }

    //~~~~~~~~~~~~~~ Getter & Setter
    public ArrayList<MusicFile> getMusicFileList() {
        return musicFileList;
    }

    public void setMusicFileList(ArrayList<MusicFile> musicFileList) {
        this.musicFileList = musicFileList;
    }

    public RecentPlayList getRecentPlayList() {
        return recentPlayList;
    }

    public void setRecentPlayList(RecentPlayList recentPlayList) {
        this.recentPlayList = recentPlayList;
    }

    public FavoriteMusicList getFavoriteFileList() {
        return favoriteMusicList;
    }

    public void setFavoriteFileList(FavoriteMusicList favoriteMusicList) {
        this.favoriteMusicList = favoriteMusicList;
    }

    public ArrayList<MusicFile> getChoosePlayList() {
        return choosePlayList;
    }

    public void setChoosePlayList(ArrayList<MusicFile> choosePlayList) {
        this.choosePlayList = choosePlayList;
    }

    public int getMusicFileNum() {
        return this.musicFileList.size();
    }
}