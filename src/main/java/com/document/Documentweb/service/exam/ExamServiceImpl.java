package com.document.Documentweb.service.exam;

import com.document.Documentweb.constrant.ErrorCommon;
import com.document.Documentweb.constrant.FunctionError;
import com.document.Documentweb.dto.exam.ExamReqDTO;
import com.document.Documentweb.dto.exam.ExamResDTO;
import com.document.Documentweb.dto.exam.ExamUpdateDTO;
import com.document.Documentweb.entity.*;
import com.document.Documentweb.exception.BookException;
import com.document.Documentweb.repository.ClassEntityRepository;
import com.document.Documentweb.repository.ExamRepository;
import com.document.Documentweb.repository.SubjectRepository;
import com.document.Documentweb.repository.UserRepository;
import com.document.Documentweb.utils.spec.BaseSpecs;
import com.document.Documentweb.utils.spec.Utils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExamServiceImpl implements IExamService{
    ExamRepository repository;
    ModelMapper examMapper;
    ClassEntityRepository classEntityRepository;
    UserRepository userRepository;
    SubjectRepository subjectRepository;

    @Override
    public List<ExamResDTO> findAll(String advanceSearch) {
        List<Exam> datas = repository.findAll(BaseSpecs.searchQuery(advanceSearch));
        return Utils.mapList(examMapper, datas, ExamResDTO.class);
    }

    @Override
    public ExamResDTO findById(Long id) {
        Exam data = repository.findById(id).orElseThrow(
                () -> new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.EXAM_NOT_FOUND, List.of(id))));
        return examMapper.map(data, ExamResDTO.class);
    }

    @Override
    public ExamResDTO create(ExamReqDTO dto) {
        Map<Object, Object> errorMap = new HashMap<>();
        Exam data = validation(dto, errorMap);

        save(data, true);

        return examMapper.map(data, ExamResDTO.class);
    }

    @Override
    public ExamResDTO update(Long id, ExamUpdateDTO dto) {
        Exam data = repository.findById(id).orElseThrow(
                () -> new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.EXAM_NOT_FOUND, List.of(id))));
        examMapper.map(dto, data);

        save(data, false);
        return examMapper.map(data, ExamResDTO.class);
    }

    @Override
    public void deleteAllById(List<Long> ids) {
        List<Long> notFound = ids.stream().filter(id -> repository.findById(id).isEmpty()).toList();
        if (!notFound.isEmpty()) throw new BookException(FunctionError.DELETE_FAILSE, Map.of(ErrorCommon.EXAM_NOT_FOUND, notFound));
        repository.deleteAllById(ids);
    }

    private void save(Exam data, boolean isCreate) {
        if (isCreate) {
            data.setCreateBy(Utils.getCurrentUser());
            data.setCreateAt(LocalDateTime.now());
        }
        data.setUpdateBy(Utils.getCurrentUser());
        data.setUpdateAt(LocalDateTime.now());
        repository.save(data);
    }

    @Override
    public void upload(MultipartFile file, ExamReqDTO dto) {
        Map<Object, Object> errorMap = new HashMap<>();
        Exam data = validation(dto, errorMap);
        List<List<String>> listQuestions = new ArrayList<>();
        if (!file.getOriginalFilename().endsWith(".docx")) {
            throw new BookException("FILE_INCORRECT");
        }

        // Đọc nội dung file Word
        try (InputStream inputStream = file.getInputStream()) {
            XWPFDocument document = new XWPFDocument(inputStream);
            StringBuilder content = new StringBuilder();

            List<String> queue = new ArrayList<>();

            document.getParagraphs().forEach(paragraph -> {
                if (paragraph.getText().isBlank()) {
                    List<String> temp = new ArrayList<>(queue);
                    listQuestions.add(temp);
                    queue.clear();
                }
                queue.add(paragraph.getText());

                content.append(paragraph.getText()).append("\n");
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Question> questions  = new ArrayList<>();
        listQuestions.forEach(question -> {
            if (!question.isEmpty() && question.size() >= 4) {
                Question dataQuestion = Question.builder()
                        .question(question.get(0))
                        .firstAnswer(question.get(1))
                        .secondAnswer(question.get(2))
                        .thirdAnswer(question.get(3))
                        .fourthAnswer(question.get(4) == null ? null : question.get(4))
                        .build();
                int correctAns = 0;
                boolean isCorrect = false;
                for (String i : question) {
                    if (i.charAt(0) == '*') {
                        correctAns ++;
                        isCorrect = true;
                        break;
                    } else {
                        correctAns += 1;
                    }
                }
                if (isCorrect) errorMap.computeIfAbsent(ErrorCommon.QUESTION_DOES_NOT_ANSWER, k -> new ArrayList<>().add(question.get(0)));
                switch (correctAns) {
                    case 1 :
                        dataQuestion.setCorrectAnswer("A");
                        break;
                    case 2 :
                        dataQuestion.setCorrectAnswer("B");
                        break;
                    case 3 :
                        dataQuestion.setCorrectAnswer("C");
                        break;
                    case 4 :
                        dataQuestion.setCorrectAnswer("D");
                        break;
                    default:
                        break;
                }
                questions.add(dataQuestion);
            }
        });

        data.setQuestions(questions);
        save(data, true);
    }

    private Exam validation(ExamReqDTO dto, Map<Object, Object> errorMap) {
        Exam data = examMapper.map(dto, Exam.class);
        Optional<ClassEntity> classOpt = classEntityRepository.findById(dto.getClassEntityId());
        Optional<Subject> subjectOpt = subjectRepository.findById(dto.getSubjectId());
        Optional<User> userOpt = userRepository.findById(dto.getUserId());

        if (classOpt.isEmpty()) errorMap.put(ErrorCommon.CLASS_DOES_NOT_EXIST, List.of(dto.getClassEntityId()));
        if (subjectOpt.isEmpty()) errorMap.put(ErrorCommon.SUBJECT_DOES_NOT_EXIST, List.of(dto.getSubjectId()));
        if (userOpt.isEmpty()) errorMap.put(ErrorCommon.USER_DOES_NOT_EXIST, List.of(dto.getUserId()));

        if (!errorMap.isEmpty()) throw new BookException(FunctionError.CREATE_FAILED, errorMap);

        data.setClassEntity(classOpt.get()  );
        data.setSubject(subjectOpt.get());
        data.setUser(userOpt.get());
        return data;
    }

}
