package replace;

import java.io.File;
import java.io.IOException;

/**
 * Created by htrembl2 on 3/19/15.
 */
public class StatsOnRealFile {

    public enum StatType {
        SPACE,
        LINE_FEED,
        LINE_FEED_WITH_SPACE,
        OTHER,
        TOTAL
    }

    private static void inc(int[] stats, StatType type) {
        stats[type.ordinal()]++;
    }

    public static int[] stats(String content) {
        int[] stats = new int[StatType.values().length];

        int length = content.length();
        for(int i = 0; i < length; i++) {
            inc(stats, StatType.TOTAL);

            char c = content.charAt(i);
            if(c == ' ') {
                inc(stats, StatType.SPACE);
            }
            else if(c == '\r' || c == '\n') {
                if(i + 1 == length) {
                    inc(stats, StatType.LINE_FEED);
                    break;
                }
                char next = content.charAt(i + 1);
                if(next == '\r' || next == '\n') {
                    i++;
                }
                if(i + 1 == length) {
                    inc(stats, StatType.LINE_FEED);
                    break;
                }
                next = content.charAt(i + 1);
                if(next == ' ') {
                    inc(stats, StatType.LINE_FEED_WITH_SPACE);
                    i++;
                }
                else {
                    inc(stats, StatType.LINE_FEED);
                }
            }
            else {
                inc(stats, StatType.OTHER);
            }
        }

        return stats;
    }

    public static void main(String[] args) throws IOException {
        String file = args[0];
        String content = FileUtils.readFile(new File(file));

        int[] stats = stats(content);
        int total = stats[StatType.TOTAL.ordinal()];

        for(int i = 0; i < stats.length - 1; i++) {
            writeMessage(StatType.values()[i], stats[i], total);
        }
    }

    private static void writeMessage(StatType name, int count, int total) {
        System.out.println(count + " " + name + " (" + (count * 100 / total) + "%)");
    }
}
