
package top.aruoxi.webmall.service.impl;

import top.aruoxi.webmall.api.vo.WebMallIndexConfigGoodsVO;
import top.aruoxi.webmall.dao.IndexConfigMapper;
import top.aruoxi.webmall.dao.WebMallGoodsMapper;
import top.aruoxi.webmall.entity.IndexConfig;
import top.aruoxi.webmall.entity.WebMallGoods;
import top.aruoxi.webmall.service.WebMallIndexConfigService;
import top.aruoxi.webmall.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WebMallIndexConfigServiceImpl implements WebMallIndexConfigService {

    @Autowired
    private IndexConfigMapper indexConfigMapper;

    @Autowired
    private WebMallGoodsMapper goodsMapper;

    @Override
    public List<WebMallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number) {
        List<WebMallIndexConfigGoodsVO> webMallIndexConfigGoodsVOS = new ArrayList<>(number);
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigsByTypeAndNum(configType, number);
        if (!CollectionUtils.isEmpty(indexConfigs)) {
            //取出所有的goodsId
            List<Long> goodsIds = indexConfigs.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
            List<WebMallGoods> webMallGoods = goodsMapper.selectByPrimaryKeys(goodsIds);
            webMallIndexConfigGoodsVOS = BeanUtil.copyList(webMallGoods, WebMallIndexConfigGoodsVO.class);
            for (WebMallIndexConfigGoodsVO webMallIndexConfigGoodsVO : webMallIndexConfigGoodsVOS) {
                String goodsName = webMallIndexConfigGoodsVO.getGoodsName();
                String goodsIntro = webMallIndexConfigGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 30) {
                    goodsName = goodsName.substring(0, 30) + "...";
                    webMallIndexConfigGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 22) {
                    goodsIntro = goodsIntro.substring(0, 22) + "...";
                    webMallIndexConfigGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        return webMallIndexConfigGoodsVOS;
    }
}
