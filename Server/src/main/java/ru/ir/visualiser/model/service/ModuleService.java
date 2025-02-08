package ru.ir.visualiser.model.service;

import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ir.visualiser.model.classes.Ir;
import ru.ir.visualiser.model.classes.ir.FunctionIR;
import ru.ir.visualiser.model.classes.ir.ModuleIR;
import ru.ir.visualiser.model.repository.BlockRepository;
import ru.ir.visualiser.model.repository.DotRepository;
import ru.ir.visualiser.model.repository.FunctionRepository;
import ru.ir.visualiser.model.repository.ModuleRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ModuleService {
    private final ModuleRepository moduleRepository;
    private final BlockRepository blockRepository;
    private final DotRepository dotRepository;
    private final FunctionRepository functionRepository;
    private final IrService irService;

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
        Ir ir = irService.get(irId);
        return moduleRepository.findByIr(ir).orElse(null);
    }

    @Transactional
    public void delete(Long irId) {
        Ir ir = irService.get(irId);
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
