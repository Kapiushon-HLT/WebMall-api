
package top.aruoxi.webmall.service;

import top.aruoxi.webmall.api.vo.WebMallIndexConfigGoodsVO;
import java.util.List;

public interface WebMallIndexConfigService {

    /**
     * 返回固定数量的首页配置商品对象(首页调用)
     *
     * @param number
     * @return
     */
    List<WebMallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number);
}
