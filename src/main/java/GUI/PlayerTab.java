package GUI;

import FileIO.FilePathParser;
import Music.CurrentMusic;
import Music.Lyric_Repeat;
import Music.Music;
import Music.MusicListManager;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class PlayerTab extends JPanel {

    public static JLabel text;
    private final JFXPanel fxPanel = new JFXPanel();
    private final JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 30, 10));
    /* Music info -> Image, name */
    private JPanel musicInfoPanel;
    private JLabel musicName;
    private Image musicImage;
    private JLabel musicImageLabel;
    /* Buttons */
    private JButton playButton;
    private JButton seekNextButton;
    private JButton seekPreviousButton;
    private JButton stopButton;
    private JButton playModeButton;
    private JButton starButton; // for favorite
    private JSlider volumeSlider;
    private JSlider currentTimeSlider;
    private Tab tabPanel;

    public PlayerTab() {

        this.setPreferredSize(new Dimension(240, 300));
        this.setBackground(Color.BLACK);

        addImageLabel();

        addCurrentTimeSlider();

        addSeekPreviousButton();
        addPlayButton();
        addSeekNextButton();

        addPlayModeButton();
        addStopButton();
        addStarButton();

        buttonPanel.setBackground(Color.black);
        this.add(buttonPanel);
        addVolumeSlider();
        //TODO!!!!!
        addLyric();

        Platform.runLater(() -> initFX(fxPanel));
        fxPanel.setSize(0, 0);

        this.add(fxPanel);
        this.setVisible(true);
    }

    private void initFX(JFXPanel fxPanel) {
        Scene scene = initScene();
        fxPanel.setScene(scene);
    }                                   // can play media using javafx scene and mediaplayer

    private Scene initScene() {
        Group root = new Group();

        return (new Scene(root, javafx.scene.paint.Color.GREENYELLOW));
    }

    //add whole Buttons
    private void addButtonImage(JButton button, String imageFileName) throws IOException {
        Image buttonImage = ImageIO.read(new File("./"+ imageFileName));
        buttonImage = buttonImage.getScaledInstance(20, 20, Image.SCALE_DEFAULT);
        button.setIcon(new ImageIcon(buttonImage));

    }

    private void addPlayButton() {
        playButton = new JButton();
        
        /* buttons setting */
        try {
            addButtonImage(playButton, "play.jpg");
        } catch (IOException e) {
            playButton.setText("▶");
        }

        playButton.addActionListener(e -> {
            CurrentMusic currentMusic = CurrentMusic.getInstance();
            if (currentMusic.isPlayable()) {
                CurrentMusic.getInstance().play();
                try {
                    addButtonImage(playButton, "pause.png");
                } catch (Exception k) {
                    playButton.setText("||");
                }
                MusicListManager.getInstance().addToRecentPlayList(CurrentMusic.getInstance().toMusic());
            } else {
                CurrentMusic.getInstance().pause();
                reset();
            }
        });
        buttonPanel.add(playButton);
    }

    private void addSeekNextButton() {
        seekNextButton = new JButton();
        try {
            addButtonImage(seekNextButton, "seek-next.png");
        } catch (Exception e) {
            seekNextButton.setText(">>");
        }

        seekNextButton.addActionListener(e -> {
            CurrentMusic currentMusic = CurrentMusic.getInstance();
            currentMusic.seekNext();
        });
        buttonPanel.add(seekNextButton);
    }

    private void addSeekPreviousButton() {
        seekPreviousButton = new JButton();

        try {
            addButtonImage(seekPreviousButton, "seek-Previous.png");

        } catch (IOException e) {
            seekPreviousButton.setText("<<");
        }

        seekPreviousButton.addActionListener(e -> {
            CurrentMusic currentMusic = CurrentMusic.getInstance();
            currentMusic.seekPrevious();
        });

        buttonPanel.add(seekPreviousButton);

    }

    private void addStopButton() {
        stopButton = new JButton();
        try {
            addButtonImage(stopButton, "stop.png");
        } catch (IOException e) {
            stopButton.setText("■");
        }

        stopButton.addActionListener(e -> {
            CurrentMusic currentMusic = CurrentMusic.getInstance();
            currentMusic.stop();
            reset();
        });
        buttonPanel.add(stopButton);


    }

    private void addPlayModeButton() {
        playModeButton = new JButton();
        try {
        	addButtonImage(playModeButton,"loop");
        } catch (Exception e) {
            playModeButton.setText("A/R");
        }

        playModeButton.addActionListener(e -> {
            if (CurrentMusic.playMode == 0) {
                CurrentMusic.playMode++;
                playModeButton.setText("A/N");
                
            } else if (CurrentMusic.playMode == 1) {
                CurrentMusic.playMode++;
                playModeButton.setText("O/R");
                
            } else {
                CurrentMusic.playMode = 0;
                playModeButton.setText("A/R");
                
            }
        });
        buttonPanel.add(playModeButton);
    }

    private void addVolumeSlider() {
        //vertical plz
        volumeSlider = new JSlider();

        // TODO add graphic option
        volumeSlider.addChangeListener(evt -> {
            float volume = (float) (volumeSlider.getValue() - volumeSlider.getMinimum()) / (volumeSlider.getMaximum() - volumeSlider.getMinimum());
            CurrentMusic currentMusic = CurrentMusic.getInstance();
            currentMusic.setVolume(volume);
        });
        volumeSlider.setBackground(Color.BLACK);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setForeground(Color.white);
        this.add(volumeSlider);
    }

    private void addCurrentTimeSlider() {
        // TODO
        // horizontal plz
        currentTimeSlider = new JSlider();
        currentTimeSlider.setEnabled(true);
        CurrentMusic currentMusic = CurrentMusic.getInstance();

        currentTimeSlider.addChangeListener(evt -> {
            float percent = (float) (currentTimeSlider.getValue() - currentTimeSlider.getMinimum()) / (currentTimeSlider.getMaximum() - currentTimeSlider.getMinimum());
            currentMusic.seek(percent);
        });
        currentTimeSlider.setBackground(Color.BLACK);
        currentTimeSlider.setPaintTicks(true);
        currentTimeSlider.setForeground(Color.white);

        currentTimeSlider.setValue(currentTimeSlider.getMinimum());

        currentMusic.addChangeTimeEvent(currentTimeSlider, (JSlider jSlider) -> {
            SwingUtilities.invokeLater(() -> {
                Optional<Duration> currentTimeOptional = currentMusic.getCurrentTime();
                Optional<Duration> totalTimeOptional = currentMusic.getTotalTime();
                if (currentTimeOptional.isPresent() && totalTimeOptional.isPresent()) {
                    double percent = currentTimeOptional.get().toMillis() / totalTimeOptional.get().toMillis();
                    jSlider.setValue((int) (percent * (jSlider.getMaximum() - jSlider.getMinimum())) + jSlider.getMinimum());
                }
            });
        });
        this.add(currentTimeSlider);
    }

    private void addStarButton() {
        //TODO
        starButton = new JButton("★");
        starButton.addActionListener(e -> {
            Music temp = CurrentMusic.getInstance().toMusic();
            if (MusicList.listNum != 1) {
                MusicListManager.getInstance().addToFavoriteMusicList(temp);

            } else {
                MusicListManager.getInstance().deleteToFavoriteMusicList(temp);
                tabPanel.getFavoriteButton().doClick();
            }
        });
        buttonPanel.add(starButton);
    }

    private void addImageLabel() {

        try {
            if (musicImage == null) {
                musicImage = ImageIO.read(new File("./"+ "defaultImage.jpg"));
            }
            musicImage = musicImage.getScaledInstance(200, 200, Image.SCALE_DEFAULT);

            musicImageLabel = new JLabel(new ImageIcon(musicImage));

            musicInfoPanel = new JPanel();
            musicInfoPanel.setLayout(new BorderLayout());
            musicInfoPanel.add(musicImageLabel, BorderLayout.CENTER);

            musicName = new JLabel();
            musicName.setText("Ready");
            musicName.setForeground(Color.WHITE);
            musicName.setBackground(Color.darkGray);
            musicName.setOpaque(true);

            musicInfoPanel.setSize(30, 30);
            musicInfoPanel.add(musicName, BorderLayout.SOUTH);

            this.add(musicInfoPanel);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void replaceMusicInfo() {
        try {
            Music music = CurrentMusic.getInstance().toMusic();
            if (music.getAlbumArt() != null) {
                musicImage = ImageIO.read(new ByteArrayInputStream(music.getAlbumArt()));
                musicImage = musicImage.getScaledInstance(200, 200, Image.SCALE_DEFAULT);
                musicImageLabel.removeAll();
                musicImageLabel.setIcon(new ImageIcon(musicImage));
            }
            musicName.setText(FilePathParser.getFileName(music.getFilename()));
        } catch (Exception e) {
        }
    }

    public void doStop() {
        stopButton.doClick();
    }

    public void doPlay() {
        replaceMusicInfo();
        playButton.doClick();
    }

    public void reset() {
        starButton.setText("★");
        try {
            addButtonImage(playButton, "play.jpg");
        } catch (IOException e) {
            playButton.setText("▶");
        }
    }

    public void connectPanels(Tab tabPanel) {
        this.tabPanel = tabPanel;
    }

    private void addLyric() {
        text = new JLabel();

        text.setOpaque(true);
        text.setBackground(Color.BLACK);
        text.setForeground(Color.WHITE);
        text.setSize(30, 30);
        add(text);

        new Lyric_Repeat();
        //buttonPanel.add(text1);
    }
}