package ru.ir.visualiser.service;

import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ir.visualiser.model.Ir;
import ru.ir.visualiser.model.ir.FunctionIR;
import ru.ir.visualiser.model.ir.ModuleIR;
import ru.ir.visualiser.repository.*;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ModuleService {
    private final ModuleRepository moduleRepository;
    private final BlockRepository blockRepository;
    private final DotRepository dotRepository;
    private final FunctionRepository functionRepository;
    private final IrRepository irRepository;

    public ModuleIR create(ModuleIR module) {
        Collection<FunctionIR> functions = module.getFunctions();
        for (FunctionIR function : functions) {
            blockRepository.saveAll(function.getBlocks());
        }
        dotRepository.saveAll(module.getDots().values());
        functionRepository.saveAll(functions);
        return moduleRepository.save(module);
    }

    @Nullable
    public ModuleIR find(Long irId) {
        Ir ir = irRepository.findById(irId).orElse(null);
        return moduleRepository.findByIr(ir).orElse(null);
    }

    @Transactional
    public void delete(Long irId) {
        Ir ir = irRepository.findById(irId).orElse(null);
        ModuleIR module = moduleRepository.findByIr(ir).orElse(null);
        Collection<FunctionIR> functions = module.getFunctions();
        for (FunctionIR function : functions) {
            blockRepository.deleteAll(function.getBlocks());
        }
        dotRepository.deleteAll(module.getDots().values());
        functionRepository.deleteAll(functions);
        moduleRepository.delete(module);
    }
}
