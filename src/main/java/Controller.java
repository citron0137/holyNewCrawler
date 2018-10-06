import Crawler.Crawler;
import Crawler.TorrentLinCrawler;
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
        /*
        DBHandler bttDb = new DBHandler();

        bttDb.setCon();

        SiteDTO torrentLinSite = bttDb.getSiteInfo();
            //if I throw site link -> set all data about site

        //torrentLineCrawler.getPost();


       bttDb.getBoardInfo();
        //torrentLinCrawler.setBoards(bttDb.getBoardInfo());
            //if I throw board link -> set all data about board

        //torrentLinCrawler.login(null,null);
            //Login
        List<PostDTO> torrentLinPosts = torrentLinCrawler.getPosts(new Date(),new Date());
            //get Posts
        bttDb.insertPosts(torrentLinPosts);
            //inset Posts at Database
        bttDb.close();

        *
        */
        DBHandler bttDb = new DBHandler();
        if(!bttDb.setCon()) return;
        //bttDb.getSiteInfo("http://www.tfreeca22.com");
        //bttDb.getBoardInfo("http://www.tfreeca22.com/board.php?mode=list&b_id=tmovie");


        Crawler torrentLinCrawler = new TorrentLinCrawler();
        SiteDTO torrentLinSite = bttDb.getSiteInfo("https://torrentlin.com");
        List<BoardDTO> tmpBoards = new ArrayList<>();
        tmpBoards.add(bttDb.getBoardInfo("https://torrentlin.com/bbs/board.php?bo_table=torrent_movie_new"));
        tmpBoards.add(bttDb.getBoardInfo("https://torrentlin.com/bbs/board.php?bo_table=torrent_movie_old"));
        tmpBoards.add(bttDb.getBoardInfo("https://torrentlin.com/bbs/board.php?bo_table=torrent_movie_3d"));
        torrentLinCrawler.setPost(tmpBoards);
        List<PostDTO> posts = new ArrayList<>();
        for (BoardDTO itr : tmpBoards){
            //Post 데이터베이스 리스트 생성
            int lastUpdate_index = torrentLinCrawler.doCrawl(itr);
            for (int i = 1 ; i< 4; i++){
                PostDTO tmpPost = torrentLinCrawler.getPost(i);
                if( tmpPost != null) posts.add(tmpPost);
            }
        }

        bttDb.insertPosts(posts);
//        System.exit(1);
        //tmp Posts
        /*
        List<PostDTO> Posts = new ArrayList<PostDTO>();
        Posts.add(new PostDTO(
                1,
                "postlilnk1_1111",
                "this is sparta",
                "citron0137",
                LocalDateTime.of(2018, Month.OCTOBER, 1, 12, 30),
                null,
                null
                ));
        Posts.add(new PostDTO(
                1,
                "postlilnk2_222222",
                "venom",
                "citron0137",
                LocalDateTime.of(2018, Month.OCTOBER, 1, 12, 30),
                null,
                null
        ));
        Posts.add(new PostDTO(
                1,
                "postlilnk13333333",
                "Ironman",
                "citron0137",
                LocalDateTime.of(2018, Month.OCTOBER, 1, 12, 30),
                null,
                null
        ));
        */
        //bttDb.insertPosts(Posts);
        //insert post info
        bttDb.close();
    }
}
