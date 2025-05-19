package ru.ir.visualiser.service;


import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import ru.ir.visualiser.files.FileWorker;
import ru.ir.visualiser.model.Ir;
import ru.ir.visualiser.repository.IrRepository;

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

    /** deleting files.
     *
     * @param id id of ir
     * @throws IOException error
     */
    @Transactional
    public void deleteById(long id) throws IOException {
        Ir ir = irRepository.findById(id).orElse(null);
        if (ir != null) {
            for (Ir child : ir.getChildren()) {
                deleteById(child.getId());
            }
            FileWorker.deleteDirectory(ir.getDotPath());
            FileWorker.deleteDirectory(ir.getSvgPath());
            FileWorker.deleteDirectory(ir.getIrPath());
            irRepository.delete(ir);
        }
    }
}