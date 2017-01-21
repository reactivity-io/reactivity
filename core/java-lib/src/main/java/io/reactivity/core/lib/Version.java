/*
 * The MIT License (MIT) Copyright (c) 2016 The reactivity authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package io.reactivity.core.lib;

/**
 * <p>
 * A version follows the semver protocol with an optional snapshot status.
 * </p>
 *
 * @author Guillaume DROUET
 * @since 0.1.0
 */
public class Version {

    /**
     * The version in MAJOR.MINOR.PATCH format.
     */
    private final String semver;

    /**
     * Indicates is the version is a snapshot or not.
     */
    private final boolean snapshot;

    /**
     * A derived integer from the version. For instance, for a version 0.1.0, the integer should be 10.
     */
    private final int number;

    /**
     * <p>
     * Builds a new version.
     * </p>
     *
     * @param semver the version according to semver protocol
     * @param snapshot indicates if the version is a snapshot
     * @param number the {@code int} representation of the version
     */
    public Version(final String semver, final boolean snapshot, final int number) {
        this.semver = semver;
        this.snapshot = snapshot;
        this.number = number;
    }

    /**
     * <p>
     * Builds a new version. Constructs the semver from the {@code int} representation.
     * </p>
     *
     * @param snapshot indicates if the version is a snapshot
     * @param number the {@code int} representation of the version
     */
    public Version(final boolean snapshot, final int number) {
        String strNum = String.valueOf(number);
        strNum = ("000".substring(strNum.length()) + strNum).replace("", ".");
        this.semver = strNum.substring(1, strNum.length() - 1);
        this.snapshot = snapshot;
        this.number = number;
    }

    /**
     * <p>
     * Builds a new version by extracting the MAJOR.MINOR.PATCH pattern eventually followed by a {@code -SNAPSHOT}
     * {@code String}.
     * </p>
     *
     * @param rawVersion the raw version (ex: 1.0.0-SNAPSHOT)
     */
    public Version(final String rawVersion) {
        final int snapshotIdx = rawVersion.indexOf("-SNAPSHOT");

        if (snapshotIdx == -1) {
            snapshot = false;
            semver = rawVersion;
        } else {
            snapshot = true;
            semver = rawVersion.substring(0, snapshotIdx);
        }

        number = Integer.parseInt(semver.replace(".", ""));
    }

    /**
     * <p>
     * Gets the version.
     * </p>
     *
     * @return the version
     */
    public String getSemver() {
        return semver;
    }

    /**
     * <p>
     * Indicates if the version is a snapshot or not.
     * </p>
     *
     * @return {@code true} if the version is a snapshot, {@code false} otherwise
     */
    public boolean isSnapshot() {
        return snapshot;
    }

    /**
     * <p>
     * Gets the {@code int} representation of the version.
     * </p>
     *
     * @return the integer value
     */
    public int getNumber() {
        return number;
    }
}
