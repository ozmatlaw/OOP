package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

public class Shell {
    private static final String CMD_EXIT = "exit";
    private static final String ADD = "add";
    private static final String RES = "res";
    private static final String REND = "render";
    private static final String CHARS = "chars";
    private static final String REMOVE = "remove";
    private static final String CONSOLE = "console";
    private static final String DOWN = "down";
    private static final String UP = "up";
    private static final String ALL = "all";
    private static final String SPACE = "space";
    private static final String FONT_NAME = "Courier New";
    private static final String OUTPUT_FILENAME = "out.html";
    private static final int MIN_PIXELS_PER_CHAR = 2;
    private static final int INITIAL_CHARS_IN_ROW = 64;
    private static final String INITIAL_CHARS_RANGE = "0-9";
    private final int minCharsInRow;
    private final int maxCharsInRow;
    private int charsInRow;
    private final BrightnessImgCharMatcher charMatcher;
    private AsciiOutput output;
    private final Set<Character> charSet = new HashSet<>();

    /**
     * basic constructor
     * @param img the desired img to display
     */
    public Shell(Image img) {
        this.minCharsInRow = Math.max(1, img.getWidth() / img.getHeight());
        this.maxCharsInRow = img.getWidth() / MIN_PIXELS_PER_CHAR;
        this.charsInRow = Math.max(Math.min(INITIAL_CHARS_IN_ROW, maxCharsInRow), minCharsInRow);
        this.charMatcher = new BrightnessImgCharMatcher(img, FONT_NAME);
        this.output = new HtmlAsciiOutput(OUTPUT_FILENAME, FONT_NAME);
        addChars(INITIAL_CHARS_RANGE);
    }

    /**
     * runs the application for the user
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(">>> ");
        String cmd = scanner.next().trim();
        while (!cmd.toLowerCase().equals(CMD_EXIT)) {
            var param = scanner.nextLine().trim();
            switch (cmd) {
                case CHARS:
                    showChars();
                    break;
                case ADD:
                    add(param);
                    break;
                case REMOVE:
                    removeChars(param);
                    break;
                case RES:
                    resChange(param);
                    break;
                case REND:
                    render();
                    break;
                case CONSOLE:
                    output = new ConsoleAsciiOutput();
                    break;
                default:
                    System.out.println("Invalid command");
            }
            System.out.print(">>> ");
            cmd = scanner.next();
        }
    }

    /**
     * changes the resolution based in the given command and range
     * @param s the given resolution cammnd to up down or invalid command
     */
    private void resChange(String s) {
        switch (s) {
            case UP:
                if (charsInRow * 2 <= maxCharsInRow) {
                    charsInRow *= 2;
                } else {
                    System.out.println("You're using the maximal resolution");
                    return;
                }
                break;
            case DOWN:
                if (charsInRow * 2 >= minCharsInRow) {
                    charsInRow /= 2;
                } else {
                    System.out.println("You're using the minimal resolution");
                    return;
                }
                break;
            default:
                System.out.println("Invalid resolution command ");
                return;
        }
        System.out.println("Width set to " + charsInRow);
    }

    /**
     * add command implementation
     * @param param string command of desired chars to add
     */
    private void add(String param) {
        addChars(param);
    }

    /**
     * gets the range of chars to add/remove to the set
     * @param param string command of desired chars to remove/add
     * @return the range of chars to add/remove
     */
    private static char[] parseCharRange(String param) {
        if (param.length() == 1) {
            return new char[]{param.charAt(0), param.charAt(0)};
        } else if (param.equals(ALL)) {
            return new char[]{' ', '~'};
        } else if (param.equals(SPACE)) {
            return new char[]{' ', ' '};
        } else if (param.charAt(1) == '-' && param.length() == 3) {
            if (param.charAt(0) <= param.charAt(2)) {
                return new char[]{param.charAt(0), param.charAt(2)};
            }else{
                return new char[]{param.charAt(2), param.charAt(0)};
            }
        }
        return null;
    }

    /**
     * removes the given chars from the current char set
     * @param s string command of desired chars to remove
     */
    private void removeChars(String s) {
        char[] range = parseCharRange(s);
        if (range != null) {
            Stream.iterate(range[0], c -> c <= range[1], c -> (char) ((int) c + 1)).forEach(charSet::remove);
        }
    }

    /**
     * add the given chars to the current set of chars
     * @param s string command of desired chars to add
     */
    private void addChars(String s) {
        char[] range = parseCharRange(s);
        if (range != null) {
            Stream.iterate(range[0], c -> c <= range[1], c -> (char) ((int) c + 1)).forEach(charSet::add);
        }
    }

    /**
     * shows the chars in the console
     */
    private void showChars() {
        charSet.stream().sorted().forEach(c -> System.out.print(c + " "));
        System.out.println();
    }

    /**
     * renders the img
     */
    private void render(){
        char[][] img = charMatcher.chooseChars(charsInRow,charSet.toArray( new Character[0]));
        if(img != null){
            output.output(img);
        }
    }
}
