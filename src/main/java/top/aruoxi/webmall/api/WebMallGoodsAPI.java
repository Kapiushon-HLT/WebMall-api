
package top.aruoxi.webmall.api;

import io.swagger.annotations.*;
import top.aruoxi.webmall.api.vo.WebMallSearchGoodsVO;
import top.aruoxi.webmall.common.Constants;
import top.aruoxi.webmall.common.WebMallException;
import top.aruoxi.webmall.common.ServiceResultEnum;
import top.aruoxi.webmall.config.annotation.TokenToMallUser;
import top.aruoxi.webmall.api.vo.WebMallMallGoodsDetailVO;
import top.aruoxi.webmall.entity.MallUser;
import top.aruoxi.webmall.entity.WebMallGoods;
import top.aruoxi.webmall.service.WebMallGoodsService;
import top.aruoxi.webmall.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "v1", tags = "4.WebMall商品相关接口")
@RequestMapping("/api/v1")
public class WebMallGoodsAPI {

    private static final Logger logger = LoggerFactory.getLogger(WebMallGoodsAPI.class);

    @Resource
    private WebMallGoodsService webMallGoodsService;

    @GetMapping("/search")
    @ApiOperation(value = "商品搜索接口", notes = "根据关键字和分类id进行搜索")
    public Result<PageResult<List<WebMallSearchGoodsVO>>> search(@RequestParam(required = false) @ApiParam(value = "搜索关键字") String keyword,
                                                                 @RequestParam(required = false) @ApiParam(value = "分类id") Long goodsCategoryId,
                                                                 @RequestParam(required = false) @ApiParam(value = "orderBy") String orderBy,
                                                                 @RequestParam(required = false) @ApiParam(value = "页码") Integer pageNumber,
                                                                 @TokenToMallUser MallUser loginMallUser) {
        
        logger.info("goods search api,keyword={},goodsCategoryId={},orderBy={},pageNumber={},userId={}", keyword, goodsCategoryId, orderBy, pageNumber, loginMallUser.getUserId());

        Map params = new HashMap(4);
        //两个搜索参数都为空，直接返回异常
        if (goodsCategoryId == null && StringUtils.isEmpty(keyword)) {
            WebMallException.fail("非法的搜索参数");
        }
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        params.put("goodsCategoryId", goodsCategoryId);
        params.put("page", pageNumber);
        params.put("limit", Constants.GOODS_SEARCH_PAGE_LIMIT);
        //对keyword做过滤 去掉空格
        if (!StringUtils.isEmpty(keyword)) {
            params.put("keyword", keyword);
        }
        if (!StringUtils.isEmpty(orderBy)) {
            params.put("orderBy", orderBy);
        }
        //搜索上架状态下的商品
        params.put("goodsSellStatus", Constants.SELL_STATUS_UP);
        //封装商品数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(webMallGoodsService.searchNewBeeMallGoods(pageUtil));
    }

    @GetMapping("/goods/detail/{goodsId}")
    @ApiOperation(value = "商品详情接口", notes = "传参为商品id")
    public Result<WebMallMallGoodsDetailVO> goodsDetail(@ApiParam(value = "商品id") @PathVariable("goodsId") Long goodsId, @TokenToMallUser MallUser loginMallUser) {
        logger.info("goods detail api,goodsId={},userId={}", goodsId, loginMallUser.getUserId());
        if (goodsId < 1) {
            return ResultGenerator.genFailResult("参数异常");
        }
        WebMallGoods goods = webMallGoodsService.getNewBeeMallGoodsById(goodsId);
        if (goods == null) {
            return ResultGenerator.genFailResult("参数异常");
        }
        if (Constants.SELL_STATUS_UP != goods.getGoodsSellStatus()) {
            WebMallException.fail(ServiceResultEnum.GOODS_PUT_DOWN.getResult());
        }
        WebMallMallGoodsDetailVO goodsDetailVO = new WebMallMallGoodsDetailVO();
        BeanUtil.copyProperties(goods, goodsDetailVO);
        goodsDetailVO.setGoodsCarouselList(goods.getGoodsCarousel().split(","));
        return ResultGenerator.genSuccessResult(goodsDetailVO);
    }

}
