package top.infra.errorhandle;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DefaultXmlStringUnmarshallerTest {

    @Test
    public void testDefaultXmlStringUnmarshaller() {
        final String content = "content";
        assertEquals("", DefaultXmlStringUnmarshaller.INSTANCE.unmarshal(null));
        assertEquals("", DefaultXmlStringUnmarshaller.INSTANCE.unmarshal(" "));
        assertEquals(content, DefaultXmlStringUnmarshaller.INSTANCE.unmarshal("<string>content</string>"));
        assertEquals(content, DefaultXmlStringUnmarshaller.INSTANCE.unmarshal(content));
    }
}
