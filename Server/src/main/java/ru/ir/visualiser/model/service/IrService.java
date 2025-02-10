package ru.ir.visualiser.model.service;


import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Map;

import ru.ir.visualiser.model.classes.Ir;
import ru.ir.visualiser.model.repository.IrRepository;
import ru.ir.visualiser.model.classes.ir.ModuleIR;

@Service
@RequiredArgsConstructor
public class IrService {

    private  final IrRepository irRepository;

    public Ir create(Ir ir) {
        return irRepository.save(ir);
    }

    @Nullable
    public Ir get(Long id) {
        return irRepository.findById(id).orElse(null);
    }

    @Transactional
    public void update(Ir ir) {
        irRepository.save(ir);
    }

    @Transactional
    public void deleteById(long id) {
        Ir ir = irRepository.findById(id).orElse(null);
        if (ir != null) {
            for (Ir child : ir.getChildren()) {
                deleteById(child.getId());
            }
            irRepository.delete(ir);
        }
    }
}