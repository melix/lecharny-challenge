package replace;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by htrembl2 on 3/19/15.
 */
public class StatsOnRealFileTest {

    @Test
    public void testBoundaries() {
        // just make sure it doesn't crash
        StatsOnRealFile.stats("\r");
        StatsOnRealFile.stats("\n");
        StatsOnRealFile.stats("\r\n");
        StatsOnRealFile.stats("\n\r");
        StatsOnRealFile.stats("\r ");
        StatsOnRealFile.stats("\n ");
        StatsOnRealFile.stats("\r\n ");
        StatsOnRealFile.stats("\n\r ");
    }

    @Test
    public void testStats() throws Exception {
        String content =
                  "aa aa\r"
                + "b b\n"
                + "c c\r\n"
                + "dd d d\n\r"
                + "ee\n"
                + "b b\n "
                + "c c\n "
                + "dd d d\n "
                + "\n "
                + "ee\r";

        int[] expected = new int[StatsOnRealFile.StatType.values().length];
        expected[StatsOnRealFile.StatType.SPACE.ordinal()] = 9;
        expected[StatsOnRealFile.StatType.LINE_FEED.ordinal()] = 6;
        expected[StatsOnRealFile.StatType.LINE_FEED_WITH_SPACE.ordinal()] = 4;
        expected[StatsOnRealFile.StatType.OTHER.ordinal()] = 24;
        expected[StatsOnRealFile.StatType.TOTAL.ordinal()] = 43;

        int[] actual = StatsOnRealFile.stats(content);

        assertArrayEquals(expected, actual);
    }
}