
package top.aruoxi.webmall.dao;

import top.aruoxi.webmall.entity.WebMallGoods;
import top.aruoxi.webmall.entity.StockNumDTO;
import top.aruoxi.webmall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WebMallGoodsMapper {
    int deleteByPrimaryKey(Long goodsId);

    int insert(WebMallGoods record);

    int insertSelective(WebMallGoods record);

    WebMallGoods selectByPrimaryKey(Long goodsId);

    int updateByPrimaryKeySelective(WebMallGoods record);

    int updateByPrimaryKeyWithBLOBs(WebMallGoods record);

    int updateByPrimaryKey(WebMallGoods record);

    List<WebMallGoods> findNewBeeMallGoodsList(PageQueryUtil pageUtil);

    int getTotalNewBeeMallGoods(PageQueryUtil pageUtil);

    List<WebMallGoods> selectByPrimaryKeys(List<Long> goodsIds);

    List<WebMallGoods> findNewBeeMallGoodsListBySearch(PageQueryUtil pageUtil);

    int getTotalNewBeeMallGoodsBySearch(PageQueryUtil pageUtil);

    int batchInsert(@Param("webMallGoodsList") List<WebMallGoods> webMallGoodsList);

    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    int batchUpdateSellStatus(@Param("orderIds")Long[] orderIds,@Param("sellStatus") int sellStatus);

}