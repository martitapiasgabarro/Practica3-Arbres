import java.io.*;
import java.util.*;

public class BinaryTree {
    private NodeA root;

    private class NodeA {
        Person data;
        NodeA left, right;

        public NodeA(Person data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    public BinaryTree() {
        this.root = null;
    }

    public BinaryTree(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            this.root = preorderLoad(br);
        } catch (IOException e) {
            System.out.println("Error en carregar l'arbre des del fitxer: " + e.getMessage());
        }
    }

    // Mantenim `preorderLoad` com a privat
    private NodeA preorderLoad(BufferedReader br) throws IOException {
        String line = br.readLine();
        if (line == null || line.equals(";")) {
            return null;
        }

        Person person = new Person(line);
        NodeA node = new NodeA(person);
        node.left = preorderLoad(br);
        node.right = preorderLoad(br);
        return node;
    }


    public String getName() {
        return root != null ? root.data.getName() : null;
    }

    public void addNode(Person person, String level) {
        root = addNodeRecursive(root, person, level, 0);
    }

    // Mètode recursiu per afegir un node a l'arbre
    private NodeA addNodeRecursive(NodeA node, Person person, String level, int index) {
        if (node == null) {
            return new NodeA(person);
        }

        if (index >= level.length()) {
            return node; // posició incorrecta
        }

        if (level.charAt(index) == 'L') {
            node.left = addNodeRecursive(node.left, person, level, index + 1);
        } else if (level.charAt(index) == 'R') {
            node.right = addNodeRecursive(node.right, person, level, index + 1);
        }
        return node;
    }

    public void displayTree() {
        displayTreeRecursive(root, 0);
    }

    private void displayTreeRecursive(NodeA node, int level) {
        if (node == null) {
            return;
        }
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        System.out.println(node.data.getName());

        displayTreeRecursive(node.left, level + 1);
        displayTreeRecursive(node.right, level + 1);
    }

    public void preorderSave() {
        if (root == null) {
            throw new IllegalStateException("L'arbre està buit.");
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(root.data.getName() + ".txt"))) {
            preorderSaveRecursive(root, bw);
        } catch (IOException e) {
            System.out.println("Error en desar l'arbre: " + e.getMessage());
        }
    }

    private void preorderSaveRecursive(NodeA node, BufferedWriter bw) throws IOException {
        if (node == null) {
            bw.write(";");
            bw.newLine();
            return;
        }

        bw.write(node.data.toString());
        bw.newLine();
        preorderSaveRecursive(node.left, bw);
        preorderSaveRecursive(node.right, bw);
    }

    public boolean removePerson(String name) {
        return removePersonRecursive(root, name);
    }



    private boolean removePersonRecursive(NodeA node, String name) {
        if (node == null) {
            return false;
        }

        if (node.data.getName().equals(name)) {
            return true;
        }

        boolean foundInLeft = removePersonRecursive(node.left, name);
        boolean foundInRight = removePersonRecursive(node.right, name);


        if (foundInLeft) {
            node.left = null;
        }

        if (foundInRight) {
            node.right = null;
        }
        return foundInLeft || foundInRight;
    }


    public boolean isFrom(String place) {
        return root != null && root.data.getPlaceOfOrigin().equalsIgnoreCase(place);
    }

    public boolean isDescentFrom(String place) {
        return isDescentFromRecursive(root, place);
    }

    private boolean isDescentFromRecursive(NodeA node, String place) {
        if (node == null) return false;
        if (node.data.getPlaceOfOrigin().equalsIgnoreCase(place)) return true;
        return isDescentFromRecursive(node.left, place) || isDescentFromRecursive(node.right, place);
    }

    public int howManyParents() {
        int parents = 0;
        if (root != null) {
            if (root.left != null) parents++;
            if (root.right != null) parents++;
        }
        return parents;
    }

    public int howManyGrandParents() {
        return countNodesRecursive(root.left) + countNodesRecursive(root.right);
    }

    private int countNodesRecursive(NodeA node) {
        if (node == null) return 0;
        return 1 + countNodesRecursive(node.left) + countNodesRecursive(node.right);
    }

    public boolean marriedParents() {
        return root != null && root.left != null && root.right != null &&
                root.left.data.getMaritalStatus() == Person.MARRIED &&
                root.right.data.getMaritalStatus() == Person.MARRIED;
    }
}
