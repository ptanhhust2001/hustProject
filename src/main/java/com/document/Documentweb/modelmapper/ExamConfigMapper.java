package com.document.Documentweb.modelmapper;

import com.document.Documentweb.dto.exam.ExamReqDTO;
import com.document.Documentweb.dto.exam.ExamResDTO;
import com.document.Documentweb.dto.exam.ExamUpdateDTO;
import com.document.Documentweb.entity.Exam;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExamConfigMapper {
    @Bean
    public ModelMapper examMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        PropertyMap<?,?> map1 = new PropertyMap<Exam, ExamResDTO>(){
            protected void configure() {
            }
        };
        PropertyMap<?,?> map2 = new PropertyMap<ExamReqDTO, Exam>(){
            protected void configure() {
                skip(destination.getId());
            }
        };
        PropertyMap<?,?> map3 = new PropertyMap<ExamUpdateDTO, Exam>(){
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
