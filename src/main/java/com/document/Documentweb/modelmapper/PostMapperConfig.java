package com.document.Documentweb.modelmapper;

import com.document.Documentweb.dto.post.PostReqDTO;
import com.document.Documentweb.dto.post.PostResDTO;
import com.document.Documentweb.dto.post.PostUpdateDTO;
import com.document.Documentweb.entity.Post;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostMapperConfig {
    @Bean
    public ModelMapper postMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        PropertyMap<?,?> map1 = new PropertyMap<Post, PostResDTO>(){
            protected void configure() {
            }
        };
        PropertyMap<?,?> map2 = new PropertyMap<PostReqDTO, Post>(){
            protected void configure() {
                skip(destination.getId());
            }
        };
        PropertyMap<?,?> map3 = new PropertyMap<PostUpdateDTO, Post>(){
            @Override
            protected void configure() {
                skip(destination.getId());
                skip(destination.getCreateAt());
                skip(destination.getUpdateAt());
                skip(destination.getCreateBy());
                skip(destination.getUpdateBy());
            }
        };
        mapper.addMappings(map1);
        mapper.addMappings(map2);
        return mapper;
    }
}
