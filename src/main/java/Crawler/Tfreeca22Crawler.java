package Crawler;

import Struct.BoardDTO;
import Struct.PostDTO;

import java.util.Date;
import java.util.List;

public class Tfreeca22Crawler implements Crawler {

    List<BoardDTO> Boards;

    public void login(String id, String pw) {

    }

    public void setBoards(List<BoardDTO> boards) {

    }

    public PostDTO getPost(BoardDTO board, int postNum) {
        return null;
    }

    public List<PostDTO> getPosts(Date start, Date end) {
        return null;
    }
}
