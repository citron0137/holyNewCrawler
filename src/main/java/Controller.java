import Crawler.Crawler;
import Crawler.TorrentLinCrawl;
import Struct.BoardDTO;
import Struct.PostDTO;
import Struct.SiteDTO;
import bttSQL.DBHandler;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Controller {
    public static void main(String[] args){

        DBHandler bttDb = new DBHandler();
        if(!bttDb.setCon()){
            System.out.println("DB connection fail\ncheck DBHandler.java");
            return;
        }

        Crawler torrentLinCrawler = new TorrentLinCrawl();
        SiteDTO torrentLinSite = bttDb.getSiteInfo("https://torrentlin.com");
        List<BoardDTO> tmpBoards = new ArrayList<>();
        tmpBoards.add(bttDb.getBoardInfo("https://torrentlin.com/bbs/board.php?bo_table=torrent_movie_new"));
        tmpBoards.add(bttDb.getBoardInfo("https://torrentlin.com/bbs/board.php?bo_table=torrent_movie_old"));
        tmpBoards.add(bttDb.getBoardInfo("https://torrentlin.com/bbs/board.php?bo_table=torrent_movie_3d"));
        torrentLinCrawler.setBoards(tmpBoards);
          List<PostDTO> posts = ((TorrentLinCrawl) torrentLinCrawler).testMethod();

       // torrentLinCrawler.saveTorrentFile("https://torrentlin.com/bbs/board.php?bo_table=torrent_movie_new&wr_id=3469");
        /*
        List<PostDTO> posts = new ArrayList<>();
        for (BoardDTO itr : tmpBoards){
            //Post 데이터베이스 리스트 생성
            int lastUpdate_index = torrentLinCrawler.doCrawl(itr);  //////using torrentLinCrawler
            for (int i = 1 ; i< 4; i++){
                PostDTO tmpPost = torrentLinCrawler.getPost(i); /////using torrentLinCrawler
                if( tmpPost != null) posts.add(tmpPost);
            }
        }
        */
        bttDb.insertPosts(posts);
        bttDb.close();
    }
}
