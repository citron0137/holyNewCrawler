
package Crawler;

import Struct.BoardDTO;
import Struct.PostDTO;
import Struct.PostImageDTO;
import Struct.TorrentFileDTO;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TorrentLinCrawl {

    private String SITE_URL = "https://torrentlin.com";
    private List<BoardDTO> boards = new ArrayList<>();
    private List<Selector> selectors = new ArrayList<>();
    private BoardDTO board;
    private int thisPostNum;



    public TorrentLinCrawl(){
        selectors.add(new Selector(
                        "https://torrentlin.com/bbs/board.php?bo_table=torrent_movie_new",
                        "body > div > table > tbody > tr:nth-child(6) > td > table > tbody > tr > td:nth-child(1) > div:nth-child(2) > table:nth-child(4) > tbody > tr > td > form > table > tbody > tr:nth-child(2) > td.subject > nobr > a",
                        "../bbs/board.php?bo_table=torrent_movie_new&wr_id=",
                        "https://torrentlin.com/bbs/board.php?bo_table=torrent_movie_new&wr_id=",
                        "body > div > table > tbody > tr:nth-child(6) > td > table > tbody > tr > td:nth-child(1) > div:nth-child(2) > table:nth-child(3) > tbody > tr > td > div:nth-child(2) > table:nth-child(1) > tbody > tr > td:nth-child(1) > h1",
                        "",
                        "body > div > table > tbody > tr:nth-child(6) > td > table > tbody > tr > td:nth-child(1) > div:nth-child(2) > table:nth-child(3) > tbody > tr > td > div:nth-child(1) > div:nth-child(1) > span",
                        "업데이트 : yy-MM-dd HH:mm"
                )
        );
        selectors.add(new Selector(
                        "https://torrentlin.com/bbs/board.php?bo_table=torrent_movie_old",
                        "td.subject a[href]",
                        "../bbs/board.php?bo_table=torrent_movie_old&wr_id=",
                        "https://torrentlin.com/bbs/board.php?bo_table=torrent_movie_old&wr_id=",
                        "body > div > table > tbody > tr:nth-child(6) > td > table > tbody > tr > td:nth-child(1) > div:nth-child(2) > table:nth-child(3) > tbody > tr > td > div:nth-child(2) > table:nth-child(1) > tbody > tr > td:nth-child(1) > h1",
                        "",
                        "body > div > table > tbody > tr:nth-child(6) > td > table > tbody > tr > td:nth-child(1) > div:nth-child(2) > table:nth-child(3) > tbody > tr > td > div:nth-child(1) > div:nth-child(1) > span",
                        "업데이트 : yy-MM-dd HH:mm"
                )
        );
    }


    public void login(String id, String pw) {
        //TODO
    }

    public boolean setBoard(BoardDTO board){
        for(Selector itrSelector: this.selectors){
            if(board.getBoardUrl().equals(itrSelector.BOARD_URL)){
                board.setSelector(itrSelector);
                continue;
            }
        }
        if(board.getSelector() == null) return false;
        try {
            Thread.sleep(1000);
            Document rawBoard =  Jsoup.connect(board.getBoardUrl())
                    .header("Origin", this.SITE_URL)
                    .header("Referer", this.SITE_URL)
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Content-Type", "text/html;charset=iso-8859-1")
                    //.header("Accept-Encoding", "gzip, deflate, br")
                    .method(Connection.Method.GET)
                    .ignoreContentType(true)
                    .get();
            String LastUpdated = "-";
            String LastUpdatedList = rawBoard.select(board.getSelector().LAST_UPDATED_SELECTOR).attr("href");
            if(LastUpdatedList != null&& LastUpdatedList.length() != 0)
            {
                LastUpdated = rawBoard.select(board.getSelector().LAST_UPDATED_SELECTOR).attr("href").substring(board.getSelector().BOARD_BASE_URL.length());
            }
            String LastIndex = LastUpdated.replaceAll("[^0-9]", " ");
            board.getSelector().LAST_UPDATED_IDX = Integer.parseInt(LastIndex.trim().split(" ")[0]);
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public void setBoards(List<BoardDTO> boards) {
        this.boards= boards;
        for(BoardDTO itrBoard: this.boards){
            if(!setBoard(itrBoard)){
                System.out.println("no selector for "+ itrBoard.getBoardUrl());
            }
        }


    }

    public String selectFromDoc(Document doc, String selector){
        String result;
        try {
            result = doc.select(selector).get(0).text();
        }catch (IllegalArgumentException e){
            result = "-";
        } catch (IndexOutOfBoundsException e){
            result = "-";
        }
        return result;
    }

    public PostDTO getPost(BoardDTO board, int postNum) throws Exception {
        Document rawPost = Jsoup.connect(board.getSelector().POST_BASE_URL+postNum)
                .header("Origin", this.SITE_URL)
                .header("Referer", board.getBoardUrl())
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Content-Type", "text/html;charset=iso-8859-1")
                .method(Connection.Method.GET)
                .ignoreContentType(true)
                .get();
        String title = selectFromDoc(rawPost,board.getSelector().TITLE_SELECTOR).replace("'","");
        String userName = selectFromDoc(rawPost,board.getSelector().USER_NAME_SELECTOR);
        String dateString=selectFromDoc(rawPost, board.getSelector().CREATED_DATE_SELECTOR);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(board.getSelector().DATE_TIME_FORMAT);
        LocalDateTime time = LocalDateTime.parse(dateString, formatter);
        PostDTO rstPost = new PostDTO(
                board.getSeq(),
                board.getSelector().POST_BASE_URL+postNum,
                title,userName,time,null,null
        );
        saveTorrentFile(rstPost);

        List<PostImageDTO> postImageDTOS = savePostImage(rstPost);
        rstPost.setImages(postImageDTOS);

        return rstPost;
    }

    public List<PostDTO> getPosts(BoardDTO board, LocalDateTime start, LocalDateTime end) {
        if(board.getSelector() == null) return null;
        int num = board.getSelector().LAST_UPDATED_IDX;
        List<PostDTO> rstPosts = new ArrayList<>();
        while(true){
            try {
                PostDTO post = getPost(board,num);
                if(post.getPost_date().isBefore(start)) break;
                else if(post.getPost_date().isBefore(end)){
                    rstPosts.add(post);
                }
                else{
                    //TODO Remove 잔여 파일
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            num--;
        }
        return rstPosts;
    }


    public List<PostDTO> getBoardsPosts(List<BoardDTO> boards, LocalDateTime start, LocalDateTime end) {
        List<PostDTO> result = new ArrayList<>();
        for (BoardDTO itrBoard:boards) {
            List<PostDTO> tmpPosts = getPosts(itrBoard,start,end);
            if(tmpPosts == null) continue;
            result.addAll(tmpPosts);
        }
        return result;
    }

    public List<TorrentFileDTO> saveTorrentFile(PostDTO post) {
        List<TorrentFileDTO> torrentFiles = new ArrayList<>();
        try {
            Connection.Response loginPageResponse = Jsoup.connect(post.getPost_link())
                    .timeout(3000)
                    .header("Origin", "http://torrentlin.com")
                    .header("Referer", "http://torrentlin.com")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .method(Connection.Method.GET)
                    .execute();
            Map<String, String> cookie = loginPageResponse.cookies();
            Document tmpDocument = loginPageResponse.parse();
            Elements elems2 = tmpDocument.select("body > div > table > tbody > tr:nth-child(6) > td > table > tbody > tr > td:nth-child(1) > div:nth-child(2) > table:nth-child(3) > tbody > tr > td > div:nth-child(2) > table:nth-child(3) > tbody > tr > td > div > div a[href]");
            TorrentFileDTO tmpTorrent = new TorrentFileDTO();
            tmpTorrent.setPost_seq(post.getSeq());
            for (Element elem2 : elems2) {
                if(elem2.attr("href").contains("torrent") && ( !elem2.attr("href").contains("srt") && !elem2.attr("href").contains("smi"))) {
                    //System.out.println(elem2.attr("href"));
                    String downLoadScript = elem2.attr("href");
                    String torrentFileName = elem2.text();
                    String[] downloadInfo = downLoadScript.split(",");
                    Thread.sleep(1000);
                    downloadInfo[0]= downloadInfo[0].replace("javascript:file_download('","https://torrentlin.com/bbs/").replace("'","");
                    torrentFileName= torrentFileName.replace("'","").replace(");","")
                            .replaceAll("/:*?\"<>|","")
                            .replace("\\","")
                            .replace("/","")
                            .replace(":","")
                            .replace("*","")
                            .replace("?","")
                            .replace("\"","")
                            .replace("\'","")
                            .replace("<","")
                            .replace(">","")
                            .replace("|","");

                    System.out.println("link : "+ downloadInfo[0]);
                    System.out.println("file_ tilte : "+ torrentFileName);
                    //System.out.println(type);
                    Connection.Response response = Jsoup.connect(downloadInfo[0])
                            .header("Origin", "http://torrentlin.com")
                            .header("Referer", post.getPost_link())
                            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                            .header("Content-Type", "text/html;charset=iso-8859-1")
                            .header("Accept-Encoding", "gzip, deflate, br")
//                                    .header("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
                            .method(Connection.Method.GET)
                            .cookies(cookie)
                            .ignoreContentType(true)
                            .execute();
                    FileOutputStream out = new FileOutputStream("tmp/"+new java.io.File(torrentFileName));
                    out.write(response.bodyAsBytes());
                    out.close();
                    tmpTorrent.setInfohash_dir(torrentFileName);
                }

                else if(elem2.attr("href").contains("magnet")){
                    System.out.println(elem2.attr("href"));
                    tmpTorrent.setMagnet(elem2.attr("href"));
                    torrentFiles.add(tmpTorrent);
                    tmpTorrent = new TorrentFileDTO();
                    tmpTorrent.setPost_seq(post.getSeq());
                }
            }
        } catch (Exception e) {
            return null;
        }
        post.setTorrents(torrentFiles);
        return torrentFiles;
    }

    public List<PostImageDTO> savePostImage(PostDTO postDTO) {

        List<PostImageDTO> postImageDTOS = new ArrayList<>();


        try {
            Document tmpDocument = Jsoup.connect(postDTO.getPost_link())
                    .header("Origin", this.SITE_URL)
                    .header("Referer", board.getBoardUrl())
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Content-Type", "text/html;charset=iso-8859-1")
                    .method(Connection.Method.GET)
                    .ignoreContentType(true)
                    .get();
            //TODO 셀렉터 등록하기
            Elements elems = tmpDocument.select("#writeContents > article > img");
            for (Element elem : elems){
                Pattern p = Pattern.compile("\\.\\.[/_0-9a-zA-Z]*\\.(jpg|png|gif)");
                Matcher m = p.matcher(elem.toString());

                if(m.find()){
                    if (m.group(0) !=null){
                        Connection.Response response = Jsoup.connect( this.SITE_URL + m.group(0).replace("..",""))
                                .header("Origin", "http://torrentlin.com")
                                .header("Referer", postDTO.getPost_link())
                                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                                .header("Content-Type", "text/html;charset=iso-8859-1")
                                .header("Accept-Encoding", "gzip, deflate, br")
                                .method(Connection.Method.GET)
                                .ignoreContentType(true)
                                .execute();

                        String IMG_PATH = "img/" + m.group(0).split("/")[5];
                        FileOutputStream out = new FileOutputStream(new java.io.File(IMG_PATH));
                        out.write(response.bodyAsBytes());
                        out.close();

                        PostImageDTO postImageDTO = new PostImageDTO(IMG_PATH,"");
                        postImageDTO.setPost_seq(postDTO.getSeq());
                        postImageDTOS.add(postImageDTO);
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return postImageDTOS;
    }

    public PostDTO getLastPost(BoardDTO board) throws Exception{
        this.board = board;
        setBoard(this.board);
        thisPostNum = this.board.getSelector().LAST_UPDATED_IDX;
        return getPost(this.board, this.thisPostNum);
    }

    public PostDTO getPrevPost() throws Exception{
        this.thisPostNum--;
        return getPost(this.board, this.thisPostNum);
    }

    public List<PostDTO> testMethod(){
        List<PostDTO> posts = new ArrayList<>();
        try {
            posts = getBoardsPosts(boards,LocalDateTime.of(2018,8,1,0,0),LocalDateTime.of(2018,10,7,1,1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }
}
