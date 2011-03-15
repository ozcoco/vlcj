/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright 2009, 2010, 2011 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.capture;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * An example of transcoding and saving video from a capture device.
 * <p>
 * Capture devices often, as in the case with Video4Linux, produce raw frames
 * of video data. These raw frames must be transcoded before the video can be
 * saved or streamed.
 * <p>
 * This class creates a full-screen media player that captures, transcodes and
 * saves video from a video capture device.
 * <p>
 * Specify the MRL of the capture device as a command-line argument...
 * <p>
 * Linux:
 * <pre>
 *   v4l2:///dev/video0
 * </pre>
 * Windows:
 * <pre>
 *   dshow://
 * </pre>
 * Each time this application is executed a new time-stamped video capture file
 * will be created in a directory called "Videos" under the user home directory.
 */
public class CaptureTest extends VlcjTest {

  private JFrame frame;
  private JPanel contentPane;
  private Canvas canvas;
  private MediaPlayerFactory factory;
  private EmbeddedMediaPlayer mediaPlayer;
  private CanvasVideoSurface videoSurface;
  
  public static void main(final String[] args) {
    if(args.length != 1) {
      System.out.println("Specify a capture device MRL, for example \"v4l2:///dev/video0\" or \"dshow://\"");
      System.exit(1);
    }
    
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new CaptureTest().start(args[0]);
      }
    });
  }

  public CaptureTest() {
    canvas = new Canvas();
    canvas.setBackground(Color.black);
    
    contentPane = new JPanel();
    contentPane.setBackground(Color.black);
    contentPane.setLayout(new BorderLayout());
    contentPane.add(canvas, BorderLayout.CENTER);
    
    frame = new JFrame("Capture");
    frame.setContentPane(contentPane);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(800, 600);
    frame.pack();
    
    factory = new MediaPlayerFactory(new String[] {"--no-video-title-show"});
    mediaPlayer = factory.newEmbeddedMediaPlayer();
    
    videoSurface = factory.newVideoSurface(canvas);
    
    mediaPlayer.setVideoSurface(videoSurface);
  }
  
  private void start(String mrl) {
    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(frame);
    
    File dir = new File(System.getProperty("user.home"), "Videos");
    dir.mkdirs();
    
    DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
    String fileName =  dir.getAbsolutePath() + "/Capture-" + df.format(new Date()) + ".mpg";

    // Tweak the options depending on your encoding requirements and audio 
    // capture device
    String[] options = {
      ":sout=#transcode{vcodec=mp4v,vb=4096,scale=1,acodec=mpga,ab=128,channels=2,samplerate=44100}:duplicate{dst=file{dst=" + fileName + "},dst=display}",
      ":input-slave=asla://hw:0,0"
    };
    
    mediaPlayer.playMedia(mrl, options);
  }
}