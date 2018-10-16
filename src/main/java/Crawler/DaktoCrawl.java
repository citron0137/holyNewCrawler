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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DaktoCrawl implements Crawler {

    private String SITE_URL = "https://dakto.org";
    private List<BoardDTO> boards = new ArrayList<>();
    private List<Selector> selectors = new ArrayList<>();
    private BoardDTO board;
    private int thisPostNum;


    public DaktoCrawl(){
        /*
        1. 한국영화
         */
        selectors.add(new Selector(
                        "https://dakto.org/d/new_movie",
                        "#fboardlist > div.list-board > ul > li:nth-child(1) > div.wr-subject > a",
                        "https://dakto.org/d/new_movie/", /*모르겠다*/
                        "https://dakto.org/d/new_movie/",
                        "#thema_wrapper > div > div.at-body > div > div > div.col-md-9.at-col.at-main > div.view-wrap > section > article > h1",
                        "",
                        "#thema_wrapper > div > div.at-body > div > div > div.col-md-9.at-col.at-main > div.view-wrap > section > article > div.panel.panel-default.view-head > div.list-group.font-12 > a:nth-child(1) > span.pull-right.hidden-xs.text-muted",
                        "yyyy.MM.dd HH:mm"
                )
        );
        /*
            2. 외국영화
            3. 에로영화
        */
//        selectors.add(new Selector(
//                        "https://torrentlin.com/bbs/board.php?bo_table=torrent_movie_old",
//                        //body > div > table > tbody > tr:nth-child(6) > td > table > tbody > tr > td:nth-child(1) > div:nth-child(2) > table:nth-child(4) > tbody > tr > td > form > table > tbody > tr:nth-child(2) > td.subject
//                        "td.subject a[href]",
//                        "../bbs/board.php?bo_table=torrent_movie_old&wr_id=",
//                        "https://torrentlin.com/bbs/board.php?bo_table=torrent_movie_old&wr_id=",
//                        "body > div > table > tbody > tr:nth-child(6) > td > table > tbody > tr > td:nth-child(1) > div:nth-child(2) > table:nth-child(3) > tbody > tr > td > div:nth-child(2) > table:nth-child(1) > tbody > tr > td:nth-child(1) > h1",
//                        "",
//                        "body > div > table > tbody > tr:nth-child(6) > td > table > tbody > tr > td:nth-child(1) > div:nth-child(2) > table:nth-child(3) > tbody > tr > td > div:nth-child(1) > div:nth-child(1) > span",
//                        "업데이트 : yy-MM-dd HH:mm",
//                        "body > div > table > tbody > tr:nth-child(6) > td > table > tbody > tr > td:nth-child(1) > div:nth-child(2) > table:nth-child(3) > tbody > tr > td > div:nth-child(2) > table:nth-child(3) > tbody > tr:nth-child(", 2, 3, ") > td > div > div:nth-child(1) > a:nth-child(3)",
//                        "body > div > table > tbody > tr:nth-child(6) > td > table > tbody > tr > td:nth-child(1) > div:nth-child(2) > table:nth-child(3) > tbody > tr > td > div:nth-child(2) > table:nth-child(3) > tbody > tr:nth-child(", 2, 3, ") > td > div > div:nth-child(1) > a:nth-child(2)"
//                )
//        );
    }


    public Document connection(String connectionURL) throws Exception{

        Document doc = Jsoup.connect(connectionURL)
                .header("Origin", "http://torrentlin.com")
                .header("Referer", "http://torrentlin.com")
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .header("Content-Type", "text/html;charset=iso-8859-1")
                .method(Connection.Method.GET)
                .ignoreContentType(true)
                .get();
        return doc;
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
            Document rawBoard = connection(board.getBoardUrl());

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

            Document rawPost = Jsoup.connect(board.getSelector().POST_BASE_URL+postNum)
                    .timeout(10000)
//                    .header("Origin", "http://torrentlin.com")
                    .header("Referer", board.getBoardUrl())
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Content-Type", "text/html;charset=iso-8859-1")
                    .method(Connection.Method.GET)
                    .ignoreContentType(true)
                    .get();
        String title = selectFromDoc(rawPost,board.getSelector().TITLE_SELECTOR);
        String userName = selectFromDoc(rawPost,board.getSelector().USER_NAME_SELECTOR);

        String dateString=selectFromDoc(rawPost, board.getSelector().CREATED_DATE_SELECTOR);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(board.getSelector().DATE_TIME_FORMAT);
        LocalDateTime time = LocalDateTime.parse(dateString, formatter);

        PostDTO rstPost = new PostDTO(
                board.getSeq(),
                board.getSelector().POST_BASE_URL+postNum,
                title,userName,time,null,null
        );

        List<PostImageDTO> postImageDTOS = savePostImage(rstPost);
        rstPost.setImages(postImageDTOS);

        List<TorrentFileDTO> torrentFileDTOs = saveTorrentFile(rstPost, board);
        rstPost.setTorrents(torrentFileDTOs);

        System.out.println("");

        return rstPost;
    }


    public List<PostDTO> getPosts(BoardDTO board, LocalDateTime start, LocalDateTime end) {
        int num = board.getSelector().LAST_UPDATED_IDX;
        System.out.println(num);
        List<PostDTO> rstPosts = new ArrayList<>();
        while(true){
            try {
                PostDTO post = getPost(board,num);
                if(post.getPost_date().isBefore(start)) break;
                else if(post.getPost_date().isBefore(end)){
                    rstPosts.add(post);
                }
                else{

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
            result.addAll(getPosts(itrBoard,start,end));
        }
        return result;
    }


    public int crawlTorrentSite() {
        return 0;
    }


    public List<TorrentFileDTO> saveTorrentFile(PostDTO post, BoardDTO boardDTO) {
        List<TorrentFileDTO> torrentFiles = new ArrayList<>();
        Boolean margnetFlag = false;
        String margnetString="";

        try {
            Connection.Response loginPageResponse = Jsoup.connect(post.getPost_link())
                    .timeout(3000)
                    .header("Referer", this.SITE_URL)
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .method(Connection.Method.GET)
                    .execute();
            Map<String, String> cookie = loginPageResponse.cookies();
            Document tmpDocument = loginPageResponse.parse();

            //마그넷 주소 알아오기
            if (!margnetFlag){
                Elements margnetElements = tmpDocument.select("a[href]");
                for (Element ele : margnetElements){
                    if (ele.toString().contains("href=\"magnet")){
                        Pattern pattern  =  Pattern.compile("magnet:\\?[\\=\\:a-z0-9A-Z]+");
                        Matcher match = pattern.matcher(ele.toString());

                        if(match.find()){ // 이미지 태그를 찾았다면,,
                            margnetString = match.group(0); // 글 내용 중에 첫번째 이미지 태그를 뽑아옴.
                            margnetFlag = true;
                        }
                        System.out.println(margnetString);
                    }
                }
            }

            //토렌트 정보 얻기
            Elements elems = tmpDocument.select(boardDTO.getSelector().TORRENT_URL_SELECTOR);
            TorrentFileDTO tmpTorrent = new TorrentFileDTO();
            tmpTorrent.setPost_seq(post.getSeq());

            for (Element elem : elems) {

                if (elem.attr("href").contains("https://dakto.org/download?bo_table=new_movie&wr_id=") && elem.toString().contains(".torrent")) {
                    String strings[] = elem.toString().split("<i class=\"fa fa-download\"></i> ");
                    strings = strings[1].split(".torrent");
                    String torrentFileName = strings[0];

                    //TODO 안깔끔해 아직도
                    String matches = ".\\/\\[\\{\\}\\}\\]\\?\\.\\,\\;\\:\\|\\)\\*\\~\\`\\!\\^\\-\\+\\<\\>\\@\\#\\$\\%\\&\\\\\\=\\(\\'\\\"";
                    torrentFileName = torrentFileName.replaceAll(matches, "")
                            .replace(".", " ") + ".torrent";

//                    System.out.println(torrentFileName);

                    Connection.Response response = Jsoup.connect(elem.attr("href").toString())
                            .header("Referer", post.getPost_link())
                            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                            .header("Content-Type", "text/html;charset=iso-8859-1")
                            .header("Accept-Encoding", "gzip, deflate, br")
//                                    .header("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
                            .method(Connection.Method.GET)
                            .cookies(cookie)
                            .ignoreContentType(true)
                            .execute();

                    //String IMG_PATH = "C:\\crawler\\torrentlin\\img\\" + m.group(0).split("/")[5];
                    String torrentFilePath = "C:\\crawler\\dakto\\torrent\\" + torrentFileName;
//                    System.out.println("토렌트 파일 명 : "+torrentFilePath);

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
        try {
            Connection.Response imgPathResponse = Jsoup.connect(postDTO.getPost_link())
                    .timeout(3000)
//                    .header("Origin", "http://torrentlin.com")
                    .header("Referer", postDTO.getPost_link())
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .method(Connection.Method.GET)
                    .execute();

            Document tmpDocument = imgPathResponse.parse();

            //TODO 셀렉터 등록하기
            String imgSelector = "#thema_wrapper > div > div.at-body > div > div > div.col-md-9.at-col.at-main > div.view-wrap > section > article > div:nth-child(3) > div.view-content > a > img";
            Elements elems = tmpDocument.select("#thema_wrapper > div > div.at-body > div > div > div.col-md-9.at-col.at-main > div.view-wrap > section > article > div:nth-child(3) > div.view-content > a > img");
            for (Element elem : elems){
                String imgInfo = elem.toString();
                System.out.println(elem.toString());

                Pattern pattern = Pattern.compile("src=\"([a-zA-Z0-9\\:\\/\\.\\_\\-]+)");
                Matcher match = pattern.matcher(elem.toString());

                if(match.find()){
                    if (match.group(0) !=null){
                        String imgURL = match.group(0).replace("src=\"","");
//                        System.out.println("이미지 : " + imgURL);
                        Connection.Response response = Jsoup.connect( imgURL)
                                .header("Referer", postDTO.getPost_link())
                                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                                .header("Content-Type", "text/html;charset=iso-8859-1")
                                .header("Accept-Encoding", "gzip, deflate, br")
                                .method(Connection.Method.GET)
                                .ignoreContentType(true)
                                .execute();

                        String IMG_PATH = "C:\\crawler\\dakto\\img\\" + imgURL.split("new_movie/")[1];
                        FileOutputStream out = new FileOutputStream(new java.io.File(IMG_PATH));

//                        FileOutputStream out = new FileOutputStream(new java.io.File(m.group(0).split("/")[5]));
                        out.write(response.bodyAsBytes());
                        out.close();

                        PostImageDTO postImageDTO = new PostImageDTO(IMG_PATH,"");
                        postImageDTO.setPost_seq(postDTO.getSeq());
                        postImageDTOS.add(postImageDTO);
                    }
                }
            }
//            tmpTorrent.setPost_seq(post.getSeq());
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

    //getPost 함수의 첫번째 인자는 post table의 마지막 저장 시간
    //두번째 인자는 현재 시간을 저장하는 방식을 사용
    public List<PostDTO> testMethod(){
        List<PostDTO> posts = new ArrayList<>();
        try {
            posts = getPosts(boards.get(0),LocalDateTime.of(2018,10,1,0,0),LocalDateTime.of(2018,10,13,1,47));
            //posts = getPosts(boards.get(0),LocalDateTime.of(2018,10,1,0,0),LocalDateTime.of(2018,10,7,00,00));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }
}
