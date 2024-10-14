package com.document.Documentweb.service.post;

import com.document.Documentweb.constrant.ErrorCommon;
import com.document.Documentweb.constrant.FunctionError;
import com.document.Documentweb.dto.ResponsePageDTO;
import com.document.Documentweb.dto.post.PostReqDTO;
import com.document.Documentweb.dto.post.PostResDTO;
import com.document.Documentweb.entity.Post;
import com.document.Documentweb.exception.BookException;
import com.document.Documentweb.repository.PostRepository;
import com.document.Documentweb.utils.spec.BaseSpecs;
import com.document.Documentweb.utils.spec.Utils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements IPostService{
    PostRepository repository;
    ModelMapper mapper;

    public List<PostResDTO> findAll() {
        return repository.findAll().stream().map(post -> mapper.map(post, PostResDTO.class)).toList();
    }

    public ResponsePageDTO<List<PostResDTO>> findAllByPage(String advanceSearch, Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("updateDate").descending());
        Page<Post> page = repository.findAll(BaseSpecs.searchQuery(advanceSearch), pageable);
        List<PostResDTO> dtos = Utils.mapList(mapper, page.getContent(), PostResDTO.class);
        return ResponsePageDTO.success(dtos, page.getTotalElements());
    }

    public PostResDTO findById(Long id) {
        Post data = repository.findById(id)
                .orElseThrow(()
                        -> new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.POST_NOT_FOUND, List.of(id))));
        return mapper.map(data, PostResDTO.class);
    }

    public PostResDTO create(PostReqDTO dto) {
        Post data = mapper.map(dto, Post.class);
        repository.save(data);
        return mapper.map(data, PostResDTO.class);
    }


    public PostResDTO update(Long id, PostReqDTO dto) {
        if (repository.findById(id).isEmpty()) throw new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.POST_NOT_FOUND, List.of(id)));
        Post data = mapper.map(dto, Post.class);
        data.setId(id);
        repository.save(data);
        return mapper.map(data, PostResDTO.class);
    }

    public void deleteAllById(List<Long> ids) {
        List<Long> notFound = ids.stream().filter(id -> repository.findById(id).isEmpty()).toList();
        if (!notFound.isEmpty())
            throw new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.POST_NOT_FOUND, notFound));
        repository.deleteAllByIdInBatch(ids);
    }
}