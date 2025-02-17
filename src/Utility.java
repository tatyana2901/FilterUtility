import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class Utility {

    private String stringFilename = "strings.txt";
    private String floatFilename = "floats.txt";
    private String intFilename = "integers.txt";
    private String path;
    private boolean ifAppend;
    private boolean isStatisticShort;
    private boolean isStatisticFull;
    private List<String> inputFiles = new ArrayList<>();

    public void addFileToList(String filename) {
        inputFiles.add(filename);
    }

    public List<String> getInputFiles() {
        return inputFiles;
    }

    public void setStatisticShort(boolean statisticShort) {
        isStatisticShort = statisticShort;
    }

    public void setStatisticFull(boolean statisticFull) {
        isStatisticFull = statisticFull;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void addPrefixToFilenames(String prefix) {
        this.stringFilename = new StringBuilder(stringFilename).insert(stringFilename.indexOf("strings.txt"), prefix).toString();
        this.floatFilename = new StringBuilder(floatFilename).insert(floatFilename.indexOf("floats.txt"), prefix).toString();
        this.intFilename = new StringBuilder(intFilename).insert(intFilename.indexOf("integers.txt"), prefix).toString();
    }

    public void setIfAppend(boolean ifAppend) {
        this.ifAppend = ifAppend;
    }

    public void deleteFileIfEmpty(File file) {
        if (file.length() == 0) {
            file.delete();
        }
    }

    public void closeAllBuffRdr(List<BufferedReader> list) {
        list.forEach(bufferedReader -> {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public List<BufferedReader> getBuffReader() {
        List<BufferedReader> bfList = new ArrayList<>();
        inputFiles.forEach(s -> {
            try {
                bfList.add(new BufferedReader(new FileReader(s, StandardCharsets.UTF_8)));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
        return bfList;
    }

    public int getMaxStringsVal() {

        Optional<Integer> max = inputFiles.stream().map(s -> {
            try {
                return Files.readAllLines(Paths.get(s)).size();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return 0;
            }
        }).max(Comparator.comparingInt(o -> o));
        return max.orElse(0);

    }

    public void doCommand() {

        int strCounter = 0;
        int flCounter = 0;
        int intCounter = 0;

        double minFl = Double.MAX_VALUE;
        double maxFl = -Double.MAX_VALUE;
        double sumFl = 0.0;

        long minInt = Long.MAX_VALUE;
        long maxInt = Long.MIN_VALUE;
        long sumInt = 0L;

        int maxStrLength = 0;
        int minStrLength = Integer.MAX_VALUE;


        File intFile = new File(path, intFilename);
        File floatFile = new File(path, floatFilename);
        File stringFile = new File(path, stringFilename);

        FileWriter fw = null;
        FileWriter fw1 = null;
        FileWriter fw2 = null;
        try {
            fw = new FileWriter(intFile, ifAppend);
            fw1 = new FileWriter(floatFile, ifAppend);
            fw2 = new FileWriter(stringFile, ifAppend);

            List<BufferedReader> bfList = getBuffReader();


            for (int i = 0; i < getMaxStringsVal(); i++) {
                for (BufferedReader bufferedReader : bfList) {
                    String line;
                    if ((line = bufferedReader.readLine()) != null) {
                        try {
                            long l = Long.parseLong(line);
                            fw.write(line + "\n");
                            intCounter++;
                            sumInt = sumInt + l;
                            if (l > maxInt) {
                                maxInt = l;
                            }
                            if (l < minInt) {
                                minInt = l;
                            }
                        } catch (NumberFormatException e) {
                            try {
                                double d = Double.parseDouble(line);
                                fw1.write(line + "\n");
                                flCounter++;
                                sumFl = sumFl + d;
                                if (d > maxFl) {
                                    maxFl = d;
                                }
                                if (d < minFl) {
                                    minFl = d;
                                }
                            } catch (NumberFormatException ex) {
                                fw2.write(line + "\n");
                                strCounter++;
                                if (line.length() > maxStrLength) {
                                    maxStrLength = line.length();
                                }
                                if (line.length() < minStrLength) {
                                    minStrLength = line.length();
                                }
                            }
                        }
                    }
                }
            }

            closeAllBuffRdr(bfList);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
                if (fw1 != null) {
                    fw1.close();
                }
                if (fw2 != null) {
                    fw2.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }
        deleteFileIfEmpty(intFile);
        deleteFileIfEmpty(floatFile);
        deleteFileIfEmpty(stringFile);

        if (isStatisticShort || isStatisticFull) {
            if (intCounter != 0) {
                System.out.println("В файл " + intFile.getAbsolutePath() + " записано " + intCounter + " элементов.");
                if (isStatisticFull) {
                    System.out.println("Максимальное значение: " + maxInt + "; " + "Минимальное значение: " + minInt + "; " + "Сумма: " + sumInt + "; " + "Среднее " + sumInt / intCounter + ".");
                }
            }
            if (flCounter != 0) {
                System.out.println("В файл " + floatFile.getAbsolutePath() + " записано " + flCounter + " элементов.");
                if (isStatisticFull) {
                    System.out.println("Максимальное значение: " + maxFl + "; " + "Минимальное значение: " + minFl + "; " + "Сумма: " + sumFl + "; " + "Среднее " + sumFl / flCounter + ".");
                }
            }
            if (strCounter != 0) {
                System.out.println("В файл " + stringFile.getAbsolutePath() + " записано " + strCounter + " элементов.");
                if (isStatisticFull) {
                    System.out.println("Размер самой длинной строки: " + maxStrLength + "; " + "Размер самой короткой строки: " + minStrLength + ".");
                }
            }
        }

    }
}
