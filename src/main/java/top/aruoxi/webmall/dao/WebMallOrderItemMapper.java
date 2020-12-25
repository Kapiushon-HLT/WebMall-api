
package top.aruoxi.webmall.dao;

import top.aruoxi.webmall.entity.WebMallOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WebMallOrderItemMapper {
    int deleteByPrimaryKey(Long orderItemId);

    int insert(WebMallOrderItem record);

    int insertSelective(WebMallOrderItem record);

    WebMallOrderItem selectByPrimaryKey(Long orderItemId);

    /**
     * 根据订单id获取订单项列表
     *
     * @param orderId
     * @return
     */
    List<WebMallOrderItem> selectByOrderId(Long orderId);

    /**
     * 根据订单ids获取订单项列表
     *
     * @param orderIds
     * @return
     */
    List<WebMallOrderItem> selectByOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 批量insert订单项数据
     *
     * @param orderItems
     * @return
     */
    int insertBatch(@Param("orderItems") List<WebMallOrderItem> orderItems);

    int updateByPrimaryKeySelective(WebMallOrderItem record);

    int updateByPrimaryKey(WebMallOrderItem record);
}