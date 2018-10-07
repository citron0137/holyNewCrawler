package Crawler;

import Struct.*;
import Struct.BoardDTO;
import Struct.PostDTO;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface Crawler {
    public void login(String id, String pw);
    public void setBoards(List<BoardDTO> boards);
    public PostDTO getPost(BoardDTO board, int postNum) throws Exception;
    public List<PostDTO> getPosts(BoardDTO board, LocalDateTime start, LocalDateTime end);

    //TODO 태규 추가
    public List<PostDTO> getBoardsPosts(List<BoardDTO> boards, LocalDateTime start, LocalDateTime end);

    public int crawlTorrentSite();
    public List<TorrentFileDTO> saveTorrentFile(PostDTO post);

}
