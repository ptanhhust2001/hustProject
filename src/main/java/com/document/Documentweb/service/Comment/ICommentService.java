package com.document.Documentweb.service.comment;

import com.document.Documentweb.dto.ResponsePageDTO;
import com.document.Documentweb.dto.comment.CommentReqDTO;
import com.document.Documentweb.dto.comment.CommentResDTO;
import com.document.Documentweb.dto.comment.CommentUpdateDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICommentService {
    ResponsePageDTO<List<CommentResDTO>> findAll(String advanceSearch, Pageable pageable);

    CommentResDTO findById(Long id);

    CommentResDTO create(CommentReqDTO dto);

    CommentResDTO update(Long id, CommentUpdateDTO dto);

    void deleteAllById(List<Long> id);
}
