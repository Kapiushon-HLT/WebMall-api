
package top.aruoxi.webmall.dao;

import top.aruoxi.webmall.entity.WebMallShoppingCartItem;
import top.aruoxi.webmall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WebMallShoppingCartItemMapper {
    int deleteByPrimaryKey(Long cartItemId);

    int insert(WebMallShoppingCartItem record);

    int insertSelective(WebMallShoppingCartItem record);

    WebMallShoppingCartItem selectByPrimaryKey(Long cartItemId);

    WebMallShoppingCartItem selectByUserIdAndGoodsId(@Param("newBeeMallUserId") Long newBeeMallUserId, @Param("goodsId") Long goodsId);

    List<WebMallShoppingCartItem> selectByUserId(@Param("newBeeMallUserId") Long newBeeMallUserId, @Param("number") int number);

    List<WebMallShoppingCartItem> selectByUserIdAndCartItemIds(@Param("newBeeMallUserId") Long newBeeMallUserId, @Param("cartItemIds") List<Long> cartItemIds);

    int selectCountByUserId(Long newBeeMallUserId);

    int updateByPrimaryKeySelective(WebMallShoppingCartItem record);

    int updateByPrimaryKey(WebMallShoppingCartItem record);

    int deleteBatch(List<Long> ids);

    List<WebMallShoppingCartItem> findMyNewBeeMallCartItems(PageQueryUtil pageUtil);

    int getTotalMyNewBeeMallCartItems(PageQueryUtil pageUtil);
}