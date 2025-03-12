package ru.nsu.fit.data;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import ru.nsu.fit.DataLexer;
import ru.nsu.fit.DataParser;
import ru.nsu.fit.data.node.Node;

import java.io.IOException;
import java.io.Reader;

public class DataReader {
    public static Node parseData(Reader reader) throws IOException {
        DataLexer lexer = new DataLexer(CharStreams.fromReader(reader));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DataParser parser = new DataParser(tokens);
        ParseTree tree = parser.calc();
        ParseTreeWalker walker = new ParseTreeWalker();
        DataWalker dataWalker = new DataWalker();
        walker.walk(dataWalker, tree);
        return dataWalker.getDataNode();
    }
}
