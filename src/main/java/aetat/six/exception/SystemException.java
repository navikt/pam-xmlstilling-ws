package aetat.six.exception;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("squid:S1948")
public class SystemException extends RuntimeException {

    private static final Logger LOG = LoggerFactory.getLogger(SystemException.class);

    private String eventMessage;
    private Object[] eventArgs;

    public SystemException(String eventMessage, Throwable cause) {
        this(eventMessage, null, cause);
    }

    public SystemException(String msg) {
        this(msg, null);
    }

    public SystemException(String eventMessage, Object[] eventArgs, Throwable cause) {
        super(eventMessage, cause);
        this.eventMessage = eventMessage;
        this.eventArgs = eventArgs;

        if (LOG.isInfoEnabled()) {
            if (null != cause) {
                LOG.info(getMessage(), ExceptionUtils.getRootCause(cause));
            } else {
                LOG.info(getMessage());
            }
        }
    }

    @Override
    public String getMessage() {
        StringBuilder buf = new StringBuilder();
        buf.append(eventMessage);
        buf.append(" - ");
        buf.append(joinArguments());
        return buf.toString();
    }

    private String joinArguments() {
        if (null == eventArgs) {
            return "no arguments";
        }
        
        return StringUtils.join(eventArgs, ", ");
    }
}
