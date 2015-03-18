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

import java.util.Random;

public class RandomStringGenerator {
    private final static Random RANDOM = new Random();

    public static String randomAlphanumericString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            int r = RANDOM.nextInt(10);
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
}
