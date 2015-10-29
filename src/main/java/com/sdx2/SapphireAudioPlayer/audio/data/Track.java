package main.java.com.sdx2.SapphireAudioPlayer.audio.data;

import main.java.com.sdx2.SapphireAudioPlayer.audio.util.AudioUtil;
import org.jaudiotagger.tag.FieldKey;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class Track {
    private int fileSize;
    private int Tracklength;
    private String fileName;
    private String directory;
    private int sampleRate;
    private int channels;
    private int bps;
    private int bitrate;
    private int subsongIndex;
    private long startPosition;
    private long totalSamples;
    private String locationString;
    private String codec;
    private String encoder;

    private Map<FieldKey, String> tagFields = new HashMap<FieldKey, String>(5, 1f);

    public int getfileSize() {
        return fileSize;
    }

    public void setfileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getTrackLength() {
        return Tracklength;
    }

    public void setTrackLength(int tracklength) {
        this.Tracklength = tracklength;
    }

    public String getFileName() {
        if (fileName == null) {
            fileName = AudioUtil.removeExt(getFile().getName());
        }
        return fileName;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public int getChannels() {
        return channels;
    }

    public String getChannelsAsString() {
        switch (getChannels()) {
            case 1:
                return "Mono";
            case 2:
                return "Stereo";
            default:
                return getChannels() + " ch";
        }
    }

    public void setChannels(int channels) {
        this.channels = channels;
    }

    public int getBps() {
        return bps;
    }

    public void setBps(int bps) {
        this.bps = bps;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public int getSubsongIndex() {
        return subsongIndex;
    }

    public void setSubsongIndex(int subsongIndex) {
        this.subsongIndex = subsongIndex;
    }

    public long getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(long startPosition) {
        this.startPosition = startPosition;
    }

    public long getTotalSamples() {
        return totalSamples;
    }

    public void setTotalSamples(long totalSamples) {
        this.totalSamples = totalSamples;
    }

    public URI getLocation() {
        if (locationString != null) {
            try {
                return new URI(locationString);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void setLocation(String location) {
        locationString = location;
    }

    public File getFile() {
        return new File(getLocation());
    }

    public boolean isFile() {
        return getLocation() != null && !isStream();
    }

    public boolean isStream() {
        return getLocation() != null && "http".equals(getLocation().getScheme());
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec.intern();
    }

    public String getEncoder() {
        return encoder;
    }

    public void setEncoder(String encoder) {
        this.encoder= encoder.intern();
    }

    public String getDirectory() {
        if (directory == null) {
            directory = getFile().getParentFile().getName();
        }
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }


    public String getArtist() {
        return tagFields.get(FieldKey.ARTIST);
    }

    public void addArtist(String value) {
        tagFields.put(FieldKey.ARTIST, value);
    }

    public String getAlbum() {
        return tagFields.get(FieldKey.ALBUM);
    }

    public void addAlbum(String value) {
        tagFields.put(FieldKey.ALBUM, value);
    }

    public String getTitle() {
        return tagFields.get(FieldKey.TITLE);
    }

    public void addTitle(String value) {
        tagFields.put(FieldKey.TITLE, value);
    }

    public String getAlbumArtist() {
        return tagFields.get(FieldKey.ALBUM_ARTIST);
    }

    public void addAlbumArtist(String value) {
        tagFields.put(FieldKey.ALBUM_ARTIST, value);
    }

    public String getYear() {
        return tagFields.get(FieldKey.YEAR);
    }

    public void addYear(String value) {
        tagFields.put(FieldKey.YEAR, value);
    }

    public String getGenre() {
        return tagFields.get(FieldKey.GENRE);
    }

    public String getGenres() {
        return tagFields.get(FieldKey.GENRE);
    }

    public void addGenre(String value) {
        tagFields.put(FieldKey.GENRE, value);
    }

    public String getComment() {
        return tagFields.get(FieldKey.COMMENT);
    }

    public void addComment(String value) {
        tagFields.put(FieldKey.COMMENT, value);
    }

    public String getTrack() {
        return tagFields.get(FieldKey.TRACK);
    }

    public void addTrack(String value) {
        tagFields.put(FieldKey.TRACK, value);
    }

    public void addTrack(Integer value) {
        if (value != null) {
            addTrack(value.toString());
        }
    }

    public void setTrack(String value) {
        tagFields.put(FieldKey.TRACK, value);
    }

    public void setTrack(Integer value) {
        if (value != null) {
            setTrack(value.toString());
        }
    }

    public String getTrackTotal() {
        return tagFields.get(FieldKey.TRACK_TOTAL);
    }

    public void addTrackTotal(String value) {
        tagFields.put(FieldKey.TRACK_TOTAL, value);
    }

    public void addTrackTotal(Integer value) {
        if (value != null) {
            addTrackTotal(value.toString());
        }
    }

    public void setTrackTotal(String value) {
        tagFields.put(FieldKey.TRACK_TOTAL, value);
    }

    public void setTrackTotal(Integer value) {
        if (value != null) {
            setTrackTotal(value.toString());
        }
    }

    public String getDisc() {
        return tagFields.get(FieldKey.DISC_NO);
    }

    public void addDisc(String value) {
        tagFields.put(FieldKey.DISC_NO, value);
    }

    public void addDisc(Integer value) {
        if (value != null) {
            addDisc(value.toString());
        }
    }

    public void setDisc(String value) {
        tagFields.put(FieldKey.DISC_NO, value);
    }

    public void setDisc(Integer value) {
        if (value != null) {
            setDisc(value.toString());
        }
    }

    public String getDiscTotal() {
        return tagFields.get(FieldKey.DISC_TOTAL);
    }

    public void addDiscTotal(String value) {
        tagFields.put(FieldKey.DISC_TOTAL, value);
    }

    public void addDiscTotal(Integer value) {
        if (value != null) {
            addDiscTotal(value.toString());
        }
    }

    public void setDiscTotal(String value) {
        tagFields.put(FieldKey.DISC_TOTAL, value);
    }

    public void setDiscTotal(Integer value) {
        if (value != null) {
            setDiscTotal(value.toString());
        }
    }

    public String getRecordLabel() {
        return tagFields.get(FieldKey.RECORD_LABEL);
    }

    public String getRecordLabels() {
        return tagFields.get(FieldKey.RECORD_LABEL);
    }

    public void addRecordLabel(String value) {
        tagFields.put(FieldKey.RECORD_LABEL, value);
    }

    public String getCatalogNo() {
        return tagFields.get(FieldKey.CATALOG_NO);
    }

    public String getCatalogNos() {
        return tagFields.get(FieldKey.CATALOG_NO);
    }

    public void addCatalogNo(String value) {
        tagFields.put(FieldKey.CATALOG_NO, value);
    }

    public String getRating() {
        return tagFields.get(FieldKey.RATING);
    }

    public void addRating(String value) {
        tagFields.put(FieldKey.RATING, value);
    }

    public void addTagFieldValue(FieldKey key, String value) {
            tagFields.put(key, value);
    }

}
