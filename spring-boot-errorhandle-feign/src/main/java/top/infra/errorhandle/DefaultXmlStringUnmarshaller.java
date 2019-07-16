package top.infra.errorhandle;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class DefaultXmlStringUnmarshaller implements XmStringlUnmarshaller {

    public static final XmStringlUnmarshaller INSTANCE = new DefaultXmlStringUnmarshaller();

    public static final String XML_TAG_STRING_BEGIN = "<string>";
    public static final String XML_TAG_STRING_END = "</string>";

    @Override
    public String unmarshal(final String xml) {
        final String result;

        if (isNotBlank(xml)) {
            if (xml.startsWith(XML_TAG_STRING_BEGIN) && xml.endsWith(XML_TAG_STRING_END)) {
                result = xml.substring(XML_TAG_STRING_BEGIN.length(), xml.length() - XML_TAG_STRING_END.length());
            } else {
                result = xml;
            }
        } else {
            result = "";
        }

        return result;
    }
}
