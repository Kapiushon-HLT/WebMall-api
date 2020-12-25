
package top.aruoxi.webmall.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import top.aruoxi.webmall.api.param.SaveOrderParam;
import top.aruoxi.webmall.api.vo.WebMallOrderDetailVO;
import top.aruoxi.webmall.api.vo.WebMallOrderListVO;
import top.aruoxi.webmall.common.Constants;
import top.aruoxi.webmall.common.WebMallException;
import top.aruoxi.webmall.common.ServiceResultEnum;
import top.aruoxi.webmall.config.annotation.TokenToMallUser;
import top.aruoxi.webmall.api.vo.WebMallShoppingCartItemVO;
import top.aruoxi.webmall.entity.MallUser;
import top.aruoxi.webmall.entity.MallUserAddress;
import top.aruoxi.webmall.service.WebMallOrderService;
import top.aruoxi.webmall.service.WebMallShoppingCartService;
import top.aruoxi.webmall.service.WebMallUserAddressService;
import top.aruoxi.webmall.util.PageQueryUtil;
import top.aruoxi.webmall.util.PageResult;
import top.aruoxi.webmall.util.Result;
import top.aruoxi.webmall.util.ResultGenerator;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "v1", tags = "7.WebMall订单操作相关接口")
@RequestMapping("/api/v1")
public class WebMallOrderAPI {

    @Resource
    private WebMallShoppingCartService webMallShoppingCartService;
    @Resource
    private WebMallOrderService webMallOrderService;
    @Resource
    private WebMallUserAddressService webMallUserAddressService;

    @PostMapping("/saveOrder")
    @ApiOperation(value = "生成订单接口", notes = "传参为地址id和待结算的购物项id数组")
    public Result<String> saveOrder(@ApiParam(value = "订单参数") @RequestBody SaveOrderParam saveOrderParam, @TokenToMallUser MallUser loginMallUser) {
        int priceTotal = 0;
        if (saveOrderParam == null || saveOrderParam.getCartItemIds() == null || saveOrderParam.getAddressId() == null) {
            WebMallException.fail(ServiceResultEnum.PARAM_ERROR.getResult());
        }
        if (saveOrderParam.getCartItemIds().length < 1) {
            WebMallException.fail(ServiceResultEnum.PARAM_ERROR.getResult());
        }
        List<WebMallShoppingCartItemVO> itemsForSave = webMallShoppingCartService.getCartItemsForSettle(Arrays.asList(saveOrderParam.getCartItemIds()), loginMallUser.getUserId());
        if (CollectionUtils.isEmpty(itemsForSave)) {
            //无数据
            WebMallException.fail("参数异常");
        } else {
            //总价
            for (WebMallShoppingCartItemVO webMallShoppingCartItemVO : itemsForSave) {
                priceTotal += webMallShoppingCartItemVO.getGoodsCount() * webMallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                WebMallException.fail("价格异常");
            }
            MallUserAddress address = webMallUserAddressService.getMallUserAddressById(saveOrderParam.getAddressId());
            if (!loginMallUser.getUserId().equals(address.getUserId())) {
                return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
            }
            //保存订单并返回订单号
            String saveOrderResult = webMallOrderService.saveOrder(loginMallUser, address, itemsForSave);
            Result result = ResultGenerator.genSuccessResult();
            result.setData(saveOrderResult);
            return result;
        }
        return ResultGenerator.genFailResult("生成订单失败");
    }

    @GetMapping("/order/{orderNo}")
    @ApiOperation(value = "订单详情接口", notes = "传参为订单号")
    public Result<WebMallOrderDetailVO> orderDetailPage(@ApiParam(value = "订单号") @PathVariable("orderNo") String orderNo, @TokenToMallUser MallUser loginMallUser) {
        return ResultGenerator.genSuccessResult(webMallOrderService.getOrderDetailByOrderNo(orderNo, loginMallUser.getUserId()));
    }

    @GetMapping("/order")
    @ApiOperation(value = "订单列表接口", notes = "传参为页码")
    public Result<PageResult<List<WebMallOrderListVO>>> orderList(@ApiParam(value = "页码") @RequestParam(required = false) Integer pageNumber,
                                                                  @ApiParam(value = "订单状态:0.待支付 1.待确认 2.待发货 3:已发货 4.交易成功") @RequestParam(required = false) Integer status,
                                                                  @TokenToMallUser MallUser loginMallUser) {
        Map params = new HashMap(4);
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        params.put("userId", loginMallUser.getUserId());
        params.put("orderStatus", status);
        params.put("page", pageNumber);
        params.put("limit", Constants.ORDER_SEARCH_PAGE_LIMIT);
        //封装分页请求参数
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(webMallOrderService.getMyOrders(pageUtil));
    }

    @PutMapping("/order/{orderNo}/cancel")
    @ApiOperation(value = "订单取消接口", notes = "传参为订单号")
    public Result cancelOrder(@ApiParam(value = "订单号") @PathVariable("orderNo") String orderNo, @TokenToMallUser MallUser loginMallUser) {
        String cancelOrderResult = webMallOrderService.cancelOrder(orderNo, loginMallUser.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(cancelOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(cancelOrderResult);
        }
    }

    @PutMapping("/order/{orderNo}/finish")
    @ApiOperation(value = "确认收货接口", notes = "传参为订单号")
    public Result finishOrder(@ApiParam(value = "订单号") @PathVariable("orderNo") String orderNo, @TokenToMallUser MallUser loginMallUser) {
        String finishOrderResult = webMallOrderService.finishOrder(orderNo, loginMallUser.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(finishOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(finishOrderResult);
        }
    }

    @GetMapping("/paySuccess")
    @ApiOperation(value = "模拟支付成功回调的接口", notes = "传参为订单号和支付方式")
    public Result paySuccess(@ApiParam(value = "订单号") @RequestParam("orderNo") String orderNo, @ApiParam(value = "支付方式") @RequestParam("payType") int payType) {
        String payResult = webMallOrderService.paySuccess(orderNo, payType);
        if (ServiceResultEnum.SUCCESS.getResult().equals(payResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(payResult);
        }
    }

}
