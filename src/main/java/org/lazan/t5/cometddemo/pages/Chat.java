package org.lazan.t5.cometddemo.pages;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.lazan.t5.cometd.services.PushManager;

public class Chat {
    @InjectComponent
    private Zone formZone;

    @Inject
    private Block messageBlock;

    @Property
    private String message;

    @Inject
    private PushManager pushManager;

    // this event is fired when a message is received on the 'chatTopic' topic
    Block onChat(String message) {
        this.message = message;
        return messageBlock;
    }

    // this event is fired when the form is posted
    Block onSuccess() {
        // broadcast the message on the 'chatTopic' topic
        pushManager.broadcast("chatTopic", message);
        return formZone.getBody();
    }
}
