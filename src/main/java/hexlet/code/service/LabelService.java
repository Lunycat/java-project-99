package hexlet.code.service;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.dto.label.LabelUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LabelService {

    private final LabelRepository repository;

    private final LabelMapper mapper;

    public List<LabelDTO> findAll() {
        List<Label> labels = repository.findAll();
        return mapper.toListLabelDTO(labels);
    }

    public LabelDTO findById(Long id) {
        Label label = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found label with id = " + id));
        return mapper.toLabelDTO(label);
    }

    public LabelDTO save(LabelCreateDTO dto) {
        Label label = mapper.toLabel(dto);
        repository.save(label);
        return mapper.toLabelDTO(label);
    }

    public LabelDTO update(LabelUpdateDTO dto, Long id) {
        Label label = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found label with id = " + id));
        mapper.update(dto, label);
        repository.save(label);
        return mapper.toLabelDTO(label);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
