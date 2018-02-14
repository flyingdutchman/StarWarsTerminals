import tools.Crypto;

import javax.crypto.Cipher;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class is responsible for the creation of all the crypted data used in the program
 * It has it's own Main so it can be launchde separatly
 */
public class DataInit {

    private final String RAW_DATA_PATH = "data_raw";
    private final String FINAL_DATA_PATH = "data";

    public DataInit() {
        try {
            Files.createDirectories(Paths.get(FINAL_DATA_PATH));
        } catch (IOException e) {
            System.err.println("Could not create \""+FINAL_DATA_PATH+"\" folder");
            e.printStackTrace();
        }

        //Parsing des fichiers raw
        File[] files = new File("data_raw").listFiles();
        parseFiles(files);
    }

    private void parseFiles(File[] files) {

        for (File file : files) {

            if (file.isDirectory()) {
                System.out.println("Parsing dossier: " + file.getName());
                try {
                    Files.createDirectories(Paths.get(convertDataPath(file.getPath())));
                } catch (IOException e) {
                    System.err.println("Could not create \""+file.getName()+"\" folder");
                    e.printStackTrace();
                }
                parseFiles(file.listFiles()); // Récursion

            } else {
                // If is a file, then encrypt it
                encryptFile(file);
            }
        }
    }

    public void encryptFile(File inputFile) {
        String key = "This is a secret";
        System.out.print("Encryption de "+inputFile.getName()+" : ");
        String newPath =  convertDataPath(inputFile.getPath()) +".encrypted";
        File encryptedFile = new File(newPath);

        try {
            Crypto.fileProcessor(Cipher.ENCRYPT_MODE,key,inputFile,encryptedFile);
            //Pour Decrypt :
            //Crypto.fileProcessor(Cipher.DECRYPT_MODE,key,encryptedFile,decryptedFile);
            System.out.println("Succès");
        } catch (Exception e) {
            System.err.println("Could not encrypt : "+encryptedFile.getName());
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    private String convertDataPath(String oldPath) {
        return oldPath.replaceFirst(RAW_DATA_PATH, FINAL_DATA_PATH);
    }
}
