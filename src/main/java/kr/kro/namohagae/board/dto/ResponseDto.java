package kr.kro.namohagae.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@AllArgsConstructor
public class ResponseDto {
    private PageDto pageDto;
    private List<BoardTownListDto> boardTownListDto;
    private List<BoardTownListDto> boardTownReadCountDto;
    private List<BoardTownListDto> boardTownRecommendCountDto;


    @Data
    @AllArgsConstructor
    public static class waitList {
        private PageDto pageDto;
        private List<KnowledgeMainDto> knowledgeMainDto;
    }
}
