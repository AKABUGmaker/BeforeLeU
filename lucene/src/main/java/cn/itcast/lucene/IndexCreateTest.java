package cn.itcast.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;

import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class IndexCreateTest {

    @Test
    public void testCreateIndex() throws IOException {

        //创建相对目录,使用工厂设计模式动态匹配
        Directory directory = FSDirectory.open(new File("indexDir"));

        //分词器,标准的英文分词器(换成ik分词器)
        Analyzer analyzer = new IKAnalyzer();

        //索引写出的配置,两个参数:版本和分词器
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST,analyzer);


        //CREATE_OR_APPEND默认值,索引库不存在则创建,存在则向内部追加数据
        //CREATE每次都新建
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        //创建索引写出器,两个参数,目录和配置
        IndexWriter indexWriter = new IndexWriter(directory,config);

        Document document = new Document();

        document.add(new StringField("id","10086", Field.Store.YES));
        document.add(new TextField("title","今天天气不错,很热,给劲,科比", Field.Store.YES));
        document.add(new StoredField("price","19.9"));

        //加钱
        //textField.setBoost(10000);


        //把文档关联给所有写出器
        indexWriter.addDocument(document);

        indexWriter.commit();

        indexWriter.close();

    }

    // 批量创建索引
    @Test
    public void testBatchCreateIndex() throws Exception{
        // 创建文档的集合
        Collection<Document> docs = new ArrayList<>();
        // 创建文档对象
        Document document1 = new Document();
        document1.add(new StringField("id", "1", Field.Store.YES));
        document1.add(new TextField("title", "谷歌地图之父跳槽facebook", Field.Store.YES));
        docs.add(document1);
        // 创建文档对象
        Document document2 = new Document();
        document2.add(new StringField("id", "2", Field.Store.YES));
        document2.add(new TextField("title", "谷歌地图之父加盟FaceBook", Field.Store.YES));
        docs.add(document2);
        // 创建文档对象
        Document document3 = new Document();
        document3.add(new StringField("id", "3", Field.Store.YES));
        document3.add(new TextField("title", "谷歌地图创始人拉斯离开谷歌加盟Facebook", Field.Store.YES));
        docs.add(document3);
        // 创建文档对象
        Document document4 = new Document();
        document4.add(new StringField("id", "4", Field.Store.YES));
        document4.add(new TextField("title", "谷歌地图之父跳槽Facebook与Wave项目取消有关", Field.Store.YES));
        docs.add(document4);
        // 创建文档对象
        Document document5 = new Document();
        document5.add(new StringField("id", "5", Field.Store.YES));
        document5.add(new TextField("title", "谷歌地图之父拉斯加盟社交网站Facebook", Field.Store.YES));
        docs.add(document5);

        // 索引目录类,指定索引在硬盘中的位置
        Directory directory = FSDirectory.open(new File("indexDir"));
        // 引入IK分词器
        Analyzer analyzer = new IKAnalyzer();
        // 索引写出工具的配置对象
        IndexWriterConfig conf = new IndexWriterConfig(Version.LATEST, analyzer);
        // 设置打开方式：OpenMode.APPEND 会在索引库的基础上追加新索引。
        // OpenMode.CREATE会先清空原来数据，再提交新的索引
        conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        // 创建索引的写出工具类。参数：索引的目录和配置信息
        IndexWriter indexWriter = new IndexWriter(directory, conf);
        // 把文档集合交给IndexWriter
        indexWriter.addDocuments(docs);
        // 提交
        indexWriter.commit();
        // 关闭
        indexWriter.close();
    }

    // 批量创建索引
    @Test
    public void testBatchCreateIndexNumeric() throws Exception{
        // 创建文档的集合
        Collection<Document> docs = new ArrayList<>();
        // 创建文档对象
        Document document1 = new Document();
        document1.add(new LongField("id", 1L, Field.Store.YES));
        document1.add(new TextField("title", "谷歌地图之父跳槽facebook", Field.Store.YES));
        docs.add(document1);
        // 创建文档对象
        Document document2 = new Document();
        document2.add(new LongField("id", 2L, Field.Store.YES));
        document2.add(new TextField("title", "谷歌地图之父加盟FaceBook", Field.Store.YES));
        docs.add(document2);
        // 创建文档对象
        Document document3 = new Document();
        document3.add(new LongField("id", 3L, Field.Store.YES));
        document3.add(new TextField("title", "谷歌地图创始人拉斯离开谷歌加盟Facebook", Field.Store.YES));
        docs.add(document3);
        // 创建文档对象
        Document document4 = new Document();
        document4.add(new LongField("id", 4L, Field.Store.YES));
        document4.add(new TextField("title", "谷歌地图之父跳槽Facebook与Wave项目取消有关", Field.Store.YES));
        docs.add(document4);
        // 创建文档对象
        Document document5 = new Document();
        document5.add(new LongField("id", 5L, Field.Store.YES));
        document5.add(new TextField("title", "谷歌地图之父拉斯加盟社交网站Facebook", Field.Store.YES));
        docs.add(document5);

        // 索引目录类,指定索引在硬盘中的位置
        Directory directory = FSDirectory.open(new File("indexDir"));
        // 引入IK分词器
        Analyzer analyzer = new IKAnalyzer();
        // 索引写出工具的配置对象
        IndexWriterConfig conf = new IndexWriterConfig(Version.LATEST, analyzer);
        // 设置打开方式：OpenMode.APPEND 会在索引库的基础上追加新索引。
        // OpenMode.CREATE会先清空原来数据，再提交新的索引
        conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        // 创建索引的写出工具类。参数：索引的目录和配置信息
        IndexWriter indexWriter = new IndexWriter(directory, conf);
        // 把文档集合交给IndexWriter
        indexWriter.addDocuments(docs);
        // 提交
        indexWriter.commit();
        // 关闭
        indexWriter.close();
    }
}
