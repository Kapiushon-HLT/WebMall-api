
package top.aruoxi.webmall.service.impl;

import top.aruoxi.webmall.api.vo.WebMallIndexCarouselVO;
import top.aruoxi.webmall.dao.CarouselMapper;
import top.aruoxi.webmall.entity.Carousel;
import top.aruoxi.webmall.service.WebMallCarouselService;
import top.aruoxi.webmall.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class WebMallCarouselServiceImpl implements WebMallCarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    public List<WebMallIndexCarouselVO> getCarouselsForIndex(int number) {
        List<WebMallIndexCarouselVO> webMallIndexCarouselVOS = new ArrayList<>(number);
        List<Carousel> carousels = carouselMapper.findCarouselsByNum(number);
        if (!CollectionUtils.isEmpty(carousels)) {
            webMallIndexCarouselVOS = BeanUtil.copyList(carousels, WebMallIndexCarouselVO.class);
        }
        return webMallIndexCarouselVOS;
    }
}
