package cn.itcast.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class IndexDeleteTest {
    @Test
    public void testDeleteIndex() throws IOException {


        //创建目录,使用工厂设计模式动态匹配，
        Directory directory = FSDirectory.open(new File("indexDir"));

        //分词器，标准的英文分词器
        Analyzer analyzer = new IKAnalyzer();

        //索引写出的配置，两个参数，分别为版本，和分词器
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST,analyzer);

        //CREATE_OR_APPEND默认值，索引库不存在，则创建，存在则向内部追加数据，
        //CREATE每次都新建
        //config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        //索引写出器的创建，两个参数，写出目录，写出配置
        IndexWriter indexWriter = new IndexWriter(directory,config);



        //删除所有
        //indexWriter.deleteAll();

        //根据词条查询的删除不能够删除数值的类型
        indexWriter.deleteDocuments(new Term("id","3"));

        Query query = NumericRangeQuery.newLongRange("id",3L,3L,true,true);

        //根据查询删除，需要什么查询写什么，
        indexWriter.deleteDocuments(query);

        indexWriter.commit();

        indexWriter.close();
    }

}
