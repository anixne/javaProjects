import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FileExplorer {

    private File currentDirectory;

    public FileExplorer() {
        this.currentDirectory = new File(System.getProperty("user.dir"));
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Java File Explorer. Type help for available commands.\n");

        while (true) {
            System.out.print(currentDirectory.getAbsolutePath() + " > ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) continue;

            String[] parts = input.split("\\s+", 2);
            String command = parts[0];
            String argument = parts.length > 1 ? parts[1] : "";

            try {
                switch (command.toLowerCase()) {
                    case "ls" -> listDirectoryContents();
                    case "cd" -> changeDirectory(argument);
                    case "mkdir" -> createDirectory(argument);
                    case "touch" -> createFile(argument);
                    case "rm" -> deleteFileOrDirectory(argument);
                    case "info" -> displayFileInfo(argument);
                    case "help" -> displayHelp();
                    case "exit" -> {
                        System.out.println("Goodbye.");
                        return;
                    }
                    default -> System.out.println("Unknown command. Type 'help' to see the list of commands.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void listDirectoryContents() {
        File[] files = currentDirectory.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("Directory is empty.");
            return;
        }

        for (File file : files) {
            String type = file.isDirectory() ? "[DIR] " : "      ";
            System.out.printf("%s%s%n", type, file.getName());
        }
    }

    private void changeDirectory(String path) {
        if (path.isEmpty()) {
            System.out.println("Please specify a directory name.");
            return;
        }

        File newDir = path.equals("..")
                ? currentDirectory.getParentFile()
                : new File(currentDirectory, path);

        if (newDir != null && newDir.exists() && newDir.isDirectory()) {
            currentDirectory = newDir;
        } else {
            System.out.println("Directory not found: " + path);
        }
    }

    private void createDirectory(String name) {
        if (name.isEmpty()) {
            System.out.println("Please provide a name for the directory.");
            return;
        }

        File newDir = new File(currentDirectory, name);
        if (newDir.mkdir()) {
            System.out.println("Directory created: " + newDir.getName());
        } else {
            System.out.println("Failed to create directory. It may already exist.");
        }
    }

    private void createFile(String name) {
        if (name.isEmpty()) {
            System.out.println("Please provide a file name.");
            return;
        }

        File newFile = new File(currentDirectory, name);
        try {
            if (newFile.createNewFile()) {
                System.out.println("File created: " + newFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("Could not create file: " + e.getMessage());
        }
    }

    private void deleteFileOrDirectory(String name) {
        if (name.isEmpty()) {
            System.out.println("Please specify a file or directory to delete.");
            return;
        }

        File target = new File(currentDirectory, name);
        if (!target.exists()) {
            System.out.println("File or directory not found.");
            return;
        }

        if (target.delete()) {
            System.out.println("Deleted: " + target.getName());
        } else {
            System.out.println("Could not delete. Make sure it's empty or not locked.");
        }
    }

    private void displayFileInfo(String name) {
        if (name.isEmpty()) {
            System.out.println("Please specify a file name.");
            return;
        }

        File file = new File(currentDirectory, name);
        if (!file.exists()) {
            System.out.println("File not found.");
            return;
        }

        System.out.println("Name:      " + file.getName());
        System.out.println("Path:      " + file.getAbsolutePath());
        System.out.println("Type:      " + (file.isDirectory() ? "Directory" : "File"));
        System.out.println("Readable:  " + file.canRead());
        System.out.println("Writable:  " + file.canWrite());
        System.out.println("Size:      " + file.length() + " bytes");
        System.out.println("Modified:  " + file.lastModified());
    }

    private void displayHelp() {
        System.out.println("""
                Available commands:
                  ls              List directory contents
                  cd <dir>        Change current directory
                  mkdir <name>    Create a new directory
                  touch <name>    Create a new file
                  rm <name>       Delete file or directory
                  info <name>     Show file details
                  help            Show this help message
                  exit            Exit the program
                """);
    }

    public static void main(String[] args) {
        new FileExplorer().start();
    }
}
