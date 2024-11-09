import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    private Students students = new Students();
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Main program = new Main();
        System.out.print("Introdueix la ruta de la carpeta amb els estudiants: ");
        String folderPath = program.scanner.nextLine();
        program.readAllStudents(folderPath);
        program.showMenu();
    }

    private void showMenu() {
        int option;
        do {
            System.out.println("Menú Principal:");
            System.out.println("1. Mostrar llistat d'estudiants");
            System.out.println("2. Mostrar família d'un estudiant");
            System.out.println("3. Afegir un estudiant");
            System.out.println("4. Modificar un estudiant");
            System.out.println("5. Mostrar el informe");
            System.out.println("6. Eliminar alumne");
            System.out.println("7. Guardar i Sortir");
            System.out.print("Tria una opció: ");

            option = scanner.nextInt();
            scanner.nextLine(); // Netejar el buffer

            switch (option) {
                case 1:
                    displayAllStudentsNames();
                    break;
                case 2:
                    System.out.print("Introdueix el nom de l'estudiant: ");
                    String name = scanner.nextLine();
                    showStudentFamily(name);
                    break;
                case 3:
                    addNewStudent();
                    break;
                case 4:
                    System.out.print("Introdueix el nom de l'estudiant per modificar: ");
                    name = scanner.nextLine();
                    modifyStudent(name);
                    break;
                case 5:
                    mostrarInforme();
                    break;
                case 6:
                    System.out.print("Introdueix el nom de l'estudiant a eliminar: ");
                    String studentNameToDelete = scanner.nextLine();
                    students.removeStudent(studentNameToDelete);
                    System.out.println("Estudiant eliminat.");
                    break;
                case 7:
                    saveAllStudents();
                    System.out.println("Dades guardades. Sortint del programa.");
                    break;
                default:
                    System.out.println("Opció no vàlida. Si us plau, tria una opció vàlida.");
            }
        } while (option != 7);
    }

    private void readAllStudents(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Carpeta no vàlida.");
            return;
        }

        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.getName().endsWith(".txt")) {
                try {
                    BinaryTree studentTree = loadBinaryTreeFromFile(file);
                    students.addStudent(studentTree);
                    System.out.println("Estudiant carregat des de: " + file.getName());
                } catch (IOException e) {
                    System.out.println("Error al llegir el fitxer " + file.getName() + ": " + e.getMessage());
                }
            }
        }
    }


    private BinaryTree loadBinaryTreeFromFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        BinaryTree tree = new BinaryTree(); //Suposar que es buit

        while ((line = reader.readLine()) != null) {
            if (line.contains("Name:")) {
                Person person = parseStudentData(line);
                tree.addNode(person, ""); // Afegir  l'arbre, inicialment com arrel
            } else if (line.trim().equals(";")) {
                // Si hi ha un ';', vol dir que el node no té fills
                break;
            }
        }
        reader.close();
        return tree;
    }

    private Person parseStudentData(String line) {
        //Format de la linia: "Name: <name>, place of Origin: <place>, marital status: <status>"
        String name = extractValue(line, "Name: ");
        String placeOfOrigin = extractValue(line, "place of Origin: ");
        String maritalStatusStr = extractValue(line, "marital status: ");

        // Convertir el estat civil de text a un valor int
        int maritalStatus = convertMaritalStatus(maritalStatusStr);

        return new Person(name, placeOfOrigin, maritalStatus);
    }

    private int convertMaritalStatus(String status) {
        switch (status.trim().toLowerCase()) {
            case "single":
                return 0;
            case "married":
                return 1;
            case "divorced":
                return 2;
            case "widowed":
                return 3;
            default:
                return -1; // Estat desconegut
        }
    }



    private String extractValue(String line, String key) {
        int startIdx = line.indexOf(key) + key.length();
        int endIdx = line.indexOf(",", startIdx);
        if (endIdx == -1) {
            endIdx = line.length(); // Si no hay coma, el valor es hasta el final de la línea
        }
        return line.substring(startIdx, endIdx).trim();
    }



    private void saveAllStudents() {
        for (String studentName : students.getAllStudentsName()) {
            BinaryTree studentTree = students.getStudent(studentName);
            if (studentTree != null) {
                studentTree.preorderSave();
            }
        }
    }


    private void displayAllStudentsNames() {
        var names = students.getAllStudentsName();
        if (names == null || names.isEmpty()) {
            System.out.println("No hi ha estudiants emmagatzemats.");
        } else {
            System.out.println("Llistat d'estudiants:");
            for (String name : names) {
                System.out.println(name);
            }
        }
    }

    private void showStudentFamily(String name) {
        BinaryTree studentTree = students.getStudent(name);
        if (studentTree != null) {
            System.out.println("Família de " + name + ":");
            studentTree.displayTree();
        } else {
            System.out.println("Estudiant no trobat.");
        }
    }

    private void addNewStudent() {
        System.out.print("Introdueix el nom de l'estudiant: ");
        String name = scanner.nextLine();
        System.out.print("Introdueix el lloc d'origen: ");
        String placeOfOrigin = scanner.nextLine();
        System.out.print("Introdueix l'estat civil (0: Solter, 1: Casat, 2: Divorciat, 3: Viudo): ");
        int maritalStatus = scanner.nextInt();
        scanner.nextLine(); // Netejar el buffer

        Person student = new Person(name, placeOfOrigin, maritalStatus);
        BinaryTree studentTree = new BinaryTree();
        studentTree.addNode(student, ""); // afegir com a arrel
        students.addStudent(studentTree);

        System.out.println("Estudiant afegit correctament.");
    }

    private void modifyStudent(String name) {
        BinaryTree studentTree = students.getStudent(name);
        if (studentTree == null) {
            System.out.println("Estudiant no trobat.");
            return;
        }

        System.out.println("Opcions de modificació:");
        System.out.println("1. Afegir membre de la família");
        System.out.println("2. Eliminar membre de la família");
        System.out.print("Tria una opció: ");
        int option = scanner.nextInt();
        scanner.nextLine(); // Netejar el buffer

        if (option == 1) {
            System.out.print("Introdueix el nom: ");
            String familyName = scanner.nextLine();
            System.out.print("Introdueix el lloc d'origen: ");
            String familyPlace = scanner.nextLine();
            System.out.print("Introdueix l'estat civil (0: Solter, 1: Casat, 2: Divorciat, 3: Viudo): ");
            int familyStatus = scanner.nextInt();
            scanner.nextLine(); // Netejar el buffer

            System.out.print("Introdueix la posició (L, R, LL, etc.): ");
            String position = scanner.nextLine();

            Person familyMember = new Person(familyName, familyPlace, familyStatus);
            studentTree.addNode(familyMember, position);
            System.out.println("Membre de la família afegit correctament.");
        } else if (option == 2) {
            System.out.print("Introdueix el nom del membre a eliminar: ");
            String familyName = scanner.nextLine();
            studentTree.removePerson(familyName);
            System.out.println("Membre de la família eliminat.");
        } else {
            System.out.println("Opció no vàlida.");
        }
    }

    private void mostrarInforme() {
        System.out.print("Introdueix la ciutat de naixement dels estudiants: ");
        String birthCity = scanner.nextLine();
        System.out.print("Introdueix la ciutat de procedència de la família: ");
        String originCity = scanner.nextLine();

        int bornInCity = 0;
        int descentFromCity = 0;
        int singleParent = 0;
        int divorcedParents = 0;
        int grandparents = 0;
        int moreThanTwoGrandparents = 0;

        for (String name : students.getAllStudentsName()) {
            BinaryTree studentTree = students.getStudent(name);
            if (studentTree.isFrom(birthCity)) bornInCity++;
            if (studentTree.isDescentFrom(originCity)) descentFromCity++;
            if (studentTree.howManyParents() == 1) singleParent++;
            if (studentTree.howManyParents() == 2 && !studentTree.marriedParents()) divorcedParents++;
            int grandParentCount = studentTree.howManyGrandParents();
            if (grandParentCount > 0) grandparents++;
            if (grandParentCount > 2) moreThanTwoGrandparents++;
        }

        System.out.println("Informe:");
        System.out.println("Alumnes nascuts a " + birthCity + ": " + bornInCity);
        System.out.println("Alumnes amb descendència de " + originCity + ": " + descentFromCity);
        System.out.println("Alumnes amb un únic progenitor: " + singleParent);
        System.out.println("Alumnes amb progenitors divorciats: " + divorcedParents);
        System.out.println("Alumnes amb avis o àvies: " + grandparents);
        System.out.println("Alumnes amb més de 2 avis o àvies: " + moreThanTwoGrandparents);
    }
}
