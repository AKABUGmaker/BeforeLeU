package cn.itcast.es.test;

import cn.itcast.EsDemo;
import cn.itcast.es.pojo.Goods;
import cn.itcast.es.repository.GoodsRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.elasticsearch.index.query.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)//声明运行环境spring
@SpringBootTest(classes = EsDemo.class)//直接使用启动类扫描加载对应的资源信息
public class EsTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    @Autowired
    private GoodsRepository goodsRepository;

    /**
     * 借助springData,直接使用pojo,创建索引库
     * 解析注解,根据注解配置的内容,生成指定的索引库
     *
     */
    @Test
    public void testCreateIndexes(){

        this.elasticsearchTemplate.createIndex(Goods.class);
    }


    @Test
    public void testAddMapping(){

        this.elasticsearchTemplate.putMapping(Goods.class);
    }


    @Test
    public void testAddData() {
        Goods goods = new Goods(1L, "小米手机9", " 手机",
                "小米", 3499.00, "http://image.leyou.com/13123.jpg");

        this.goodsRepository.save(goods);
    }

    @Test
    public void addDocuments(){
        // 准备文档数据：
        List<Goods> list = new ArrayList<>();
        list.add(new Goods(1L, "小米手机7", "手机", "小米", 3299.00, "/13123.jpg"));
        list.add(new Goods(2L, "坚果手机R1", "手机", "锤子", 3699.00, "/13123.jpg"));
        list.add(new Goods(3L, "华为META10", "手机", "华为", 4499.00, "/13123.jpg"));
        list.add(new Goods(4L, "小米Mix2S", "手机", "小米", 4299.00, "/13123.jpg"));
        list.add(new Goods(5L, "荣耀V10", "手机", "华为", 2799.00, "/13123.jpg"));

        // 添加索引数据
        goodsRepository.saveAll(list);
    }

    @Test
    public void testQueryById(){
        Optional<Goods> goodsOptional = goodsRepository.findById(3L);
        System.out.println(goodsOptional.orElse(null));
    }

    @Test
    public void testQueryFindByTitle() {

        List<Goods> goodsList = this.goodsRepository.findByTitle("手机");

        goodsList.forEach(goods -> System.out.println(goods));
    }

    /**
     * 自定义查询
     */
    @Test
    public void testQueryBySelf() {

        //查询条件构建工具
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //添加查询条件,并且限定要对查询条件进行完整匹配operator(Operator.AND)
        queryBuilder.withQuery(QueryBuilders.matchQuery("title","小米手机").operator(Operator.AND));

        //执行查询
        AggregatedPage<Goods> goodsAggregatedPage = elasticsearchTemplate.queryForPage(queryBuilder.build(), Goods.class);

        //获取查询到的内容
        List<Goods> goodsList = goodsAggregatedPage.getContent();

        goodsList.forEach(goods -> System.out.println("goods = " + goods));

    }

    @Test
    public void testAggs(){


        //查询条件构建工具
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //分页问题，springdata中，起始页0，现实中起始页为1，所以要每次要-1
        queryBuilder.withPageable(PageRequest.of(0,1));
        //添加_source过滤，没有写任何内容，其实表达了，什么都不要
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{""},null));

        //添加聚合条件，根据词条内容进行聚合
        queryBuilder.addAggregation(AggregationBuilders.terms("brands").field("brand"));

        //执行查询聚合
        AggregatedPage<Goods> goodsAggregatedPage = this.elasticsearchTemplate.queryForPage(queryBuilder.build(), Goods.class);


        //从查询聚合结果中获取到所有的聚合
        Aggregations aggregations = goodsAggregatedPage.getAggregations();

        //从所有的聚合中，根据聚合名称获取到对应聚合结果
        StringTerms brands = aggregations.get("brands");

        //获取聚合分桶
        List<StringTerms.Bucket> brandsBuckets = brands.getBuckets();

        for (StringTerms.Bucket brandsBucket : brandsBuckets) {

            long docCount = brandsBucket.getDocCount();

            String key = brandsBucket.getKeyAsString();

            System.out.println(key+" 有 "+docCount+" 个");
        }
    }

    @Test
    public void testSubAggs(){
        //查询构建工具
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        //添加聚合条件
        queryBuilder.addAggregation(
                AggregationBuilders.terms("brands").field("brand")
                        .subAggregation(AggregationBuilders.avg("avg_price").field("price")));

        //执行查询，获取到聚合结果
        AggregatedPage<Goods> goodsAggregatedPage = this.elasticsearchTemplate.queryForPage(queryBuilder.build(), Goods.class);

        //获取到所有的聚合结果
        Aggregations aggregations = goodsAggregatedPage.getAggregations();

        //根据聚合结果的名称获取对应的聚合
        StringTerms brands = aggregations.get("brands");


        List<StringTerms.Bucket> buckets = brands.getBuckets();


        buckets.forEach(bucket -> {
            String key = bucket.getKeyAsString();

            long docCount = bucket.getDocCount();

            //获取子聚合中的所有内容，然后，根据聚合名称依次获取，一定要注意聚合的返回类型
            InternalAvg internalAvg = bucket.getAggregations().get("avg_price");

            double value = internalAvg.getValue();

            System.out.println(key+" 有 "+docCount+" 个 ,平均价格："+value);
        });

    }


}
