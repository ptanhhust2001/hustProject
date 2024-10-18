package com.document.Documentweb.service.post;

import com.document.Documentweb.dto.post.PostReqDTO;
import com.document.Documentweb.dto.post.PostResDTO;
import com.document.Documentweb.dto.post.PostUpdateDTO;

import java.util.List;

public interface IPostService {
    List<PostResDTO> findAll();

    PostResDTO findById(Long id);

    PostResDTO create(PostReqDTO dto);


    PostResDTO update(Long id, PostUpdateDTO dto);

    void deleteAllById(List<Long> ids);
}
