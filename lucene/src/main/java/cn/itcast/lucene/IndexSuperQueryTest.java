package cn.itcast.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class IndexSuperQueryTest {

    /**
     * 高亮查询，先做普通查询，查询完成，后，做二次分词，分完后根据查询关键字来比对后，在关键字前后拼接<em></em>
     * @throws IOException
     * @throws ParseException
     */

    @Test
    public void highLightSearch() throws IOException, ParseException, InvalidTokenOffsetsException {
        //声明索引存放的目录用来搜索
        Directory directory = FSDirectory.open(new File("indexDir"));

        //创建一个读取对象用来读取索引目录
        IndexReader indexReader = DirectoryReader.open(directory);

        //搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        QueryParser queryParser = new QueryParser("title",new IKAnalyzer());

        Query query = queryParser.parse("谷歌");


        //传入搜索条件以及需要的条目数（前n个）,如果要的比所有命中的还要多，所以有多少显示，多少，最终输出的内容就是命中和需要的两者取小
        TopDocs topDocs = indexSearcher.search(query,Integer.MAX_VALUE );

        //命中的元素的个数
        int totalHits = topDocs.totalHits;

        System.out.println("命中的文档的个数：" + totalHits);

        //得分文档数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;


        //准备高亮的工具
        Formatter formatter = new SimpleHTMLFormatter("<em>","</em>");

        Scorer scorer = new QueryScorer(query);
        Highlighter highlighter = new Highlighter(formatter,scorer);

        for (ScoreDoc scoreDoc : scoreDocs) {
            //当前文档的得分
            float score = scoreDoc.score;
            //文档编号
            int docID = scoreDoc.doc;

            //根据文档id获取具体的文档
            Document document = indexSearcher.doc(docID);

            String id = document.get("id");
            String result = document.get("title");

            //使用高亮工具进行二次分词，并做关键字的高亮匹配
            String highlighterTitle = highlighter.getBestFragment(new IKAnalyzer(), "title", result);

            System.out.println("得分："+score+" id:"+id+" title:"+highlighterTitle);
        }


    }


    //排序查询
    @Test
    public void sortSearch() throws IOException, ParseException, InvalidTokenOffsetsException {
        //声明索引存放的目录用来搜索
        Directory directory = FSDirectory.open(new File("indexDir"));

        //创建一个读取对象用来读取索引目录
        IndexReader indexReader = DirectoryReader.open(directory);

        //搜索对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        QueryParser queryParser = new QueryParser("title",new IKAnalyzer());

        Query query = queryParser.parse("谷歌");

        //声明排序条件,SortField最后一个参数,加true为降序
        Sort sort = new Sort(new SortField("id", SortField.Type.LONG));

        //传入搜索条件以及需要的条目数（前n个）,如果要的比所有命中的还要多，所以有多少显示，多少，最终输出的内容就是命中和需要的两者取小
        TopDocs topDocs = indexSearcher.search(query,Integer.MAX_VALUE ,sort);

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
            String result = document.get("title");

            System.out.println("得分："+score+" id:"+id+" title:"+result);
        }
    }

}
