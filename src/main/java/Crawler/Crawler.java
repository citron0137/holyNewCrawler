package Crawler;

import Struct.BoardDTO;
import Struct.PostDTO;

import java.util.Date;
import java.util.List;

public interface Crawler {
    public void login(String id, String pw);
    public void setBoards(List<BoardDTO> boards);
    public PostDTO getPost(BoardDTO board, int postNum);
    public List<PostDTO> getPosts(Date start, Date end);

}
