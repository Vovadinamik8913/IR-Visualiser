package ru.ir.visualiser.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.ir.visualiser.model.Ir;
import ru.ir.visualiser.model.analysis.Scev;
import ru.ir.visualiser.repository.ScevRepository;
import ru.ir.visualiser.core.parser.Parser;
import ru.ir.visualiser.core.llvm.Opt;
import ru.ir.visualiser.config.LocalConfig;
import ru.ir.visualiser.model.analysis.Memoryssa;
import ru.ir.visualiser.model.ir.ModuleIR;
import ru.ir.visualiser.repository.ModuleRepository;
import ru.ir.visualiser.repository.MemoryssaRepository;

/**
 * Request info about analysis for the given ir.
 */
@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final ScevRepository scevRepository;
    private final MemoryssaRepository memoryssaRepository;
    private final ModuleRepository moduleRepository;
    private final LocalConfig localConfig;

    /**
     * Get cached Scev analysis for given IR or parse and cache it.
     *
     * @param ir - ID of IR to get Scev analysis for.
     *
     * @return Scev analysis for given IR.
     */
    public Scev getScev(Ir ir) throws IOException {
        Optional<Scev> scev = scevRepository.findByIr(ir);
        if (scev.isPresent()) {
            return scev.get();
        }

        // TODO: Potential index out of bounds 
        String optPath = localConfig.getOptPath();
        String input = Opt.printScev(optPath, ir);
        Optional<ModuleIR> module = moduleRepository.findByIr(ir);

        if (module.isEmpty()) {
            return null;
        }

        Scev parsedScev = Parser.parseScev(input, module.get());
        scevRepository.save(parsedScev);

        return parsedScev;
    }

    /**
     * Get cached Memoryssa analysis for given IR or parse and cache it.
     *
     * @param ir - ID of IR to get Memoryssa analysis for.
     *
     * @return Memoryssa analysis for given IR.
     */    
    public Memoryssa getMemoryssa(Ir ir) throws IOException {
        Optional<Memoryssa> memoryssa = memoryssaRepository.findByIr(ir);
        if (memoryssa.isPresent()) {
            return memoryssa.get();
        }

        String optPath = localConfig.getOptPath();
        String input = Opt.printMemoryssa(optPath, ir);
        Optional<ModuleIR> module = moduleRepository.findByIr(ir);

        if (module.isEmpty()) {
            return null;
        }

        Memoryssa parsedMemoryssa = Parser.parseMemoryssa(input, module.get());
        memoryssaRepository.save(parsedMemoryssa);

        return parsedMemoryssa;
    }
}
