package ru.nsu.irvisualizer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MainPage {
    @RequestMapping("/main")
    public String main() {
        try {
            Opt opt = new Opt("/home/vilmo/Tools/LLVM/bin/opt");
            if (opt.generateDotFile("/home/vilmo/Documents/lrn/main.ll", "/home/vilmo/Documents/lrn/main.dot")) {
                return "Generated successfully";
            }
            return "Opt process couldn't generate file";
        } catch (IOException | IllegalArgumentException e) {
            return "Error when generating: " + e.getMessage();
        }
    }
}
