package sorting;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(final String[] args) {
        Scanner scanner = new Scanner(System.in);
        java.util.ArrayList<String> arguments = new java.util.ArrayList<>(List.of(args));
        arguments.stream().filter(arg -> arg.matches("-*"))
                .filter(arg -> !arg.matches("-sortingType|-dataType|-inputFile|-outFile"))
                .forEach(arg -> System.out.printf("\"%s\" is not a valid parameter. It will be skipped.", arg));
        String inputFile = getArgType(args, arguments, "-inputFile");
        String outputFile = getArgType(args, arguments, "-outputFile");
        String sortingType = getArgType(args, arguments, "-sortingType");
        if (arguments.contains("-sortingType") && !sortingType.matches("byCount|natural")) {
            System.out.println("No sorting type defined!");
            return;
        } else if (!arguments.contains("-sortingType")) {
            sortingType = "natural";
        }
        String dataType = getArgType(args, arguments, "-dataType");
        if (arguments.contains("-dataType") && !dataType.matches("long|line|word")) {
            System.out.println("No data type defined!");
            return;
        } else if (!arguments.contains("-dataType")){
            dataType = "word";
        }
        switch (dataType) {
            case "long":
                ArrayList<Long> numbers = new ArrayList<>();
                if (!"unknown".equals(inputFile)) {
                    String[] input = input(inputFile).split("\n");
                    for (String s : input) {
                        try {
                            long number = Long.parseLong(s);
                            numbers.add(number);
                        } catch (NumberFormatException nfe) {
                            System.out.printf("\"%s\" is not a long. It will be skipped\n", s);
                        }
                    }
                } else {
                    while (scanner.hasNext()) {
                        String input = scanner.next();
                        try {
                            long number = Long.parseLong(input);
                            numbers.add(number);
                        } catch (NumberFormatException nfe) {
                            System.out.printf("\"%s\" is not a long. It will be skipped\n", input);
                        }
                    }
                }
                if (numbers.size() < 1) {
                    return;
                }
                if ("byCount".equals(sortingType)) {
                    numbers.sort(Comparator.naturalOrder());
                    numbers.sort(Comparator.comparingInt(num -> getCount(numbers, num)));
                } else {
                    numbers.sort(Comparator.naturalOrder());
                }
                if (!"unknown".equals(outputFile)) {
                    output(outputFile, numbers);
                } else {
                    longs(numbers, sortingType);
                }
                break;
            case "line":
                ArrayList<String> strings = new ArrayList<>();
                if (!"unknown".equals(inputFile)) {
                    String[] input = input(inputFile).split("\n");
                    strings.addAll(Arrays.asList(input));
                } else {
                    while (scanner.hasNextLine()) {
                        strings.add(scanner.nextLine());
                    }
                    if (strings.size() < 1) {
                        return;
                    }
                }
                stringSortingMethod(sortingType, strings);
                if (!"unknown".equals(outputFile)) {
                    output(outputFile, strings);
                } else {
                    lines(strings, sortingType);
                }
                break;
            case "word":
                ArrayList<String> words = new ArrayList<>();
                if (!"unknown".equals(inputFile)) {
                    String[] input = input(inputFile).split("\n");
                    words.addAll(Arrays.asList(input));
                } else {
                    while (scanner.hasNext()) {
                        words.add(scanner.next());
                    }
                    if (words.size() < 1) {
                        return;
                    }
                }
                stringSortingMethod(sortingType, words);
                if (!"unknown".equals(outputFile)) {
                    output(outputFile, words);
                } else {
                    words(words, sortingType);
                }
                break;
        }

    }

    private static void stringSortingMethod(String sortingType, ArrayList<String> strings) {
        if ("byCount".equals(sortingType)) {
            strings.sort(Comparator.naturalOrder());
            strings.sort(Comparator.comparing(word -> getCount(strings, word)));
        } else {
            strings.sort(Comparator.naturalOrder());
        }
    }

    private static String input(String inputFile) {
        StringBuilder input = new StringBuilder();
        File file = new File(inputFile);
        try (FileReader reader = new FileReader(file)) {
            int c = reader.read();
            while (c != -1) {
                input.append((char) c);
                c = reader.read();
            }
        } catch (IOException io) {
            System.out.println("Read Error");
        }

        return input.toString();
    }

    private static <T> void output(String outputFile, ArrayList<T> array) {
        File output = new File(outputFile);
        try (FileWriter writer = new FileWriter(output)) {
            for (T item : array) {
                writer.write(item + "\n");
            }
        } catch (IOException io) {
            System.out.println("Write Error");
        }
    }

    private static String getArgType(String[] args, java.util.ArrayList<String> arguments, String argType) {
        return arguments.contains(argType) &&
                arguments.size() > arguments.indexOf(argType) + 1 ?
                args[arguments.indexOf(argType) + 1] : "unknown";
    }

    protected static void words(ArrayList<String> strings, String sortingType) {
        System.out.printf("Total words: %d.\n", strings.size());
        if ("byCount".equals(sortingType)) {
            printSortByCount(strings);
        } else {
            printSortByNatural(strings);
        }
    }

    protected static void lines(ArrayList<String> strings, String sortingType) {
        System.out.printf("Total lines: %d.\n", strings.size());
        if ("byCount".equals(sortingType)) {
            printSortByCount(strings);
        } else {
            printSortByNatural(strings);
        }
    }


    protected static void longs(ArrayList<Long> numbers, String sortingType) {
        System.out.printf("Total numbers: %d.\n", numbers.size());
        if ("byCount".equals(sortingType)) {
            printSortByCount(numbers);
        } else {
            printSortByNatural(numbers);
        }
    }

    protected static <T> int getPercentage(ArrayList<T> array, double count) {
        double arraySize = array.size();
        final double HUNDRED = 100.0;
        double percentage = (count / arraySize) * HUNDRED;
        return (int) Math.round(percentage);
    }

    protected static <T> int getCount (ArrayList<T> array, T item) {
        int count = 0;
        for (T t : array) {
            if (t.equals(item)) {
                count++;
            }
        }
        return count;
    }

    protected static <T> void printSortByCount(ArrayList<T> array) {
        java.util.ArrayList<T> alreadyDisplayed = new java.util.ArrayList<>();
        for (T item : array) {
            if (alreadyDisplayed.contains(item)) {
                continue;
            }
            int count = getCount(array, item);
            System.out.printf("%s: %d time(s), %d%s\n", item.toString(), count,
                    getPercentage(array, count), "%");
            alreadyDisplayed.add(item);
        }
    }

    protected static <T> void printSortByNatural(ArrayList<T> array) {
        System.out.print("Sorted data:");
        boolean isStringType = "string".equals(getType(array));
        for (T item : array) {
            System.out.printf(isStringType ? "\n%s" : " %s", item);
        }
    }

    protected static <T> String getType(ArrayList<T> array) {
        for (T t : array) {
            if (t.getClass() == String.class) {
                for (T t1 : array) {
                    if (t1.toString().length() > 5) {
                        return "string";
                    }
                }
                return "word";
            } else if (t.getClass() == Long.class) {
                return "long";
            } else if (t.getClass() == Integer.class) {
                return "int";
            }
        }
        return "unknown";
    }

}

