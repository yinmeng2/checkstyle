////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code for adherence to a set of rules.
// Copyright (C) 2001-2021 the original author or authors.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
////////////////////////////////////////////////////////////////////////////////

package com.puppycrawl.tools.checkstyle.checks;

import static com.google.common.truth.Truth.assertWithMessage;
import static com.puppycrawl.tools.checkstyle.checks.NewlineAtEndOfFileCheck.MSG_KEY_NO_NEWLINE_EOF;
import static com.puppycrawl.tools.checkstyle.checks.NewlineAtEndOfFileCheck.MSG_KEY_UNABLE_OPEN;
import static com.puppycrawl.tools.checkstyle.checks.NewlineAtEndOfFileCheck.MSG_KEY_WRONG_ENDING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.puppycrawl.tools.checkstyle.AbstractModuleTestSupport;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.Violation;
import com.puppycrawl.tools.checkstyle.internal.utils.TestUtil;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

public class NewlineAtEndOfFileCheckTest
    extends AbstractModuleTestSupport {

    @Override
    protected String getPackageLocation() {
        return "com/puppycrawl/tools/checkstyle/checks/newlineatendoffile";
    }

    @Test
    public void testNewlineLfAtEndOfFile() throws Exception {
        final String[] expected = CommonUtil.EMPTY_STRING_ARRAY;
        verifyWithInlineConfigParser(
                getPath("InputNewlineAtEndOfFileLf.java"),
            expected);
    }

    @Test
    public void testNewlineLfAtEndOfFileLfNotOverlapWithCrLf() throws Exception {
        final String[] expected = {
            "1: " + getCheckMessage(MSG_KEY_WRONG_ENDING),
        };
        verifyWithInlineConfigParser(
                getPath("InputNewlineAtEndOfFileCrlf.java"),
            expected);
    }

    @Test
    public void testNewlineCrlfAtEndOfFile() throws Exception {
        final String[] expected = CommonUtil.EMPTY_STRING_ARRAY;
        verifyWithInlineConfigParser(
                getPath("InputNewlineAtEndOfFileCrlf3.java"),
            expected);
    }

    @Test
    public void testNewlineCrAtEndOfFile() throws Exception {
        final String[] expected = CommonUtil.EMPTY_STRING_ARRAY;
        verifyWithInlineConfigParser(
                getPath("InputNewlineAtEndOfFileCr.java"),
            expected);
    }

    @Test
    public void testAnyNewlineAtEndOfFile() throws Exception {
        final String[] expected = CommonUtil.EMPTY_STRING_ARRAY;
        verifyWithInlineConfigParser(
                getPath("InputNewlineAtEndOfFileCrlf2.java"),
            expected);
        verifyWithInlineConfigParser(
                getPath("InputNewlineAtEndOfFileLf2.java"),
            expected);
        verifyWithInlineConfigParser(
                getPath("InputNewlineAtEndOfFileCr2.java"),
            expected);
    }

    @Test
    public void testNoNewlineLfAtEndOfFile() throws Exception {
        final String[] expected = {
            "1: " + getCheckMessage(MSG_KEY_NO_NEWLINE_EOF),
        };
        verifyWithInlineConfigParser(
                getPath("InputNewlineAtEndOfFileNoNewline.java"),
            expected);
    }

    @Test
    public void testNoNewlineAtEndOfFile() throws Exception {
        final String[] expected = {
            "1: " + getCheckMessage(MSG_KEY_NO_NEWLINE_EOF),
        };
        verifyWithInlineConfigParser(
                getPath("InputNewlineAtEndOfFileNoNewline2.java"),
            expected);
    }

    @Test
    public void testSetLineSeparatorFailure()
            throws Exception {
        final DefaultConfiguration checkConfig =
            createModuleConfig(NewlineAtEndOfFileCheck.class);
        checkConfig.addProperty("lineSeparator", "ct");
        try {
            createChecker(checkConfig);
            fail("exception expected");
        }
        catch (CheckstyleException ex) {
            assertEquals("cannot initialize module com.puppycrawl.tools.checkstyle."
                            + "checks.NewlineAtEndOfFileCheck - "
                            + "Cannot set property 'lineSeparator' to 'ct'",
                    ex.getMessage(), "Error message is unexpected");
        }
    }

    @Test
    public void testEmptyFileFile() throws Exception {
        final DefaultConfiguration checkConfig =
            createModuleConfig(NewlineAtEndOfFileCheck.class);
        checkConfig.addProperty("lineSeparator", LineSeparatorOption.LF.toString());
        final String[] expected = {
            "1: " + getCheckMessage(MSG_KEY_NO_NEWLINE_EOF),
        };
        verify(
            createChecker(checkConfig),
            getPath("InputNewlineAtEndOfFileEmptyFile.txt"),
            expected);
    }

    @Test
    public void testFileWithEmptyLineOnly() throws Exception {
        final DefaultConfiguration checkConfig =
                createModuleConfig(NewlineAtEndOfFileCheck.class);
        checkConfig.addProperty("lineSeparator", LineSeparatorOption.LF.toString());
        final String[] expected = CommonUtil.EMPTY_STRING_ARRAY;
        verify(
                createChecker(checkConfig),
                getPath("InputNewlineAtEndOfFileNewlineAtEnd.txt"),
                expected);
    }

    @Test
    public void testFileWithEmptyLineOnlyWithLfCrCrlf() throws Exception {
        final DefaultConfiguration checkConfig =
                createModuleConfig(NewlineAtEndOfFileCheck.class);
        checkConfig.addProperty("lineSeparator", LineSeparatorOption.LF_CR_CRLF.toString());
        final String[] expected = CommonUtil.EMPTY_STRING_ARRAY;
        verify(
                createChecker(checkConfig),
                getPath("InputNewlineAtEndOfFileNewlineAtEndLf.txt"),
                expected);
    }

    @Test
    public void testWrongFile() throws Exception {
        final DefaultConfiguration checkConfig = createModuleConfig(NewlineAtEndOfFileCheck.class);
        final NewlineAtEndOfFileCheck check = new NewlineAtEndOfFileCheck();
        check.configure(checkConfig);
        final List<String> lines = new ArrayList<>(1);
        lines.add("txt");
        final File impossibleFile = new File("");
        final FileText fileText = new FileText(impossibleFile, lines);
        final Set<Violation> violations = check.process(impossibleFile, fileText);
        assertEquals(1, violations.size(), "Amount of violations is unexpected");
        final Iterator<Violation> iterator = violations.iterator();
        assertEquals(getCheckMessage(MSG_KEY_UNABLE_OPEN, ""), iterator.next().getViolation(),
                "Violation message differs from expected");
    }

    @Test
    public void testWrongSeparatorLength() throws Exception {
        try (RandomAccessFile file =
                     new ReadZeroRandomAccessFile(getPath("InputNewlineAtEndOfFileLf.java"), "r")) {
            TestUtil.invokeMethod(new NewlineAtEndOfFileCheck(), "endsWithNewline", file,
                LineSeparatorOption.LF);
            fail("InvocationTargetException is expected");
        }
        catch (InvocationTargetException ex) {
            assertWithMessage("Error message is unexpected")
                .that(ex)
                    .hasCauseThat()
                        .hasMessageThat()
                        .isEqualTo("Unable to read 1 bytes, got 0");
        }
    }

    private static class ReadZeroRandomAccessFile extends RandomAccessFile {

        /* package */ ReadZeroRandomAccessFile(String name, String mode)
                throws FileNotFoundException {
            super(name, mode);
        }

        @Override
        public int read(byte[] bytes) {
            return 0;
        }
    }

}
