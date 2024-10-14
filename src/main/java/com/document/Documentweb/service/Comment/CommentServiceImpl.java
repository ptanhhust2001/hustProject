package com.document.Documentweb.service.comment;

import com.document.Documentweb.constrant.ErrorCommon;
import com.document.Documentweb.constrant.FunctionError;
import com.document.Documentweb.dto.ResponsePageDTO;
import com.document.Documentweb.dto.comment.CommentResDTO;
import com.document.Documentweb.entity.Comment;
import com.document.Documentweb.exception.BookException;
import com.document.Documentweb.repository.CommentRepository;
import com.document.Documentweb.utils.spec.BaseSpecs;
import com.document.Documentweb.utils.spec.Utils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements ICommentService {
    CommentRepository repository;
    ModelMapper modelMapper;

    public ResponsePageDTO<List<CommentResDTO>> findAll(String advanceSearch , Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("updateDate").descending());
        Page<Comment> page = repository.findAll(BaseSpecs.searchQuery(advanceSearch), pageable);
        List<CommentResDTO> dtoList = Utils.mapList(modelMapper, page.getContent()  ,CommentResDTO.class);
        return ResponsePageDTO.success(dtoList, page.getTotalElements());
    }

    public CommentResDTO findById(Long id) {
        Comment data = repository.findById(id).orElseThrow(()
                -> new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.COMMENT_NOT_FOUND, List.of(id))));
        return modelMapper.map(data, CommentResDTO.class);
    }

    public CommentResDTO create(CommentResDTO dto) {
        Comment data = modelMapper.map(dto, Comment.class);
        //thieu
        repository.save(data);
        return modelMapper.map(repository.save(data), CommentResDTO.class);
    }

    public CommentResDTO update(Long id, CommentResDTO dto) {
        Comment data = repository.findById(id).orElseThrow(()
                -> new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.COMMENT_NOT_FOUND, List.of(id))));
        data = modelMapper.map(dto, Comment.class);
        repository.save(data);
        return modelMapper.map(data, CommentResDTO.class);
    }


    public void deleteAllById(List<Long> id) {
        repository.deleteAllById(id);
    }
}