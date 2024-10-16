package com.document.Documentweb.modelmapper;

import com.document.Documentweb.dto.classenity.ClassReqDTO;
import com.document.Documentweb.dto.classenity.ClassResDTO;
import com.document.Documentweb.entity.ClassEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClassConfigMapper {

    @Bean
    public ModelMapper classMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        PropertyMap<?,?> map1 = new PropertyMap<ClassEntity, ClassResDTO>(){
            @Override
            protected void configure() {
            }
        };
        PropertyMap<?,?> map2 = new PropertyMap<ClassReqDTO, ClassEntity>(){
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        };
        mapper.addMappings(map1);
        mapper.addMappings(map2);
        return mapper;
    }
}
