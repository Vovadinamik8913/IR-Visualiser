package ru.ir.visualiser.parser;

import org.junit.jupiter.api.Test;
import ru.ir.visualiser.logic.parser.Parser;
import ru.ir.visualiser.model.classes.ir.Dot;
import ru.ir.visualiser.model.classes.ir.FunctionIR;
import ru.ir.visualiser.model.classes.ir.ModuleIR;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {

    @Test
    public void checkReadFromFile() throws URISyntaxException, IOException {
        URI path = getClass().getClassLoader().getResource("test1/test.ll").toURI();
        String content = Files.readString(Path.of(path));

        List<String> dotFiles = List.of(
                "test1/.add.dot",
                "test1/.add_my_arr.dot",
                "test1/.in_order_traversal.dot",
                "test1/.main.dot",
                "test1/.max.dot",
                "test1/.rebalance.dot",
                "test1/.rotation_left.dot",
                "test1/.rotation_right.dot",
                "test1/.shuffle.dot",
                "test1/.swap.dot",
                "test1/.update_height.dot"
        );
        List<String> dotContents = new java.util.ArrayList<>(dotFiles.size());
        for (String dotFile : dotFiles) {
            URI dotPath = getClass().getClassLoader().getResource(dotFile).toURI();
            String dotContent = Files.readString(Path.of(dotPath));
            dotContents.add(dotContent);
        }

        ModuleIR moduleIR = Parser.parseModule(content, dotContents);
        Collection<FunctionIR> functions = moduleIR.getFunctions();
        Collection<Dot> dots = moduleIR.getDots();
        FunctionIR function = functions.iterator().next();

        assertEquals(21, functions.size());
        assertEquals(144, function.getStartLine());
        assertEquals(167,function.getEndLine());
        assertEquals(dots.size(), 11);
    }

    @Test
    public void dotTest() throws URISyntaxException, IOException {
        URI path = getClass().getClassLoader().getResource("dot/test1.dot").toURI();
        String content = Files.readString(Path.of(path));
        Dot dot = Parser.parseDot(content);
        assertEquals("Node0x55cdef5b4ad0", dot.getSvgIdByLabel("2"));
        assertEquals("Node0x55cdef5b5320", dot.getSvgIdByLabel("14"));
        assertEquals("Node0x55cdef5b5380", dot.getSvgIdByLabel("20"));
        assertEquals("Node0x55cdef5b5900", dot.getSvgIdByLabel("21"));
        assertEquals("Node0x55cdef5b5d60", dot.getSvgIdByLabel("28"));
        assertEquals("Node0x55cdef5b5dc0", dot.getSvgIdByLabel("32"));
        assertEquals("Node0x55cdef5b5830", dot.getSvgIdByLabel("44"));
    }
}
