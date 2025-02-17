import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {

        Utility ut = new Utility();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-a")) {
                ut.setIfAppend(true);
            }
            if (arg.endsWith(".txt")) {
                ut.addFileToList(arg);
            }
            if (arg.equals("-p")) {
                try {
                    ut.addPrefixToFilenames(args[i + 1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Не введено значение реквизита -p (префикс имени файла)!");
                }
            }
            if (arg.equals("-o")) {
                try {
                    ut.setPath(args[i + 1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Не введено значение реквизита -o (путь)!");
                }
            }
            if (arg.equals("-s")) {
                ut.setStatisticShort(true);
            }
            if (arg.equals("-f")) {
                ut.setStatisticFull(true);
            }
            if (i == args.length - 1 && ut.getInputFiles().isEmpty()) {
                System.out.println("Не введено ни одного файла для чтения.");
                break;
            }
        }
        ut.doCommand();

    }

}