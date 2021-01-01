package com.mysite.api.service;

import com.alibaba.fastjson.JSONArray;
import com.mysite.api.dao.DetailDao;
import com.mysite.api.es.DetailESDao;
import com.mysite.api.pojo.Detail;
import com.mysite.api.pojo.MinFrame;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class DetailService {
    ///缓存，一般是放在service进行的。


    @Autowired
    DetailDao detailDao;

    @Autowired
    DetailESDao detailESDao;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;


    //高亮查询
    public List<Detail> search(String searchKey) throws Exception{

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

        //转为list<T>返回
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(list);
        List<Detail> returnList=jsonArray.toJavaList(Detail.class);


        Iterator<Detail> iterator = returnList.iterator();

        while(iterator.hasNext()){
            Detail o=iterator.next();
            if("解锁".equalsIgnoreCase(o.getMinFrame().getBlock().getFrame().getApp().getStatus())){
                iterator.remove();
            }
        }

        return returnList;
    }
    public void deleteAndGetAll(){
        //清空
        detailESDao.deleteAll();
        //获取
        initESData();
    };

    //初始化数据。
    public void initESData(){
        elasticsearchTemplate.putMapping(Detail.class);
            List<Detail> detailList=detailDao.findAll();
            for(Detail detail:detailList){
                detailESDao.save(detail);
        }
    }


    //新建detail

    public void  saveDetail(Detail detail){
        detailDao.save(detail);
        detailESDao.save(detail);
    }

    //查询。通过其他查询

    public List<Detail> findAllByMf(MinFrame minFrame){
        return  detailDao.findAllByMinFrame(minFrame);
    }


    public void deleteDetail(int id){
        detailDao.deleteById(id);
        detailESDao.deleteById(id);
    }


    public Detail findOneById(int id){
        return detailDao.getById(id);
    }
}
