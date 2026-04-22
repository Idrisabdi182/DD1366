public class Interpreter {
    public Leona leona;
    public boolean penDown = false;
    public double v = 0.0; 


    public Interpreter(Leona leona) {
        this.leona = leona;
    }

    public void execute(ParseTree node) {
        String name = node.name;

        switch (name) {
            case "CommandList":
                for (ParseTree child : node.children) {
                    execute(child);
                }
                break;
            case "Command":
                execute(node.children.get(0));
                break;
            case "Move":
                String direction = node.children.get(0).name;
                int distance = Integer.parseInt(node.children.get(1).name);

                if (direction.equals("FORW")) {
                    move(distance);
                } else {
                    move(-distance);
                }
                break;
            case "Turn":
                String turn = node.children.get(0).name;
                int angle = Integer.parseInt(node.children.get(1).name);
                if (turn.equals("LEFT")) {
                    v += angle;
                } else {
                     v -= angle;
                }
                break;
            case "Pen":
                String write = node.children.get(0).name;
                if (write.equals("UP")) {
                    penDown = false;
                } else {
                    penDown = true;
                }
                break;
            case "Color":
                String hex = node.children.get(0).name;
                leona.c = hex;
                break;
            case "Rep":
                int reps = Integer.parseInt(node.children.get(0).name);
                ParseTree insrep = node.children.get(1);
                for (int i = 0; i < reps; i++) {
                    execute(insrep);
                }
                break;
        }

    }

    public void move(int dist) {
        // convert to radians for sin & cos
        if (penDown) {
            leona.setNewX2Y2(dist, v);
            leona.printLeona();
        }
        leona.setNewX1Y1();
    }
}
