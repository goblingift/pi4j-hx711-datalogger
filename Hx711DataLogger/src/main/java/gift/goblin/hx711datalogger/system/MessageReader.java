/*
 * Copyright (C) 2019 Andre Kessler (https://github.com/goblingift)
 * All rights reserved
 */
package gift.goblin.hx711datalogger.system;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads messages from the attached properties file of this project.
 *
 * @author andre
 */
public class MessageReader {

    private static final String SYSTEM_MESSAGESPROPERTIES = "SystemMessages.properties";
    private static final String NO_MESSAGE_FOUND = "NO MESSAGE FOUND!";
    Properties props;

    public MessageReader() throws IOException {
        props = new Properties();
        String propFileName = SYSTEM_MESSAGESPROPERTIES;

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            props.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }
    }
    
    /**
     * Try to read the message with given key.
     * @param messageKey key as entered in the properties file.
     * @return If no message was found by given key, returns "NO MESSAGE FOUND!".
     */
    public String getMessage(String messageKey) {
        String message = props.getProperty(messageKey, NO_MESSAGE_FOUND);
        return message;
    }
    
    /**
     * Try to read message with given key- then try to replace the found '#' character
     * in that String with the second parameter.
     * @param messageKey key as entered in the properties file. 
     * @param replaceObject object, whom toString() method will be used.
     * @return If no message was found by given key, returns "NO MESSAGE FOUND!".
     */
    public String getMessageAndReplaceHashtag(String messageKey, Object replaceObject) {
        String message = getMessage(messageKey);
        
        if (!message.contains("#")) {
            return NO_MESSAGE_FOUND;
        } else {
            return message.replace("#", replaceObject.toString());
        }
    }
    
}
