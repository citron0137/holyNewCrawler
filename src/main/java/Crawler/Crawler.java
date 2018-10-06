package Crawler;

import Struct.*;
import Struct.BoardDTO;
import Struct.PostDTO;

import java.util.Date;
import java.util.List;

public interface Crawler {

    public void login(String id, String pw);
    public void setBoards(List<BoardDTO> boards);
    public PostDTO getPost(BoardDTO board, int postNum);
    public List<PostDTO> getPosts(Date start, Date end);

    //TODO 태규 추가
    public void setPost(List<BoardDTO> boards);

    public int doCrawl(BoardDTO board);
    public int crawlTorrentSite();
    public PostDTO getPost(int num);
    public List<TorrentFileDTO> saveTorrentFile(String url);

}
