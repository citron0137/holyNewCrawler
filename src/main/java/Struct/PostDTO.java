package Struct;

import bttSQL.DBHandler;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class PostDTO {
    private int seq;
    private int board_seq;
    private String post_link;
    private String post_name;
    private String user_id;
    private LocalDateTime post_date;
    List<TorrentFileDTO> torrents;
    List<PostImageDTO> images;

    public PostDTO(){}
    public PostDTO(
        int board_seq,
        String post_link,
        String post_name,
        String user_id,
        LocalDateTime post_date,
        List<TorrentFileDTO> torrents,
        List<PostImageDTO> images
    ){
        this.board_seq = board_seq;
        this.post_name = post_name;
        this.post_link = post_link;
        this.user_id = user_id;
        this.post_date = post_date;
        this.torrents = torrents;
        this.images = images;
    }

    public LocalDateTime getPost_date() {
        return post_date;
    }

    public int getBoard_seq() {
        return board_seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getSeq() {
        return seq;
    }

    public List<PostImageDTO> getImages() {
        return images;
    }

    public List<TorrentFileDTO> getTorrents() {
        return torrents;
    }

    public String getPost_link() {
        return post_link;
    }

    public String getPost_name() {
        return post_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setPost_link(String post_link) {
        this.post_link = post_link;
    }

    public void setBoard_seq(int board_seq) {
        this.board_seq = board_seq;
    }

    public void setPost_date(LocalDateTime post_date) {
        this.post_date = post_date;
    }

    public void setPost_name(String post_name) {
        this.post_name = post_name;
    }

    public void setTorrents(List<TorrentFileDTO> torrents) {
        this.torrents = torrents;
    }

    public void setImages(List<PostImageDTO> images) {
        this.images = images;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
