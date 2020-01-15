package vcs;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Main {

    private static final String REPO = "VCS/";
    private static final File REPO_DIR = new File(REPO);

    public static void main(String[] args) throws IOException {
        switch (args[0]) {
            case "init": {
                initRepository();
                break;
            }
            case "ci": {
                System.out.println("ci");
                checkin(args[1]);
                break;
            }
            case "co": {
                checkout(args[2], Integer.parseInt(args[1]));
                break;
            }
            case "help": {
                System.out.println("Vcs usage:");
                System.out.println("   vcs ci <file-name>");
                System.out.println("   vcs co <version-number> <file-name>");
            }
        }
    }

    private static void initRepository() {
        REPO_DIR.mkdir();
    }

    private static void checkout(String fileName, int version) throws IOException {
        String versionedFile = REPO + fileName + "," + version;
        File file = new File(versionedFile);
        if (file.exists()) {
            copy(file.getAbsolutePath(), fileName);
        } else {
            System.out.println("No such version");
        }
    }

    private static void copy(String source, String dest) throws IOException {
        Path s = Path.of(source);
        Path d = Path.of(dest);
        Files.copy(s, d, REPLACE_EXISTING);
    }

    private static void checkin(String fileName) throws IOException {
        if (!REPO_DIR.exists()) {
            initRepository();
        }

        int version = latestRevision(fileName) + 1;
        copy(fileName, REPO + fileName + "," + version);
    }

    private static int latestRevision(String fileName) {
        int max = 0;
        for (String f : REPO_DIR.list()) {
            String first = f.substring(0, f.indexOf(","));
            String last = f.substring(f.indexOf(",")+1);
            if (first.equals(fileName)) {
                int n = Integer.parseInt(last);
                max = max < n ? n : max;
            }
        }
        return max;
    }

}
