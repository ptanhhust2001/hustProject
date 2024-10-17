package com.document.Documentweb.service.comment;

import com.document.Documentweb.constrant.ErrorCommon;
import com.document.Documentweb.constrant.FunctionError;
import com.document.Documentweb.dto.ResponsePageDTO;
import com.document.Documentweb.dto.comment.CommentReqDTO;
import com.document.Documentweb.dto.comment.CommentResDTO;
import com.document.Documentweb.entity.Comment;
import com.document.Documentweb.entity.Post;
import com.document.Documentweb.exception.BookException;
import com.document.Documentweb.repository.CommentRepository;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements ICommentService {
    CommentRepository repository;
    PostRepository postRepository;
    ModelMapper mapper;

    @Override
    public ResponsePageDTO<List<CommentResDTO>> findAll(String advanceSearch, Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("updateAt").descending());
        Page<Comment> page = repository.findAll(BaseSpecs.searchQuery(advanceSearch), pageable);
        List<CommentResDTO> dtoList = Utils.mapList(mapper, page.getContent()  ,CommentResDTO.class);
        return ResponsePageDTO.success(dtoList, page.getTotalElements());
    }

    @Override
    public CommentResDTO findById(Long id) {
        Comment data = repository.findById(id).orElseThrow(()
                -> new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.COMMENT_NOT_FOUND, List.of(id))));
        return mapper.map(data, CommentResDTO.class);
    }

    @Override
    public CommentResDTO create(CommentReqDTO dto) {
        Comment data = mapper.map(dto, Comment.class);
        //thieu
        Optional<Post> postOpt = postRepository.findById(dto.getPostId());
        if (postOpt.isEmpty()) throw new BookException(FunctionError.CREATE_FAILED, Map.of(ErrorCommon.POST_DOES_NOT_EXIST, dto.getPostId()));
        data.setPost(postOpt.get());
        save(data, true);
        return mapper.map(repository.save(data), CommentResDTO.class);
    }

    @Override
    public CommentResDTO update(Long id, CommentReqDTO dto) {
        Comment data = repository.findById(id).orElseThrow(()
                -> new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.COMMENT_NOT_FOUND, List.of(id))));
        data = mapper.map(dto, Comment.class);
        Optional<Post> postOpt = postRepository.findById(dto.getPostId());
        if (postOpt.isEmpty()) throw new BookException(FunctionError.CREATE_FAILED, Map.of(ErrorCommon.POST_DOES_NOT_EXIST, dto.getPostId()));
        data.setPost(postOpt.get());

        save(data, false);
        return mapper.map(data, CommentResDTO.class);
    }

    @Override
    public void deleteAllById(List<Long> id) {
        repository.deleteAllById(id);
    }

    private void save(Comment data, boolean isCreate) {
        if (isCreate) {
            data.setCreateAt(LocalDateTime.now());
            data.setCreateBy(Utils.getCurrentUser());
        }
        data.setUpdateAt(LocalDateTime.now());
        data.setUpdateBy(Utils.getCurrentUser());
        repository.save(data);
    }
}