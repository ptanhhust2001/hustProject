package com.document.Documentweb.modelmapper;

import com.document.Documentweb.dto.question.QuestionReqDTO;
import com.document.Documentweb.dto.question.QuestionResDTO;
import com.document.Documentweb.dto.question.QuestionUpdateDTO;
import com.document.Documentweb.entity.Question;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuestionConfigMapper {
    @Bean
    public ModelMapper questionMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        PropertyMap<?,?> map1 = new PropertyMap<Question, QuestionResDTO>(){
            protected void configure() {
            }
        };
        PropertyMap<?,?> map2 = new PropertyMap<QuestionReqDTO, Question>(){
            protected void configure() {
                skip(destination.getId());
            }
        };
        PropertyMap<?,?> map3 = new PropertyMap<QuestionUpdateDTO, Question>(){
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
