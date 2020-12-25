
package top.aruoxi.webmall.service;

import top.aruoxi.webmall.api.vo.WebMallIndexCategoryVO;
import top.aruoxi.webmall.entity.GoodsCategory;

import java.util.List;

public interface WebMallCategoryService {

    String saveCategory(GoodsCategory goodsCategory);

    String updateGoodsCategory(GoodsCategory goodsCategory);

    GoodsCategory getGoodsCategoryById(Long id);

    Boolean deleteBatch(Integer[] ids);

    /**
     * 返回分类数据(首页调用)
     *
     * @return
     */
    List<WebMallIndexCategoryVO> getCategoriesForIndex();
}
