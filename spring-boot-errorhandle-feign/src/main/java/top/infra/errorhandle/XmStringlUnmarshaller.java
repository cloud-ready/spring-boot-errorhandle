package top.infra.errorhandle;

/**
 * If XmlHttpMessageConverter(using XStreamMarshaller) is configured in downstream service,
 * data may be wrapped in&lt;string&gt;&lt;/string&gt;.
 */
public interface XmStringlUnmarshaller {

    String unmarshal(String xml);
}
