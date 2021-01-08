# mysite_back
mysite网站后端接口，由springboot实现。访问地址：47.102.212.127（管理员测试账户：master,123456）

# quickStart
1 . 项目依赖于elasticsearch及ik分词器。
* 下载elasticsearch 6.8.3，解压：https://www.elastic.co/cn/downloads/past-releases/elasticsearch-6-8-3
* 下载ik分词器，并将其放到elasticsearch的plugin下：https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.8.3/elasticsearch-analysis-ik-6.8.3.zip  

2 .数据库使用mysql，在mysql新建一个数据库：mysite。（在项目中，已配置了自动建表）  
3 .运行项目。修改detail表的details字段为text类型（避免文本内容存不下）。  
4 .访问（查看项目所有接口）：http://localhost:9090/mysite/swagger-ui/index.html  
# elasticsearch
>####功能：用于网站内容的查询。使用ik分词器，对搜索结果进行高亮显示。

>####实现
1、 根据maven引入的依赖，官网下载对应的elasticsearch版本（6.8.3），解压——》下载Head插件——》es进行跨域配置——》下载ik分词器
，放到插件包下。——》测试head插件和ik分词器。  
2、springboot集成elasticsearch   
 * 引入依赖  
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
 </dependency>
``` 
* 编写ElasticSearch配置类。 
```
@Configuration
public class ElasticSearch {
    /当部署在linux上时，localhost要写为linux的ip地址
    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));
        return client;
    }
}
```
* 对Detail实体类进行相关注解：@document，@filed。  
* 接口DetailESDao继承ElasticsearchRepository，实现es基本操作。  
* 在启动类ApiApplication，指明dao和es对应包。  
* 在DetailService注入高级客户端RestHighLevelClient、detailESDao接口。  
* 前端查询调用DetailService的deleteAndGet方法，先将索引中的数据删除，再重新插入，避免es数据重复。  
* 高亮查询，通过DetailService的search方法实现：构建查询（注意使用matchQuery是实体配置的ik分词生效），构建高亮——》执行查询——》
解析结果（将原来结果的title进行高亮替换）。
```
        //1.构建查询
        SearchRequest searchRequest = new SearchRequest("mysite");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));


        //精准匹配
        //TermQueryBuilder termQueryBuilder=QueryBuilders.termQuery("title",searchKey);,不会进行分词查询
        //分词查询，需要用matchquery。
        MatchQueryBuilder matchQueryBuilder=QueryBuilders.matchQuery("title",searchKey);
        sourceBuilder.query(matchQueryBuilder);

        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");

        highlightBuilder.requireFieldMatch(false);//多高亮，设false
        highlightBuilder.preTags("<em style='color:red;font-style: normal;'>");
        highlightBuilder.postTags("</em>");
        sourceBuilder.highlighter(highlightBuilder);

        //2.执行搜索
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse=restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        //3.解析结果
       ArrayList<Map<String,Object>> list=new ArrayList<>();

        for (SearchHit hit : searchResponse.getHits().getHits()) {
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField title = highlightFields.get("title");

            //原来的结果
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();

            //替换字段
            if (title != null) {
                Text[] fragments = title.fragments();
                String newTitle = "";
                for (Text fragment : fragments) {
                    newTitle += fragment;
                }
                sourceAsMap.put("title", newTitle);
            }

            list.add(sourceAsMap);
        }
```
#swagger 3.0
>####功能：实时更新api接口文档

>####实现：
* 引入依赖  
```
<dependency>
      <groupId>io.springfox</groupId>
      <artifactId>springfox-boot-starter</artifactId>
      <version>3.0.0</version>
</dependency>
```
* 编写配置类Swagger
```
public class SwaggerConfig {

    @Bean
    public Docket docket(Environment environment){
        //设置swagger只在开发环境下使用

        Profiles profile= Profiles.of("dev");
        boolean judge=environment.acceptsProfiles(profile);

        return new Docket(DocumentationType.OAS_30).apiInfo(apiInfo())
                .enable(judge)  //判断是否是开发环境
                .groupName("青彦沐")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mysite.api.controller"))
                .build();
    }

    /*
    页面信息
     */
    private ApiInfo apiInfo(){
        Contact contact=new Contact("青彦沐","https://space.bilibili.com/100036156","1789204734@qq.com");
        return new ApiInfo(
                "mysite后端接口文档" ,
                "静以修身，俭以养德。",
                "v1.0",
                "https://space.bilibili.com/100036156",
                contact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList()
        );
    }
}
```
* 访问接口文档地址  
 swagger3访问地址：http://localhost:9090/mysite/swagger-ui/index.html  
 
 ## Docker部署项目到linux
 >### 修改配置
 * 让生产环境配置文件生效
```
#指定生效配置文件
spring.profiles.active=prod
```
* 修改elasticsearch配置类Elasticsearch，指定ip地址。
```
 new HttpHost("47.102.212.127", 9200, "http")));
```
* 使用maven打包测试，具体过程：https://blog.csdn.net/qq_33813721/article/details/112307821