package no.iterate;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.type.VoidType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProgramPrinter {
    static String indent(int depth){
        return IntStream
                .range(0, depth + 1)
                .mapToObj(i -> "   ")
                .collect(Collectors.joining());
    }

    public static void printNodes(Program program, List<Node> nodes, int depth){
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);

            String label = "REPLACE ME!";

            if(node instanceof ClassOrInterfaceDeclaration){
                label = "class/interface: " + ((ClassOrInterfaceDeclaration) node).getName();
            }

            if(node instanceof SimpleName){
                label = "name: " + ((SimpleName) node).getIdentifier();
            }

            if(node instanceof MethodDeclaration){
                label = "method: " + ((MethodDeclaration)node).getName();
            }

            if(node instanceof BlockStmt){
                label = "block statement: ";
            }

            if(node instanceof VoidType){
                label = "void";
            }

            if(node instanceof Parameter){
                label = "parameter: " + ((Parameter)node).getName();
            }

            if(node instanceof TypeParameter){
                label = "type parameter: " + ((TypeParameter)node).getName();
            }

            System.out.print(indent(depth));
            System.out.print("" + i + ": ");
            System.out.println(label);

            printNodes(program, node.getChildNodes(), depth + 1);
        }
    }
}
