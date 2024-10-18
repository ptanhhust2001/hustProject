package com.document.Documentweb.modelmapper;

import com.document.Documentweb.dto.User.UserReqDTO;
import com.document.Documentweb.dto.User.UserResDTO;
import com.document.Documentweb.dto.User.UserUpdateDTO;
import com.document.Documentweb.dto.comment.CommentReqDTO;
import com.document.Documentweb.dto.comment.CommentResDTO;
import com.document.Documentweb.dto.comment.CommentUpdateDTO;
import com.document.Documentweb.entity.Comment;
import com.document.Documentweb.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommentConfigMapper {
    @Bean
    public ModelMapper commentMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        PropertyMap<?,?> map1 = new PropertyMap<Comment, CommentResDTO>(){
            protected void configure() {
            }
        };
        PropertyMap<?,?> map2 = new PropertyMap<CommentReqDTO, Comment>(){
            protected void configure() {
                skip(destination.getId());
            }
        };
        PropertyMap<?,?> map3 = new PropertyMap<CommentUpdateDTO, Comment>(){
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        };
        mapper.addMappings(map1);
        mapper.addMappings(map2);
        mapper.addMappings(map3);
        return mapper;
    }
}
