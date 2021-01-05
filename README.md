# mysite_back
mysite网站后端接口，由springboot实现。

# quickStart

# elasticsearch
>###功能：用于网站内容的查询。使用ik分词器，对搜索结果进行高亮显示。
####实现：
1、 根据maven引入的依赖，官网下载对应的elasticsearch版本（6.8.3），解压——》下载Head插件——》es进行跨域配置——》下载ik分词器
，放到插件包下。——》测试head插件和ik分词器。  
2、springboot集成elasticsearch  
（1）编写ElasticSearch配置类。  
（2）对Detail实体类进行相关注解：@document，@filed。  
（3）接口DetailESDao继承ElasticsearchRepository，实现es基本操作。  
（4）在启动类ApiApplication，指明dao和es对应包。  
（5）在DetailService注入高级客户端RestHighLevelClient、detailESDao接口。  
（6）前端查询调用DetailService的deleteAndGet方法，先将索引中的数据删除，再重新插入，避免es数据重复。  
（7）高亮查询，通过DetailService的search方法实现：构建查询（注意使用matchQuery是实体配置的ik分词生效），构建高亮——》执行查询——》
解析结果（将原来结果的title进行高亮替换）。
