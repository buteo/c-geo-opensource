package cgeo.geocaching.connector.gc;

import cgeo.geocaching.LogEntry;
import cgeo.geocaching.TrackableLog;
import cgeo.geocaching.cgTrackable;
import cgeo.geocaching.test.AbstractResourceInstrumentationTestCase;
import cgeo.geocaching.test.R;
import cgeo.geocaching.utils.BaseUtils;

import java.util.List;

public class TrackablesTest extends AbstractResourceInstrumentationTestCase {

    public void testLogPageWithTrackables() {
        List<TrackableLog> tbLogs = GCParser.parseTrackableLog(getFileContent(R.raw.log_with_2tb));
        assertNotNull(tbLogs);
        assertEquals(2, tbLogs.size());
        TrackableLog log = tbLogs.get(0);
        assertEquals("Steffen's Kaiserwagen", log.name);
        assertEquals("1QG1EE", log.trackCode);
    }

    public void testLogPageWithoutTrackables() {
        List<TrackableLog> tbLogs = GCParser.parseTrackableLog(getFileContent(R.raw.log_without_tb));
        assertNotNull(tbLogs);
        assertEquals(0, tbLogs.size());
    }

    public void testTrackable() {
        final cgTrackable trackable = getTB2R124();
        assertEquals("TB2R124", trackable.getGeocode());
        assertEquals("Bor. Dortmund - FC Schalke 04", trackable.getName());
        assertEquals("Spiridon Lui", trackable.getOwner());
    }

    public void testTrackableWithoutImage() {
        final cgTrackable trackable = getTB2R124();
        assertNull(trackable.getImage());
        assertNotNull(trackable.getDetails());
    }

    public void testTrackableWithLogImages() {
        final cgTrackable trackable = getTBXATG();
        assertEquals("TBXATG", trackable.getGeocode());

        List<LogEntry> log = trackable.getLogs();
        // second log entry has several images; just check first two
        assertEquals("http://img.geocaching.com/track/log/large/f2e24c50-394c-4d74-8fb4-87298d8bff1d.jpg", log.get(4).getLogImages().get(0).getUrl());
        assertEquals("7b Welcome to Geowoodstock", log.get(4).getLogImages().get(0).getTitle());
        assertEquals("http://img.geocaching.com/track/log/large/b57c29c3-134e-4202-a2a1-69ce8920b055.jpg", log.get(4).getLogImages().get(1).getUrl());
        assertEquals("8 Crater Lake Natl Park Oregon", log.get(4).getLogImages().get(1).getTitle());

        // third log entry has one image
        assertEquals("http://img.geocaching.com/track/log/large/0096b42d-4d10-45fa-9be2-2d08c0d5cc61.jpg", log.get(5).getLogImages().get(0).getUrl());
        assertEquals("Traverski&#39;s GC Univ coin on tour", log.get(5).getLogImages().get(0).getTitle());
    }

    public void testParseTrackableWithoutReleaseDate() {
        cgTrackable trackable = GCParser.parseTrackable(getFileContent(R.raw.tb14wfv), null);
        assertNotNull(trackable);
        assertEquals("The Brickster", trackable.getName());
        assertEquals("Adrian C", trackable.getOwner());
        assertTrue(trackable.getGoal().startsWith("I'm on the run from the law."));
        assertTrue(trackable.getGoal().endsWith("what I've seen."));
        assertTrue(trackable.getDistance() >= 11663.5f);
        // the next two items are normally available for trackables, but not for this one, so explicitly test for null
        assertNull(trackable.getReleased());
        assertNull(trackable.getOrigin());
    }

    public void testParseRelativeLink() {
        final cgTrackable trackable = GCParser.parseTrackable(getFileContent(R.raw.tb4cwjx), null);
        assertNotNull(trackable);
        assertEquals("The Golden Lisa", trackable.getName());
        final String goal = trackable.getGoal();
        assertNotNull(goal);
        assertFalse(goal.contains(".."));
        assertTrue(goal.contains("href=\"http://www.geocaching.com/seek/cache_details.aspx?wp=GC3B7PD#\""));
    }

    private cgTrackable getTB2R124() {
        return GCParser.parseTrackable(BaseUtils.replaceWhitespace(getFileContent(R.raw.trackable_tb2r124)), null);
    }

    private cgTrackable getTBXATG() {
        return GCParser.parseTrackable(BaseUtils.replaceWhitespace(getFileContent(R.raw.trackable_tbxatg)), null);
    }

    public void testParseTrackableNotExisting() {
        cgTrackable trackable = GCParser.parseTrackable(getFileContent(R.raw.tb_not_existing), null);
        assertNull(trackable);
    }

}
