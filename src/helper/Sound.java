package helper;

import java.io.File;

import javax.sound.sampled.*;

import debug.debug;

public class Sound {
	private File sound = null;
	public Sound(String loc){
		//Save sound
		File file = new File("sounds/"+loc);
		sound=file;
	}

	public void play(){
		new Thread(
				new Runnable() {
					public void run() {
						try {
							try {
								//get an AudioInputStream
								AudioInputStream ais = AudioSystem.getAudioInputStream(sound);
								//get the AudioFormat for the AudioInputStream
								AudioFormat audioformat = ais.getFormat();
								debug.notify("Format: " + audioformat.toString());
								//ULAW format to PCM format conversion
								if ((audioformat.getEncoding() == AudioFormat.Encoding.ULAW)
										|| (audioformat.getEncoding() == AudioFormat.Encoding.ALAW)) {
									AudioFormat newformat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
											audioformat.getSampleRate(),
											audioformat.getSampleSizeInBits() * 2,
											audioformat.getChannels(),
											audioformat.getFrameSize() * 2,
											audioformat.getFrameRate(), true);
									ais = AudioSystem.getAudioInputStream(newformat, ais);
									audioformat = newformat;
								}

								//checking for a supported output line
								DataLine.Info datalineinfo = new DataLine.Info(SourceDataLine.class, audioformat);
								if (!AudioSystem.isLineSupported(datalineinfo)) {
									//System.out.println("Line matching " + datalineinfo + " is not supported.");
								} else {
									//System.out.println("Line matching " + datalineinfo + " is supported.");
									//opening the sound output line
									SourceDataLine sourcedataline = (SourceDataLine) AudioSystem.getLine(datalineinfo);
									sourcedataline.open(audioformat);
									sourcedataline.start();
									//Copy data from the input stream to the output data line
									int framesizeinbytes = audioformat.getFrameSize();
									int bufferlengthinframes = sourcedataline.getBufferSize() / 8;
									int bufferlengthinbytes = bufferlengthinframes * framesizeinbytes;
									byte[] sounddata = new byte[bufferlengthinbytes];
									int numberofbytesread = 0;
									while ((numberofbytesread = ais.read(sounddata)) != -1) {
										int numberofbytesremaining = numberofbytesread;
										sourcedataline.write(sounddata, 0, numberofbytesread);
									}
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
	}
}
