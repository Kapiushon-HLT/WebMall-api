
package top.aruoxi.webmall.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import top.aruoxi.webmall.api.param.SaveCartItemParam;
import top.aruoxi.webmall.api.param.UpdateCartItemParam;
import top.aruoxi.webmall.common.Constants;
import top.aruoxi.webmall.common.WebMallException;
import top.aruoxi.webmall.common.ServiceResultEnum;
import top.aruoxi.webmall.config.annotation.TokenToMallUser;
import top.aruoxi.webmall.api.vo.WebMallShoppingCartItemVO;
import top.aruoxi.webmall.entity.MallUser;
import top.aruoxi.webmall.entity.WebMallShoppingCartItem;
import top.aruoxi.webmall.service.WebMallShoppingCartService;
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
@Api(value = "v1", tags = "5.WebMall购物车相关接口")
@RequestMapping("/api/v1")
public class WebMallShoppingCartAPI {

    @Resource
    private WebMallShoppingCartService webMallShoppingCartService;

    @GetMapping("/shop-cart/page")
    @ApiOperation(value = "购物车列表(每页默认5条)", notes = "传参为页码")
    public Result<PageResult<List<WebMallShoppingCartItemVO>>> cartItemPageList(Integer pageNumber, @TokenToMallUser MallUser loginMallUser) {
        Map params = new HashMap(4);
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        params.put("userId", loginMallUser.getUserId());
        params.put("page", pageNumber);
        params.put("limit", Constants.SHOPPING_CART_PAGE_LIMIT);
        //封装分页请求参数
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(webMallShoppingCartService.getMyShoppingCartItems(pageUtil));
    }

    @GetMapping("/shop-cart")
    @ApiOperation(value = "购物车列表(网页移动端不分页)", notes = "")
    public Result<List<WebMallShoppingCartItemVO>> cartItemList(@TokenToMallUser MallUser loginMallUser) {
        return ResultGenerator.genSuccessResult(webMallShoppingCartService.getMyShoppingCartItems(loginMallUser.getUserId()));
    }

    @PostMapping("/shop-cart")
    @ApiOperation(value = "添加商品到购物车接口", notes = "传参为商品id、数量")
    public Result saveNewBeeMallShoppingCartItem(@RequestBody SaveCartItemParam saveCartItemParam,
                                                 @TokenToMallUser MallUser loginMallUser) {
        String saveResult = webMallShoppingCartService.saveNewBeeMallCartItem(saveCartItemParam, loginMallUser.getUserId());
        //添加成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult(saveResult);
    }

    @PutMapping("/shop-cart")
    @ApiOperation(value = "修改购物项数据", notes = "传参为购物项id、数量")
    public Result updateNewBeeMallShoppingCartItem(@RequestBody UpdateCartItemParam updateCartItemParam,
                                                   @TokenToMallUser MallUser loginMallUser) {
        String updateResult = webMallShoppingCartService.updateNewBeeMallCartItem(updateCartItemParam, loginMallUser.getUserId());
        //修改成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(updateResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult(updateResult);
    }

    @DeleteMapping("/shop-cart/{newBeeMallShoppingCartItemId}")
    @ApiOperation(value = "删除购物项", notes = "传参为购物项id")
    public Result updateNewBeeMallShoppingCartItem(@PathVariable("newBeeMallShoppingCartItemId") Long newBeeMallShoppingCartItemId,
                                                   @TokenToMallUser MallUser loginMallUser) {
        WebMallShoppingCartItem newBeeMallCartItemById = webMallShoppingCartService.getNewBeeMallCartItemById(newBeeMallShoppingCartItemId);
        if (!loginMallUser.getUserId().equals(newBeeMallCartItemById.getUserId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        Boolean deleteResult = webMallShoppingCartService.deleteById(newBeeMallShoppingCartItemId);
        //删除成功
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }

    @GetMapping("/shop-cart/settle")
    @ApiOperation(value = "根据购物项id数组查询购物项明细", notes = "确认订单页面使用")
    public Result<List<WebMallShoppingCartItemVO>> toSettle(Long[] cartItemIds, @TokenToMallUser MallUser loginMallUser) {
        if (cartItemIds.length < 1) {
            WebMallException.fail("参数异常");
        }
        int priceTotal = 0;
        List<WebMallShoppingCartItemVO> itemsForSettle = webMallShoppingCartService.getCartItemsForSettle(Arrays.asList(cartItemIds), loginMallUser.getUserId());
        if (CollectionUtils.isEmpty(itemsForSettle)) {
            //无数据则抛出异常
            WebMallException.fail("参数异常");
        } else {
            //总价
            for (WebMallShoppingCartItemVO webMallShoppingCartItemVO : itemsForSettle) {
                priceTotal += webMallShoppingCartItemVO.getGoodsCount() * webMallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                WebMallException.fail("价格异常");
            }
        }
        return ResultGenerator.genSuccessResult(itemsForSettle);
    }
}
