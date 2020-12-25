
package top.aruoxi.webmall.service.impl;

import top.aruoxi.webmall.api.vo.WebMallOrderDetailVO;
import top.aruoxi.webmall.api.vo.WebMallOrderItemVO;
import top.aruoxi.webmall.api.vo.WebMallOrderListVO;
import top.aruoxi.webmall.api.vo.WebMallShoppingCartItemVO;
import top.aruoxi.webmall.common.*;
import top.aruoxi.webmall.dao.*;
import top.aruoxi.webmall.entity.*;
import top.aruoxi.webmall.service.WebMallOrderService;
import top.aruoxi.webmall.util.BeanUtil;
import top.aruoxi.webmall.util.NumberUtil;
import top.aruoxi.webmall.util.PageQueryUtil;
import top.aruoxi.webmall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class WebMallOrderServiceImpl implements WebMallOrderService {

    @Autowired
    private WebMallOrderMapper webMallOrderMapper;
    @Autowired
    private WebMallOrderItemMapper webMallOrderItemMapper;
    @Autowired
    private WebMallShoppingCartItemMapper webMallShoppingCartItemMapper;
    @Autowired
    private WebMallGoodsMapper webMallGoodsMapper;
    @Autowired
    private WebMallOrderAddressMapper webMallOrderAddressMapper;

    @Override
    public WebMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        WebMallOrder webMallOrder = webMallOrderMapper.selectByOrderNo(orderNo);
        if (webMallOrder == null) {
            WebMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        if (!userId.equals(webMallOrder.getUserId())) {
            WebMallException.fail(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        List<WebMallOrderItem> orderItems = webMallOrderItemMapper.selectByOrderId(webMallOrder.getOrderId());
        //获取订单项数据
        if (!CollectionUtils.isEmpty(orderItems)) {
            List<WebMallOrderItemVO> webMallOrderItemVOS = BeanUtil.copyList(orderItems, WebMallOrderItemVO.class);
            WebMallOrderDetailVO webMallOrderDetailVO = new WebMallOrderDetailVO();
            BeanUtil.copyProperties(webMallOrder, webMallOrderDetailVO);
            webMallOrderDetailVO.setOrderStatusString(WebMallOrderStatusEnum.getNewBeeMallOrderStatusEnumByStatus(webMallOrderDetailVO.getOrderStatus()).getName());
            webMallOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(webMallOrderDetailVO.getPayType()).getName());
            webMallOrderDetailVO.setWebMallOrderItemVOS(webMallOrderItemVOS);
            return webMallOrderDetailVO;
        } else {
            WebMallException.fail(ServiceResultEnum.ORDER_ITEM_NULL_ERROR.getResult());
            return null;
        }
    }

    @Override
    public PageResult getMyOrders(PageQueryUtil pageUtil) {
        int total = webMallOrderMapper.getTotalNewBeeMallOrders(pageUtil);
        List<WebMallOrder> webMallOrders = webMallOrderMapper.findNewBeeMallOrderList(pageUtil);
        List<WebMallOrderListVO> orderListVOS = new ArrayList<>();
        if (total > 0) {
            //数据转换 将实体类转成vo
            orderListVOS = BeanUtil.copyList(webMallOrders, WebMallOrderListVO.class);
            //设置订单状态中文显示值
            for (WebMallOrderListVO webMallOrderListVO : orderListVOS) {
                webMallOrderListVO.setOrderStatusString(WebMallOrderStatusEnum.getNewBeeMallOrderStatusEnumByStatus(webMallOrderListVO.getOrderStatus()).getName());
            }
            List<Long> orderIds = webMallOrders.stream().map(WebMallOrder::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                List<WebMallOrderItem> orderItems = webMallOrderItemMapper.selectByOrderIds(orderIds);
                Map<Long, List<WebMallOrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(WebMallOrderItem::getOrderId));
                for (WebMallOrderListVO webMallOrderListVO : orderListVOS) {
                    //封装每个订单列表对象的订单项数据
                    if (itemByOrderIdMap.containsKey(webMallOrderListVO.getOrderId())) {
                        List<WebMallOrderItem> orderItemListTemp = itemByOrderIdMap.get(webMallOrderListVO.getOrderId());
                        //将NewBeeMallOrderItem对象列表转换成NewBeeMallOrderItemVO对象列表
                        List<WebMallOrderItemVO> webMallOrderItemVOS = BeanUtil.copyList(orderItemListTemp, WebMallOrderItemVO.class);
                        webMallOrderListVO.setWebMallOrderItemVOS(webMallOrderItemVOS);
                    }
                }
            }
        }
        PageResult pageResult = new PageResult(orderListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {
        WebMallOrder webMallOrder = webMallOrderMapper.selectByOrderNo(orderNo);
        if (webMallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            if (webMallOrderMapper.closeOrder(Collections.singletonList(webMallOrder.getOrderId()), WebMallOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        WebMallOrder webMallOrder = webMallOrderMapper.selectByOrderNo(orderNo);
        if (webMallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            webMallOrder.setOrderStatus((byte) WebMallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            webMallOrder.setUpdateTime(new Date());
            if (webMallOrderMapper.updateByPrimaryKeySelective(webMallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String paySuccess(String orderNo, int payType) {
        WebMallOrder webMallOrder = webMallOrderMapper.selectByOrderNo(orderNo);
        if (webMallOrder != null) {
            if (webMallOrder.getOrderStatus().intValue() != WebMallOrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()) {
                WebMallException.fail("非待支付状态下的订单无法支付");
            }
            webMallOrder.setOrderStatus((byte) WebMallOrderStatusEnum.OREDER_PAID.getOrderStatus());
            webMallOrder.setPayType((byte) payType);
            webMallOrder.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
            webMallOrder.setPayTime(new Date());
            webMallOrder.setUpdateTime(new Date());
            if (webMallOrderMapper.updateByPrimaryKeySelective(webMallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    @Transactional
    public String saveOrder(MallUser loginMallUser, MallUserAddress address, List<WebMallShoppingCartItemVO> myShoppingCartItems) {
        List<Long> itemIdList = myShoppingCartItems.stream().map(WebMallShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = myShoppingCartItems.stream().map(WebMallShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
        List<WebMallGoods> webMallGoods = webMallGoodsMapper.selectByPrimaryKeys(goodsIds);
        //检查是否包含已下架商品
        List<WebMallGoods> goodsListNotSelling = webMallGoods.stream()
                .filter(newBeeMallGoodsTemp -> newBeeMallGoodsTemp.getGoodsSellStatus() != Constants.SELL_STATUS_UP)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(goodsListNotSelling)) {
            //goodsListNotSelling 对象非空则表示有下架商品
            WebMallException.fail(goodsListNotSelling.get(0).getGoodsName() + "已下架，无法生成订单");
        }
        Map<Long, WebMallGoods> newBeeMallGoodsMap = webMallGoods.stream().collect(Collectors.toMap(WebMallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        //判断商品库存
        for (WebMallShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
            //查出的商品中不存在购物车中的这条关联商品数据，直接返回错误提醒
            if (!newBeeMallGoodsMap.containsKey(shoppingCartItemVO.getGoodsId())) {
                WebMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //存在数量大于库存的情况，直接返回错误提醒
            if (shoppingCartItemVO.getGoodsCount() > newBeeMallGoodsMap.get(shoppingCartItemVO.getGoodsId()).getStockNum()) {
                WebMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }
        //删除购物项
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(webMallGoods)) {
            if (webMallShoppingCartItemMapper.deleteBatch(itemIdList) > 0) {
                List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(myShoppingCartItems, StockNumDTO.class);
                int updateStockNumResult = webMallGoodsMapper.updateStockNum(stockNumDTOS);
                if (updateStockNumResult < 1) {
                    WebMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }
                //生成订单号
                String orderNo = NumberUtil.genOrderNo();
                int priceTotal = 0;
                //保存订单
                WebMallOrder webMallOrder = new WebMallOrder();
                webMallOrder.setOrderNo(orderNo);
                webMallOrder.setUserId(loginMallUser.getUserId());
                //总价
                for (WebMallShoppingCartItemVO webMallShoppingCartItemVO : myShoppingCartItems) {
                    priceTotal += webMallShoppingCartItemVO.getGoodsCount() * webMallShoppingCartItemVO.getSellingPrice();
                }
                if (priceTotal < 1) {
                    WebMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                webMallOrder.setTotalPrice(priceTotal);
                String extraInfo = "";
                webMallOrder.setExtraInfo(extraInfo);
                //生成订单项并保存订单项纪录
                if (webMallOrderMapper.insertSelective(webMallOrder) > 0) {
                    //生成订单收货地址快照，并保存至数据库
                    WebMallOrderAddress webMallOrderAddress = new WebMallOrderAddress();
                    BeanUtil.copyProperties(address, webMallOrderAddress);
                    webMallOrderAddress.setOrderId(webMallOrder.getOrderId());
                    //生成所有的订单项快照，并保存至数据库
                    List<WebMallOrderItem> webMallOrderItems = new ArrayList<>();
                    for (WebMallShoppingCartItemVO webMallShoppingCartItemVO : myShoppingCartItems) {
                        WebMallOrderItem webMallOrderItem = new WebMallOrderItem();
                        //使用BeanUtil工具类将newBeeMallShoppingCartItemVO中的属性复制到newBeeMallOrderItem对象中
                        BeanUtil.copyProperties(webMallShoppingCartItemVO, webMallOrderItem);
                        //NewBeeMallOrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
                        webMallOrderItem.setOrderId(webMallOrder.getOrderId());
                        webMallOrderItems.add(webMallOrderItem);
                    }
                    //保存至数据库
                    if (webMallOrderItemMapper.insertBatch(webMallOrderItems) > 0 && webMallOrderAddressMapper.insertSelective(webMallOrderAddress) > 0) {
                        //所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
                        return orderNo;
                    }
                    WebMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                WebMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
            WebMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        WebMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
    }
}
