package com.mysite.api.es;

import com.mysite.api.pojo.Detail;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DetailESDao extends ElasticsearchRepository<Detail,Integer> {

}
