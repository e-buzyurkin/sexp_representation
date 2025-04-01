package ru.nsu.fit.demo;

import ru.nsu.fit.data.DataReader;
import ru.nsu.fit.data.node.Node;
import ru.nsu.fit.path.Context;
import ru.nsu.fit.path.Path;
import ru.nsu.fit.schema.node.SchemaNode;
import ru.nsu.fit.schema.parser.SchemaReader;
import ru.nsu.fit.schema.validator.SchemaValidator;

import java.io.FileReader;
import java.util.Collection;

import static ru.nsu.fit.data.Util.showTheData;

public class DataPeople {
    public static void main(String[] args) throws Exception {
        Node dataNode = DataReader.parseData(new FileReader("src/main/resources/data3_people.txt"));
        SchemaNode schemaNode = SchemaReader.parseSchema(new FileReader("src/main/resources/data3_people_schema.txt"));

        boolean isValid = SchemaValidator.validate(dataNode, schemaNode);
        System.out.println("Data validation result: " + isValid);


        select(dataNode, "//people/group[@gender=\"female\"]");
    }

    private static void select(Node dataNode, String query) throws Exception {
        Path path = Path.compile(query);
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
