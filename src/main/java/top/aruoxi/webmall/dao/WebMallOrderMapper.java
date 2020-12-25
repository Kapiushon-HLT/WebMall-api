
package top.aruoxi.webmall.dao;

import top.aruoxi.webmall.entity.WebMallOrder;
import top.aruoxi.webmall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WebMallOrderMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(WebMallOrder record);

    int insertSelective(WebMallOrder record);

    WebMallOrder selectByPrimaryKey(Long orderId);

    WebMallOrder selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(WebMallOrder record);

    int updateByPrimaryKey(WebMallOrder record);

    List<WebMallOrder> findNewBeeMallOrderList(PageQueryUtil pageUtil);

    int getTotalNewBeeMallOrders(PageQueryUtil pageUtil);

    List<WebMallOrder> selectByPrimaryKeys(@Param("orderIds") List<Long> orderIds);

    int checkOut(@Param("orderIds") List<Long> orderIds);

    int closeOrder(@Param("orderIds") List<Long> orderIds, @Param("orderStatus") int orderStatus);

    int checkDone(@Param("orderIds") List<Long> asList);
}