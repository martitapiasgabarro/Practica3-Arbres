import java.util.ArrayList;

public class Students {
    private Node head;

    private class Node {
        BinaryTree studentTree;
        Node next;

        public Node(BinaryTree studentTree) {
            this.studentTree = studentTree;
            this.next = null;
        }
    }

    // Constructor que inicialitza una llista buida
    public Students() {
        this.head = null;
    }

    // Afegeix un nou estudiant de manera ordenada alfabèticament pel nom
    public void addStudent(BinaryTree newStudent) {
        String newStudentName = newStudent.getName();
        Node newNode = new Node(newStudent);

        if (head == null || head.studentTree.getName().compareTo(newStudentName) > 0) {
            newNode.next = head;
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null && current.next.studentTree.getName().compareTo(newStudentName) < 0) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }
    }

    // Elimina de la seqüència l'arbre que tingui el nom especificat
    public void removeStudent(String name) {
        if (head == null) return;

        if (head.studentTree.getName().equals(name)) {
            head = head.next;
            return;
        }

        Node current = head;
        while (current.next != null && !current.next.studentTree.getName().equals(name)) {
            current = current.next;
        }

        if (current.next != null) {
            current.next = current.next.next;
        }
    }

    // Cerca un arbre dins de la seqüència pel seu nom
    public BinaryTree getStudent(String name) {
        Node current = head;
        while (current != null) {
            if (current.studentTree.getName().equals(name)) {
                return current.studentTree;
            }
            current = current.next;
        }
        return null;
    }

    // Retorna una ArrayList amb els noms de tots els estudiants emmagatzemats
    public ArrayList<String> getAllStudentsName() {
        ArrayList<String> studentNames = new ArrayList<>();
        Node current = head;
        while (current != null) {
            studentNames.add(current.studentTree.getName());
            current = current.next;
        }
        return studentNames;
    }

    // Comprova si la llista d'estudiants està buida
    public boolean isEmpty() {
        return head == null;
    }
}
