/*
 * Copyright 2003-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package replace;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public final class RandomStringGenerator {

    private static final int DISTRIBUTION = 100;

    private final static Random RANDOM = new Random();

    private RandomStringGenerator() {}

    public static String randomAlphanumericString(int len) {
        StringBuilder sb = new StringBuilder(len);
        // Expected statistics:
        // 40 SPACE (7%)
        // 22 LINE_FEED (3%)
        // 15 LINE_FEED_WITH_SPACE (2%)
        // 493 OTHER (86%)

        for (int i = 0; i < len; i++) {
            int r = RANDOM.nextInt(DISTRIBUTION);
            if(r >= 0 && r < 7) {
                sb.append(' ');
            }
            else if(r >= 7 && r < 10) {
                sb.append(pickEol());
            }
            else if(r >= 10 && r < 12) {
                sb.append(pickEol() + " ");
            }
            else {
                sb.append((char) ('a' + RANDOM.nextInt(26)));
            }
        }
        return sb.toString();
    }

    private static String pickEol() {
        int r = RANDOM.nextInt(4);
        switch (r) {
        case 0:
            return "\n\r";
        case 1:
            return "\r\n";
        case 2:
            return "\n";
        case 3:
            return "\r";
        }
        throw new InternalError("Unmatched number: " + r);
    }

    private static File filePath(String dir, int size) {
        return new File(dir, size + ".txt");
    }

    public static void generateFile(String dir, int size) throws IOException {
        String string = randomAlphanumericString(size / 3) + "\n\r " + randomAlphanumericString(size / 3) + "\n "
                + randomAlphanumericString(size / 3);
        File path = filePath(dir, size);
        FileUtils.writeFile(path, string);
    }

    public static String readFile(String dir, int size) throws IOException {
        File path = filePath(dir, size);
        return FileUtils.readFile(path);
    }

    public static void main(String[] args) throws IOException {
        String outputDir = args[0];
        File file = new File(outputDir);
        boolean proceed = file.exists() || file.mkdirs();
        if (proceed) {
            String[] sizes = args[1].split(",");
            for (String size : sizes) {
                generateFile(outputDir, Integer.valueOf(size));
            }
        } else {
            throw new IOException("Unable to create output directory: "+outputDir);
        }
    }
}
