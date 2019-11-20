package cn.itcast.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class IndexQueryTest {

    @Test
    public void testQuery() throws IOException, ParseException {

        //查询解析器，两个参数 查询字段还有，分词器（查询分词器必须和创建索引分词器一致）
        QueryParser queryParser = new QueryParser("title", new IKAnalyzer());

        Query query = queryParser.parse("谷歌地图");

        commonSearch(query);

    }

    /**
     * 词条查询，查询的条件就是词条，不用分词
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testTermQuery() throws IOException, ParseException {

        Query query = new TermQuery(new Term("title","谷歌地图"));

        commonSearch(query);

    }


    /**
     * 通配符查询，星号，任意多个，问号 任意1个
     * 通配符查询基本逻辑还是词条查询
     */
    @Test
    public void testWildcardQuery() throws IOException, ParseException {

        Query query = new WildcardQuery(new Term("title","谷歌*"));

        commonSearch(query);
    }

    /**
     * 模糊查询
     * 默认允许对当前关键字操作，两次，可以修改，比如把次数改为0，这时就变成词条查询
     * 常见数量为1，表示只允许改1次
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testFuzzyQuery() throws IOException, ParseException {

        Query query = new FuzzyQuery(new Term("title","facebooc"),1);

        commonSearch(query);
    }

    /**
     * 数值范围查询
     * 如果某个字段是数值类型，查询时，必须使用数值范围查询
     */
    @Test
    public void testNumericRangQuery() throws IOException, ParseException {

        //5个参数，查询字段，最小值，最大值，是否包含最小，是否包含最大
        Query query = NumericRangeQuery.newLongRange("id",3L,3L,true,true);

        commonSearch(query);
    }

    /**
     * 组合查询
     * @throws IOException
     * @throws ParseException
     * MUST VS MUST   && 交集
     *
     * SHOULD VS SHOULD || 并集
     *
     * MUST  VS MUST_NOT   !取反
     */
    @Test
    public void testBooleanQuery() throws IOException, ParseException {

        //5个参数，查询字段，最小值，最大值，是否包含最小，是否包含最大
        Query query1 = NumericRangeQuery.newLongRange("id",3L,5L,true,true);
        Query query2 = NumericRangeQuery.newLongRange("id",2L,4L,true,true);
        Query query3 = NumericRangeQuery.newLongRange("id",1L,3L,true,true);

        BooleanQuery query = new BooleanQuery();

        //分别执行query1和query2,然后对结果处理
        query.add(query1, BooleanClause.Occur.MUST);
        query.add(query2, BooleanClause.Occur.MUST);
        query.add(query3, BooleanClause.Occur.MUST);

        commonSearch(query);
    }



    private void commonSearch(Query query) throws IOException, ParseException {
        //声明索引存放的目录用来搜索
        Directory directory = FSDirectory.open(new File("indexDir"));

        //创建一个读取对象用来读取索引目录
        IndexReader indexReader = DirectoryReader.open(directory);

        //搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);


        //传入搜索条件以及需要的条目数（前n个）,如果要的比所有命中的还要多，所以有多少显示，多少，最终输出的内容就是命中和需要的两者取小
        TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);

        //命中的元素的个数
        int totalHits = topDocs.totalHits;

        System.out.println("命中的文档的个数：" + totalHits);

        //得分文档数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        for (ScoreDoc scoreDoc : scoreDocs) {
            //当前文档的得分
            float score = scoreDoc.score;
            //文档编号
            int docID = scoreDoc.doc;

            //根据文档id获取具体的文档
            Document document = indexSearcher.doc(docID);

            String id = document.get("id");
            String title = document.get("title");

            System.out.println("得分：" + score + " id:" + id + " title:" + title);
        }
    }

}
