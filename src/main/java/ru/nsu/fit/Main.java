package ru.nsu.fit;

import ru.nsu.fit.data.node.*;
import ru.nsu.fit.data.*;
import ru.nsu.fit.path.Context;
import ru.nsu.fit.path.Path;
import ru.nsu.fit.schema.node.SchemaNode;
import ru.nsu.fit.schema.parser.SchemaReader;
import ru.nsu.fit.schema.validator.SchemaValidator;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import static ru.nsu.fit.data.Util.showTheData;

public class Main {

    public static void main(String[] args) throws Exception {
        Node dataNode = DataReader.parseData(new FileReader("src/main/resources/data2_construct.txt"));
        SchemaNode schemaNode = SchemaReader.parseSchema(new FileReader("src/main/resources/data2_construct_schema.txt"));

        boolean isValid = SchemaValidator.validate(dataNode, schemaNode);
        System.out.println("Data validation result: " + isValid);
        System.out.println(showTheData(dataNode, ""));


        Path path = Path.compile("//constructor/rect[@color=\"blue\"]");
        Context context = new Context(dataNode);
        Collection<Node> res = path.evaluate(context);
        int n = 0;
        for (Node i : res) {
            ++n;
            System.out.println(n + ": ");
            System.out.println(showTheData(i, "   "));
        }


    }
}
