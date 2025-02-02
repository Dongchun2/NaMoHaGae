package kr.kro.namohagae.mall.dao;

import kr.kro.namohagae.mall.dto.ProductDto;
import kr.kro.namohagae.mall.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductDao {
    // 상품 정보 저장
    public Integer save(Product product);

    // 특정 카테고리의 상품 개수를 조회
    public Integer count(Integer categoryNo);

    // 상품 목록 조회
    public List<ProductDto.ReadAll> findAll(Integer startRowNum, Integer endRowNum, Integer categoryNo);

    // 상품 상세 보기
    public ProductDto.Read findByProductNo(Integer productNo);

    // 상품명으로 검색
    public List<Product> findByProductName(String productName);

    // 필터(최신순) 검색
    public List<ProductDto.ReadAll> findAllByLatestOrder(Integer startRowNum, Integer endRowNum, Integer categoryNo);

    // 필터(판매량) 검색
    public List<ProductDto.ReadAll> findAllByOrderOfHighSales(Integer startRowNum, Integer endRowNum, Integer categoryNo);

    // 필터(이름순) 검색
    public List<ProductDto.ReadAll> findAllByProductName(Integer startRowNum, Integer endRowNum, Integer categoryNo);
}
