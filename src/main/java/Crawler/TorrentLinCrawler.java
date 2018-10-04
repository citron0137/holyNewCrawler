package Crawler;

import Struct.BoardDTO;
import Struct.PostDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TorrentLinCrawler implements Crawler{

    List<BoardDTO> boards = new ArrayList<BoardDTO>();

    public void login(String id, String pw) {
        //TODO Login
        return;
    }
    public void setBoards(List<BoardDTO> boards) {
        this.boards.add(new BoardDTO(
                "http://www.tfreeca22.com/board.php?mode=list&b_id=tmovie",
                "body > table > tbody > tr > td:nth-child(2) > table.b_list > tbody > tr:nth-child(3) > td.subject > div > a.stitle1",
                "board.php?mode=view&b_id=tmovie&id=",
                "http://www.tfreeca22.com/board.php?mode=view&b_id=tmovie&id=",
                "body > table > tbody > tr > td:nth-child(2) > table:nth-child(4) > tbody > tr:nth-child(2) > td.view_t2 > div",
                "",
                "body > table > tbody > tr > td:nth-child(2) > table:nth-child(4) > tbody > tr:nth-child(3) > td:nth-child(1)",
                "등록일: yyyy-MM-dd HH:mm:ss",
                "body > div:nth-child(", 3, 3, ")> a",
                "body > table > tbody > tr > td:nth-child(2) > table:nth-child(4) > tbody > tr:nth-child(", 4, 1, ") > td:nth-child(1) > a"
        ));
        this.boards.add(new BoardDTO(
                "https://torrentlin.com/bbs/board.php?bo_table=torrent_movie_old",
                "td.subject a[href]",
                "../bbs/board.php?bo_table=torrent_movie_old&wr_id=",
                "https://torrentlin.com/bbs/board.php?bo_table=torrent_movie_old&wr_id=",
                "body > div > table > tbody > tr:nth-child(6) > td > table > tbody > tr > td:nth-child(1) > div:nth-child(2) > table:nth-child(3) > tbody > tr > td > div:nth-child(2) > table:nth-child(1) > tbody > tr > td:nth-child(1) > h1",
                "",
                "body > div > table > tbody > tr:nth-child(6) > td > table > tbody > tr > td:nth-child(1) > div:nth-child(2) > table:nth-child(3) > tbody > tr > td > div:nth-child(1) > div:nth-child(1) > span",
                "업데이트 : yy-MM-dd HH:mm",
                "body > div > table > tbody > tr:nth-child(6) > td > table > tbody > tr > td:nth-child(1) > div:nth-child(2) > table:nth-child(3) > tbody > tr > td > div:nth-child(2) > table:nth-child(3) > tbody > tr:nth-child(", 2, 3, ") > td > div > div:nth-child(1) > a:nth-child(3)",
                "body > div > table > tbody > tr:nth-child(6) > td > table > tbody > tr > td:nth-child(1) > div:nth-child(2) > table:nth-child(3) > tbody > tr > td > div:nth-child(2) > table:nth-child(3) > tbody > tr:nth-child(", 2, 3, ") > td > div > div:nth-child(1) > a:nth-child(2)"
        ));
    }

    public PostDTO getPost(BoardDTO board, int postNum) {
        return null;
    }

    public List<PostDTO> getPosts(Date start, Date end) {
        return null;
    }
}
