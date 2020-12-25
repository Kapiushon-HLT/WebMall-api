
package top.aruoxi.webmall.service;

import top.aruoxi.webmall.api.vo.WebMallIndexCarouselVO;
import java.util.List;

public interface WebMallCarouselService {

    /**
     * 返回固定数量的轮播图对象(首页调用)
     *
     * @param number
     * @return
     */
    List<WebMallIndexCarouselVO> getCarouselsForIndex(int number);
}
