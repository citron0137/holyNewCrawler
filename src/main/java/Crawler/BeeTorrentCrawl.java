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
import java.net.URLDecoder;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BeeTorrentCrawl implements Crawler{
    private String SITE_URL = "https://beetorrent.com";
    private List<BoardDTO> boards = new ArrayList<>();
    private List<Selector> selectors = new ArrayList<>();
    private BoardDTO board;
    private int thisPostNum;

    public BeeTorrentCrawl(){
        selectors.add(new Selector(
                        "https://beetorrent.com/영화/최신영화/?board_name=newmovie",
                        "#newmovie_board_box > div.col-md-12.col-xs-12.basic-post-gallery > div:nth-child(1) > div > div.post-content.text-center > div.post-subject > a",
                        "/최신영화?board_name=newmovie&vid=", /*보류 : ../bbs/board.php?bo_table=torrent_movie_new&wr_id=*/
                        "https://beetorrent.com/영화/최신영화/?board_name=newmovie&vid=",
                        "#mb_newmovie_tr_title > td > span:nth-child(1)",
                        "",
                        "#mb_newmovie_tr_title > td > span:nth-child(2)",
                        "yyyy-MM-dd HH:mm"
                )
        );
        selectors.add(new Selector(
                        "https://beetorrent.com/영화/지난영화/?board_name=oldmovie",
                        "#oldmovie_board_box > div.col-md-12.col-xs-12.basic-post-gallery > div:nth-child(1) > div > div.post-content.text-center > div.post-subject > a",
                        "/지난영화?board_name=oldmovie&vid=",
                        "https://beetorrent.com/영화/지난영화/?board_name=oldmovie&vid=",
                        "#mb_oldmovie_tr_title > td > span:nth-child(1)",
                        "",
                        "#mb_oldmovie_tr_title > td > span:nth-child(2)",
                        "yyyy-MM-dd HH:mm"
                )
        );

        selectors.add(new Selector(
                        "https://beetorrent.com/영화/고화질영화/?board_name=hdmovie",
                        "#hdmovie_board_box > div.col-md-12.col-xs-12.basic-post-gallery > div:nth-child(1) > div > div.post-content.text-center > div.post-subject > a",
                        "/고화질영화/?board_name=hdmovie&vid=",
                        "https://beetorrent.com/영화/고화질영화/?board_name=hdmovie&vid=",
                        "#mb_hdmovie_tr_title > td > span:nth-child(1)",
                        "",
                        "#mb_hdmovie_tr_title > td > span:nth-child(2)",
                        "yyyy-MM-dd HH:mm"
                )
        );
    }


    public void login(String id, String pw) {

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
                    .header("Origin", "http://beetorrent.com")
                    .header("Referer", "http://beetorrent.com")
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

    private String selectFromDoc(Document doc, String selector){
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

        Thread.sleep(10000);
        System.out.println("시간 확인");

        Document rawPost = Jsoup.connect(board.getSelector().POST_BASE_URL+postNum)
                .header("Origin", this.SITE_URL)
                .header("Referer", board.getBoardUrl())
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Content-Type", "text/html;charset=iso-8859-1")
                .method(Connection.Method.GET)
                .ignoreContentType(true)
                .get();
        String title = selectFromDoc(rawPost,board.getSelector().TITLE_SELECTOR);
        String userName = selectFromDoc(rawPost,board.getSelector().USER_NAME_SELECTOR);
//        String dateString=selectFromDoc(rawPost, board.getSelector().CREATED_DATE_SELECTOR) ;
//        if(dateString !="-"){
//            dateString = dateString + " 00:00";
//        }
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(board.getSelector().DATE_TIME_FORMAT);
//        LocalDateTime time = LocalDateTime.parse(dateString, formatter);
//        PostDTO rstPost = new PostDTO(
//                board.getSeq(),
//                board.getSelector().POST_BASE_URL+postNum,
//                title,userName,time,null,null
//        );


        //tmp
        LocalDateTime dateTime = LocalDateTime.now();
        PostDTO rstPost = new PostDTO(
                board.getSeq(),
                board.getSelector().POST_BASE_URL+postNum,
                title,userName,dateTime,null,null
        );



        List<PostImageDTO> postImageDTOS = savePostImage(rstPost);
        rstPost.setImages(postImageDTOS);

        List<TorrentFileDTO> torrentFileDTOs = saveTorrentFile(rstPost, board);
        rstPost.setTorrents(torrentFileDTOs);

        return rstPost;
    }






    public int crawlTorrentSite() {

        return 0;
    }

    public List<TorrentFileDTO> saveTorrentFile(PostDTO post) {
        return null;
    }

    public List<TorrentFileDTO> saveTorrentFile(PostDTO post, BoardDTO boardDTO) {

        List<TorrentFileDTO> torrentFiles = new ArrayList<>();
        Boolean margnetFlag = false;
        String margnetString="";
        //mode=file&board_action=file_download&board_name=[보드명]&file_pid=[크롤링ID]&file_name=+
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

            if (!margnetFlag){
                Elements margnetElements = tmpDocument.select("a[href]");
                for (Element ele : margnetElements){
                    if (ele.toString().contains("href=\"magnet")){
                        Pattern pattern  =  Pattern.compile("magnet:\\?[\\=\\:a-z0-9A-Z]+");
                        Matcher match = pattern.matcher(ele.toString());

                        if(match.find()){ // 이미지 태그를 찾았다면,,
                            margnetString = match.group(0); // 글 내용 중에 첫번째 이미지 태그를 뽑아옴.
                            margnetFlag = true;
                            break;
                        }
                    }
                }
            }

            //토렌트 정보 얻기
            Elements elems = tmpDocument.select("span");
            String TORRENT_FILE_DOWNLOAD_URL = "https://beetorrent.com/?mb_ext=file&path=bmV3bW92aWUvMjcyODMwMDEzNV9YZGp5d0xZRl8xNzViYzMxMjg1MjFmMTBfZDMwMjRmY2U4YzE4MWEzNWNlYzA4Zjk0OWJmMTNlMzk0YzdlMTRhYS50b3JyZW50&type=download&file_name=";
            TorrentFileDTO tmpTorrent = new TorrentFileDTO();
            tmpTorrent.setPost_seq(post.getSeq());

            for (Element elem : elems) {
                if (elem.toString().contains(".torrent")){
                    String torrentFileName;
                    torrentFileName =  elem.toString().replace("<span>","").replace("</span>","");
                    System.out.println(torrentFileName);

                    Connection.Response response = Jsoup.connect(TORRENT_FILE_DOWNLOAD_URL + torrentFileName)
                            .header("Referer", post.getPost_link())
                            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                            .header("Content-Type", "text/html;charset=iso-8859-1")
                            .header("Accept-Encoding", "gzip, deflate, br")
                            .method(Connection.Method.GET)
                            .cookies(cookie)
                            .ignoreContentType(true)
                            .execute();

                    String torrentFilePath = "C:\\crawler\\beetorrent\\torrent\\" + torrentFileName;
                    FileOutputStream out = new FileOutputStream(new java.io.File(torrentFilePath));
                    out.write(response.bodyAsBytes());
                    out.close();
                    tmpTorrent.setInfohash_dir(torrentFilePath);
                    tmpTorrent.setMagnet(margnetString);
                }
                torrentFiles.add(tmpTorrent);
            }
        } catch (Exception e) {
            return null;
        }
        return torrentFiles;
    }

    public List<PostImageDTO> savePostImage(PostDTO postDTO) {
        List<PostImageDTO> postImageDTOS = new ArrayList<>();
        String saveImgPath ="";

        try {
            Connection.Response imgPathResponse = Jsoup.connect(postDTO.getPost_link())
                    .timeout(3000)
                    .header("Origin", "http://torrentlin.com")
                    .header("Referer", "http://torrentlin.com")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .method(Connection.Method.GET)
                    .execute();

            Document tmpDocument = imgPathResponse.parse();

            Elements elems = tmpDocument.select("#mb_newmovie_tr_file_image > td > div > img");
            for (Element elem : elems){

                Pattern p = Pattern.compile("(img src=\")([\\/\\-\\_0-9a-zA-Z]*)");
                Matcher m = p.matcher(elem.toString());
                if (m.find()){
                    if (m.group(2) != null && m.group(2).contains("/wp-content")){
                        String imgPath = m.group(2);
                        System.out.println(m.group(2));
                        Connection.Response response = Jsoup.connect(this.SITE_URL + imgPath)
                                .header("Origin", "http://torrentlin.com")
                                .header("Referer", postDTO.getPost_link())
                                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                                .header("Content-Type", "text/html;charset=iso-8859-1")
                                .header("Accept-Encoding", "gzip, deflate, br")
                                .method(Connection.Method.GET)
                                .ignoreContentType(true)
                                .execute();

                        //이미지 파일 저장
                        saveImgPath ="C:\\crawler\\beetorrent\\img\\" + imgPath.split("/")[5] + ".jpg";
                        FileOutputStream out = new FileOutputStream(new java.io.File(saveImgPath));
                        out.write(response.bodyAsBytes());
                        out.close();

                    }
                }

                System.out.println(saveImgPath);
                PostImageDTO postImageDTO = new PostImageDTO(saveImgPath,"");
                postImageDTO.setPost_seq(postDTO.getSeq());

                postImageDTOS.add(postImageDTO);
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


}
