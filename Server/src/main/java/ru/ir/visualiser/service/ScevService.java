package ru.ir.visualiser.service;

import java.io.IOException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import ru.ir.visualiser.model.Ir;
import ru.ir.visualiser.model.analysis.Scev;
import ru.ir.visualiser.repository.ScevRepository;
import ru.ir.visualiser.core.parser.Parser;
import ru.ir.visualiser.core.llvm.Opt;
import java.util.Optional;
import ru.ir.visualiser.config.Config;
import ru.ir.visualiser.model.ir.ModuleIR;
import ru.ir.visualiser.repository.ModuleRepository;

/**
 * Request info about scev analysis for the given ir.
 */
@Service
@RequiredArgsConstructor
public class ScevService {

    private final ScevRepository scevRepository;
    private final ModuleRepository moduleRepository;

    /**
     * Get cached Scev analysis for given IR or parse and cache it.
     *
     * @param ir - ID of IR to get Scev analysis for.
     * @param opt - Index of opt to use.
     *
     * @return Scev analysis for given IR.
     */
    public Scev get(Ir ir, int opt) throws IOException {
        Optional<Scev> scev = scevRepository.findByIr(ir);
        if (scev.isPresent()) {
            return scev.get();
        }

        // TODO: Potential index out of bounds 
        String optPath = Config.getInstance().getOptsPath()[opt];
        String input = Opt.printScev(optPath, ir);
        Optional<ModuleIR> module = moduleRepository.findByIr(ir);

        if (module.isEmpty()) {
            return null;
        }

        Scev parsedScev = Parser.parseScev(input, module.get());
        scevRepository.save(parsedScev);

        return parsedScev;
    }
}
