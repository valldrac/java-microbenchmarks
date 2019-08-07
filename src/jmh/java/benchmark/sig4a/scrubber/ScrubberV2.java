/*
 * Copyright (C) 2014 Open Whisper Systems
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package benchmark.sig4a.scrubber;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Scrub data for possibly sensitive information
 */
public class ScrubberV2 {
  private static final String TAG = Scrubber.class.getSimpleName();

  // Supposedly, the shortest international phone numbers in use contain seven digits
  private static final Pattern E164_PATTERN    = Pattern.compile("\\+(\\d{5,13})\\d{2}");
  private static final Pattern GROUPID_PATTERN = Pattern.compile("__textsecure_group__!(\\S+)");
  private static final Pattern EMAIL_PATTERN   = Pattern.compile("\\S+@\\S+\\.\\S+");

  private static final Pattern[] DEFAULTS = new Pattern[] {
          E164_PATTERN,
          GROUPID_PATTERN,
          EMAIL_PATTERN
  };

  private static final String REDACTION_MARK = "*****";

  private final Pattern[] patterns;
  public ScrubberV2(Pattern... patterns) {
    this.patterns = patterns;
  }

  public ScrubberV2() {
    this(DEFAULTS);
  }

  public String scrub(final String in) {
    if (in == null)
      return null;

    String out = in;
    for (Pattern pattern : patterns) {
      Matcher matcher = pattern.matcher(out);

      if (matcher.find()) {
        StringBuilder builder = new StringBuilder(in.length());
        int lastEndingPos = 0;

        do {
          int group = matcher.groupCount();
          builder.append(out.substring(lastEndingPos, matcher.start(group))).append(REDACTION_MARK);
          lastEndingPos = matcher.end(group);
        } while (matcher.find());

        builder.append(out.substring(lastEndingPos));
        out = builder.toString();
      }
    }
    return out;
  }
}
