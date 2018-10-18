
import Crawler.BeeTorrentCrawl;
import Crawler.DaktoCrawl;
import Crawler.TorrentBozaCrawl;
import Crawler.TorrentLinCrawl;
import Struct.BoardDTO;
import Struct.PostDTO;
import Struct.SiteDTO;
import Struct.TorrentFileDTO;
import bttSQL.DBHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

        TorrentLinCrawl torrentLinCrawler = new TorrentLinCrawl();
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
       // bttDb.insertPosts(posts);
        bttDb.close();
    }

    public void getTorrentLinPosts(String boardUrl, LocalDateTime start, LocalDateTime end) {
        PostDTO tmpPost;
        DBHandler bttDb = new DBHandler();
        if (!bttDb.setCon()) {
            System.out.println("DB connection fail\ncheck DBHandler.java");
            return;
        }
        TorrentLinCrawl torrentLinCrawler = new TorrentLinCrawl();
        try {
            tmpPost = torrentLinCrawler.getLastPost(bttDb.getBoardInfo(boardUrl));
            if (tmpPost.getPost_date().isBefore(end)) bttDb.insertPost(tmpPost);
        } catch (Exception e) {
            System.out.println("failed to get last Post");
            File errorLog = new File("error.txt");
            try {
                FileWriter fw = new FileWriter(errorLog, true);
                fw.write(boardUrl+"\n->"+e+"\n\n");
                fw.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return;
        }
        while (true) {
            try {
                tmpPost = torrentLinCrawler.getPrevPost();
                if (end.isBefore(tmpPost.getPost_date())) continue;
                if (tmpPost.getPost_date().isBefore(start)) break;
                bttDb.insertPost(tmpPost);
            } catch (Exception e) {
                //TODO file로 저장
                System.out.println("failed to get n insert Post from " + tmpPost.getPost_link());
                System.out.println("***********************************************\n"+e+"\n**************************************************");
                File errorLog = new File("error.txt");
                try {
                    FileWriter fw = new FileWriter(errorLog, true);
                    fw.write(tmpPost.getPost_link() + " \n-> "+e+"\n\n");
                    fw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            System.out.println();
        }
    }
    public void getTorrentBozzaPosts(String boardUrl, LocalDateTime start, LocalDateTime end) {
        PostDTO tmpPost;
        DBHandler bttDb = new DBHandler();
        if (!bttDb.setCon()) {
            System.out.println("DB connection fail\ncheck DBHandler.java");
            return;
        }
        TorrentBozaCrawl torrentBozaCrawl = new TorrentBozaCrawl();
        try {
            tmpPost = torrentBozaCrawl.getLastPost(bttDb.getBoardInfo(boardUrl));
            if (tmpPost.getPost_date().isBefore(end)) bttDb.insertPost(tmpPost);
        } catch (Exception e) {
            System.out.println("failed to get last Post");
            File errorLog = new File("error.txt");
            try {
                FileWriter fw = new FileWriter(errorLog, true);
                fw.write(boardUrl+"\n->"+e+"\n\n");
                fw.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return;
        }
        while (true) {
            try {
                tmpPost = torrentBozaCrawl.getPrevPost();
                if (end.isBefore(tmpPost.getPost_date())) continue;
                if (tmpPost.getPost_date().isBefore(start)) break;
                bttDb.insertPost(tmpPost);
            } catch (Exception e) {
                //TODO file로 저장
                System.out.println("failed to get n insert Post from " + tmpPost.getPost_link());
                System.out.println("***********************************************\n"+e+"\n**************************************************");
                File errorLog = new File("error.txt");
                try {
                    FileWriter fw = new FileWriter(errorLog, true);
                    fw.write(tmpPost.getPost_link() + " \n-> "+e+"\n\n");
                    fw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            System.out.println();
        }
    }
    public void getBeeTorrentPosts(String boardUrl, LocalDateTime start, LocalDateTime end) {
        PostDTO tmpPost;
        DBHandler bttDb = new DBHandler();
        if (!bttDb.setCon()) {
            System.out.println("DB connection fail\ncheck DBHandler.java");
            return;
        }
        BeeTorrentCrawl beeTorrentCrawl = new BeeTorrentCrawl();
        try {
            tmpPost = beeTorrentCrawl.getLastPost(bttDb.getBoardInfo(boardUrl));
            if (tmpPost.getPost_date().isBefore(end)) bttDb.insertPost(tmpPost);
        } catch (Exception e) {
            System.out.println("failed to get last Post");
            File errorLog = new File("error.txt");
            try {
                FileWriter fw = new FileWriter(errorLog, true);
                fw.write(boardUrl+"\n->"+e+"\n\n");
                fw.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return;
        }
        while (true) {
            try {
                tmpPost = beeTorrentCrawl.getPrevPost();
                if (end.isBefore(tmpPost.getPost_date())) continue;
                if (tmpPost.getPost_date().isBefore(start)) break;
                bttDb.insertPost(tmpPost);
            } catch (Exception e) {
                //TODO file로 저장
                System.out.println("failed to get n insert Post from " + tmpPost.getPost_link());
                System.out.println("***********************************************\n"+e+"\n**************************************************");
                File errorLog = new File("error.txt");
                try {
                    FileWriter fw = new FileWriter(errorLog, true);
                    fw.write(tmpPost.getPost_link() + " \n-> "+e+"\n\n");
                    fw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            System.out.println();
        }
    }
    public void getDaktoPosts(String boardUrl, LocalDateTime start, LocalDateTime end) {
        PostDTO tmpPost;
        DBHandler bttDb = new DBHandler();
        if (!bttDb.setCon()) {
            System.out.println("DB connection fail\ncheck DBHandler.java");
            return;
        }
        DaktoCrawl daktoCrawl = new DaktoCrawl();
        try {
            tmpPost = daktoCrawl.getLastPost(bttDb.getBoardInfo(boardUrl));
            if (tmpPost.getPost_date().isBefore(end)) bttDb.insertPost(tmpPost);
        } catch (Exception e) {
            System.out.println("failed to get last Post");
            File errorLog = new File("error.txt");
            try {
                FileWriter fw = new FileWriter(errorLog, true);
                fw.write(boardUrl+"\n->"+e+"\n\n");
                fw.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return;
        }
        while (true) {
            try {
                tmpPost = daktoCrawl.getPrevPost();
                if (end.isBefore(tmpPost.getPost_date())) continue;
                if (tmpPost.getPost_date().isBefore(start)) break;
                bttDb.insertPost(tmpPost);
            } catch (Exception e) {
                //TODO file로 저장
                System.out.println("failed to get n insert Post from " + tmpPost.getPost_link());
                System.out.println("***********************************************\n"+e+"\n**************************************************");
                File errorLog = new File("error.txt");
                try {
                    FileWriter fw = new FileWriter(errorLog, true);
                    fw.write(tmpPost.getPost_link() + " \n-> "+e+"\n\n");
                    fw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            System.out.println();
        }
    }
}
