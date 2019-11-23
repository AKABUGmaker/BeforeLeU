package cn.itcast.es.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.activation.FileTypeMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "heima",type = "docs")//相当于生命索引库,以及类型的名称
public class Goods {
    @Id
    private Long id;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String title; //标题
    @Field(type = FieldType.Keyword)
    private String category;// 分类
    @Field(type = FieldType.Keyword)
    private String brand; // 品牌
    @Field(type = FieldType.Double)
    private Double price; // 价格

    @Field(type = FieldType.Keyword,index = false)
    private String images; // 图片地址

}
