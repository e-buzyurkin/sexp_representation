package ru.nsu.fit.schema.parser;

import ru.nsu.fit.data.DataReader;
import ru.nsu.fit.data.node.Node;
import ru.nsu.fit.schema.node.SchemaNode;

import java.io.Reader;

public class SchemaReader {
    public static SchemaNode parseSchema(Reader reader) throws Exception {
        Node node = DataReader.parseData(reader);
        return DataToSchemeTranslator.translate(node);
    }
}