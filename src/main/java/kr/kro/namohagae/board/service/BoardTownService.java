package kr.kro.namohagae.board.service;

import kr.kro.namohagae.board.dao.BoardTownDao;
import kr.kro.namohagae.board.dto.BoardMainList;
import kr.kro.namohagae.board.dto.BoardTownDto;
import kr.kro.namohagae.board.dto.BoardTownListDto;
import kr.kro.namohagae.board.dto.PageDto;
import kr.kro.namohagae.board.entity.Board;
import kr.kro.namohagae.board.entity.BoardList;
import kr.kro.namohagae.global.entity.Town;
import kr.kro.namohagae.member.dao.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardTownService {

    @Autowired
    BoardTownDao boardTownDao;
    @Autowired
    MemberDao memberDao;
    private final static Integer PAGESIZE = 10;
    private final static Integer BLOCKSIZE =5;

    public void boardTownInsertData(BoardTownDto.write boardTownDto, String userEmail) {

        Board board = boardTownDto.toEntity(boardTownDto.getTownNo(),memberDao.findNoByUsername(userEmail), boardTownDto.getTitle(), boardTownDto.getContent());
        System.out.println("왜안되 이거"+board);
        boardTownDao.boardTownInsertData(board);
    }

    public List<BoardTownListDto> boardTownList(Integer townNo,String searchName,int page) {
        int pageLimit = 10;
        int pagingStart = (page - 1) * pageLimit;
        return boardTownDao.boardTownList(townNo,searchName,pagingStart, pageLimit);

    }

    public List<BoardTownListDto> boardTownReadCountList(Integer townNo, String searchName, int page) {
        int pageLimit = 10;
        int pagingStart = (page - 1) * pageLimit;

        return boardTownDao.boardTownReadCountList(townNo,searchName,pagingStart,pageLimit);
    }

    public List<BoardTownListDto> boardTownRecommendCountList(Integer townNo, String searchName, int page) {
        int pageLimit = 10;
        int pagingStart = (page - 1) * pageLimit;

        return boardTownDao.boardTownRecommendCountList(townNo,searchName,pagingStart,pageLimit);
    }
    public List<BoardMainList> mainReadList(){

        return boardTownDao.mainReadList();
    }

    public List<BoardMainList> mainRecommendList(){

        return boardTownDao.mainRecommendList();
    }

    public BoardList boardTownRead(Integer boardNo) {

        return boardTownDao.boardTownRead(boardNo);
    }



    public void townUpdateData(Board board) {

        boardTownDao.townUpdateData(board);
    }

    public Integer townDeleteData(Integer boardNo) {

        return boardTownDao.townDeleteData(boardNo);
    }
    public List<Town> townList() {
        return boardTownDao.townList();
    }
}
