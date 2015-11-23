package adf.agent.communication.standard.bundle;

import adf.communication.CommunicationMessage;
import adf.communication.MessageBundle;

import java.util.ArrayList;
import java.util.List;

public class StandardMessageBundle extends MessageBundle
{
    @Override
    public List<Class<? extends CommunicationMessage>> getMessageClassList()
    {
        List<Class<? extends CommunicationMessage>> messageClassList = new ArrayList<>();

        messageClassList.add(MessageDummy.class);

        return messageClassList;
    }
}
