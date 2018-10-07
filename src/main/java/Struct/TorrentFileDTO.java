package Struct;

public class TorrentFileDTO {
    private int post_seq;
    private String magnet;
    private String infohash_dir;
    private int file_size;

    public TorrentFileDTO(){}
    public TorrentFileDTO(
            String magnet,
            String infohash_dir
    ){
        this. magnet = magnet;
        this.infohash_dir = infohash_dir;
    }
    public TorrentFileDTO(
            int post_seq,
            String magnet,
            String infohash_dir
    ){
        this.post_seq = post_seq;
        this. magnet = magnet;
        this.infohash_dir = infohash_dir;
    }
    public void setPost_seq(int post_seq) {
        this.post_seq = post_seq;
    }

    public String getMagnet() {
        return magnet;
    }

    public int getFile_size() {
        return file_size;
    }

    public int getPost_seq() {
        return post_seq;
    }

    public String getInfohash_dir() {
        return infohash_dir;
    }

    public void setInfohash_dir(String infohash_dir) {
        this.infohash_dir = infohash_dir;
    }

    public void setMagnet(String magnet) {
        this.magnet = magnet;
    }
}
