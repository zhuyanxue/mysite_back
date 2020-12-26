package com.mysite.api.service;

import com.mysite.api.dao.DetailDao;
import com.mysite.api.es.DetailESDao;
import com.mysite.api.pojo.Detail;
import com.mysite.api.pojo.MinFrame;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
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


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class DetailService {
    ///缓存，一般是放在service进行的。


    @Autowired
    DetailDao detailDao;

    @Autowired
    DetailESDao detailESDao;

    //查询方法
    public List<Detail> search(String searchKey){
        //detailESDao.deleteAll();
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(QueryBuilders.matchQuery("details", searchKey));
        //Sort.Order order=new Sort.Order(Sort.Direction.DESC, "id");
        //Pageable pageable =PageRequest.of(0,20,Sort.by(order));
        //queryBuilder.withPageable(pageable);
        Page<Detail> page=detailESDao.search(queryBuilder.build());


        List<Detail> returnList= new ArrayList<>();
        returnList.addAll(page.getContent());
        //List<Detail> detailList=page.getContent();
        //List<Detail> endList=new ArrayList(Arrays.asList(detailList));
        //排除已经锁定
       /* for(Detail detail:returnList){
            if(detail.getMinFrame().getBlock().getFrame().getApp().getStatus().equals("解锁")){
                returnList.remove(detail);
            }
        };*/
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
