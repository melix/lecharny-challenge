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

    private static final int DISTRIBUTION = 10;

    private final static Random RANDOM = new Random();

    private RandomStringGenerator() {}

    public static String randomAlphanumericString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            int r = RANDOM.nextInt(DISTRIBUTION);
            switch (r) {
                case 0:
                    sb.append('\r');
                    break;
                case 1:
                    sb.append('\n');
                    break;
                case 2:
                    sb.append(' ');
                    break;
                case 3:
                    sb.append("\r\n");
                default:
                    sb.append((char) ('a' + RANDOM.nextInt(26)));
            }
        }
        return sb.toString();
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
