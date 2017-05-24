package se.peferb.javachat;

import se.peferb.javachat.util.Parser;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class ParserTest {

    private Parser parser;
    private final String exampleData = "DataSnapshot { key = messages, value = {-KkSIEwXQ_WowIuDP1_Z={photoUrl=https://lh4.googleusercontent.com/-Efpj8JKtH0s/AAAAAAAAAAI/AAAAAAAAAAA/AHalGhqwRNWZyBreuNtxuvn_h-LpdLBFcA/s96-c/photo.jpg, text=jag �� , name=Wilhelm Ferb Norlen}} }";

    @Before
    public void setup() {
        parser = new Parser();
    }

    @Test
    public void canParseNameFromSnapshotString() {
        String result = parser.parseName(exampleData);
        String expected = "Wilhelm Ferb Norlen";
        assertEquals(expected, result);
    }

    @Test
    public void canParseMessageFromSnapshotString() {
        String result = parser.parseMessage(exampleData);
        String expected = "jag �� ";
        assertEquals(expected, result);
    }

    @Test
    public void canParseIdFromSnapshotString() {
        String result = parser.parseId(exampleData);
        String expected = "-KkSIEwXQ_WowIuDP1_Z";
        assertEquals(expected, result);
    }

    @Test
    public void parseIdFromBrokenSnapshotStringShouldReturnEmptyString() {
        String result = parser.parseId("broken data string");
        assertTrue(result.isEmpty());
    }
}
