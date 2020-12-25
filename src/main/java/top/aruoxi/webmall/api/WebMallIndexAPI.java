
package top.aruoxi.webmall.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import top.aruoxi.webmall.common.Constants;
import top.aruoxi.webmall.common.IndexConfigTypeEnum;
import top.aruoxi.webmall.api.vo.IndexInfoVO;
import top.aruoxi.webmall.api.vo.WebMallIndexCarouselVO;
import top.aruoxi.webmall.api.vo.WebMallIndexConfigGoodsVO;
import top.aruoxi.webmall.service.WebMallCarouselService;
import top.aruoxi.webmall.service.WebMallIndexConfigService;
import top.aruoxi.webmall.util.Result;
import top.aruoxi.webmall.util.ResultGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "1.WebMall首页接口")
@RequestMapping("/api/v1")
public class WebMallIndexAPI {

    @Resource
    private WebMallCarouselService webMallCarouselService;

    @Resource
    private WebMallIndexConfigService webMallIndexConfigService;

    @GetMapping("/index-infos")
    @ApiOperation(value = "获取首页数据", notes = "轮播图、新品、推荐等")
    public Result<IndexInfoVO> indexInfo() {
        IndexInfoVO indexInfoVO = new IndexInfoVO();
        List<WebMallIndexCarouselVO> carousels = webMallCarouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<WebMallIndexConfigGoodsVO> hotGoodses = webMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        List<WebMallIndexConfigGoodsVO> newGoodses = webMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        List<WebMallIndexConfigGoodsVO> recommendGoodses = webMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.INDEX_GOODS_RECOMMOND_NUMBER);
        indexInfoVO.setCarousels(carousels);
        indexInfoVO.setHotGoodses(hotGoodses);
        indexInfoVO.setNewGoodses(newGoodses);
        indexInfoVO.setRecommendGoodses(recommendGoodses);
        return ResultGenerator.genSuccessResult(indexInfoVO);
    }
}
