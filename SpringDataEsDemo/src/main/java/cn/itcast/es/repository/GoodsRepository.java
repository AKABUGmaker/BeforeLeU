package cn.itcast.es.repository;

import cn.itcast.es.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {

    /**
     * 根据方法名称推测，根据title查询
     * @param title
     * @return
     */
    List<Goods> findByTitle(String title);

}
