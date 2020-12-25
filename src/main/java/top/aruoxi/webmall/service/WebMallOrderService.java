
package top.aruoxi.webmall.service;

import top.aruoxi.webmall.api.vo.WebMallOrderDetailVO;
import top.aruoxi.webmall.api.vo.WebMallShoppingCartItemVO;
import top.aruoxi.webmall.entity.MallUser;
import top.aruoxi.webmall.entity.MallUserAddress;
import top.aruoxi.webmall.util.PageQueryUtil;
import top.aruoxi.webmall.util.PageResult;

import java.util.List;

public interface WebMallOrderService {
    /**
     * 获取订单详情
     *
     * @param orderNo
     * @param userId
     * @return
     */
    WebMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

    /**
     * 我的订单列表
     *
     * @param pageUtil
     * @return
     */
    PageResult getMyOrders(PageQueryUtil pageUtil);

    /**
     * 手动取消订单
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String cancelOrder(String orderNo, Long userId);

    /**
     * 确认收货
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String finishOrder(String orderNo, Long userId);

    String paySuccess(String orderNo, int payType);

    String saveOrder(MallUser loginMallUser, MallUserAddress address, List<WebMallShoppingCartItemVO> itemsForSave);
}
