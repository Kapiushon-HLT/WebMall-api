
package top.aruoxi.webmall.service.impl;

import top.aruoxi.webmall.api.vo.WebMallSearchGoodsVO;
import top.aruoxi.webmall.dao.WebMallGoodsMapper;
import top.aruoxi.webmall.entity.WebMallGoods;
import top.aruoxi.webmall.service.WebMallGoodsService;
import top.aruoxi.webmall.util.BeanUtil;
import top.aruoxi.webmall.util.PageQueryUtil;
import top.aruoxi.webmall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class WebMallGoodsServiceImpl implements WebMallGoodsService {

    @Autowired
    private WebMallGoodsMapper goodsMapper;

    @Override
    public WebMallGoods getNewBeeMallGoodsById(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageResult searchNewBeeMallGoods(PageQueryUtil pageUtil) {
        List<WebMallGoods> goodsList = goodsMapper.findNewBeeMallGoodsListBySearch(pageUtil);
        int total = goodsMapper.getTotalNewBeeMallGoodsBySearch(pageUtil);
        List<WebMallSearchGoodsVO> webMallSearchGoodsVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsList)) {
            webMallSearchGoodsVOS = BeanUtil.copyList(goodsList, WebMallSearchGoodsVO.class);
            for (WebMallSearchGoodsVO webMallSearchGoodsVO : webMallSearchGoodsVOS) {
                String goodsName = webMallSearchGoodsVO.getGoodsName();
                String goodsIntro = webMallSearchGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 28) {
                    goodsName = goodsName.substring(0, 28) + "...";
                    webMallSearchGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 30) {
                    goodsIntro = goodsIntro.substring(0, 30) + "...";
                    webMallSearchGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        PageResult pageResult = new PageResult(webMallSearchGoodsVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
}
