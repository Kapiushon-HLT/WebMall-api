
package top.aruoxi.webmall.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class IndexInfoVO implements Serializable {

    @ApiModelProperty("轮播图(列表)")
    private List<WebMallIndexCarouselVO> carousels;

    @ApiModelProperty("首页热销商品(列表)")
    private List<WebMallIndexConfigGoodsVO> hotGoodses;

    @ApiModelProperty("首页新品推荐(列表)")
    private List<WebMallIndexConfigGoodsVO> newGoodses;

    @ApiModelProperty("首页推荐商品(列表)")
    private List<WebMallIndexConfigGoodsVO> recommendGoodses;
}
