
package top.aruoxi.webmall.service;

import top.aruoxi.webmall.entity.WebMallGoods;
import top.aruoxi.webmall.util.PageQueryUtil;
import top.aruoxi.webmall.util.PageResult;

public interface WebMallGoodsService {

    /**
     * 获取商品详情
     *
     * @param id
     * @return
     */
    WebMallGoods getNewBeeMallGoodsById(Long id);

    /**
     * 商品搜索
     *
     * @param pageUtil
     * @return
     */
    PageResult searchNewBeeMallGoods(PageQueryUtil pageUtil);
}
