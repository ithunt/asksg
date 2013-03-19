package edu.rit.asksg.dataio;

/**
 * Original Author:  Faisal Basra
 * http://www.javaplex.com/blog/using-javamail-api-read-emails-from-gmail/
 */
import edu.rit.asksg.domain.Conversation;
import edu.rit.asksg.domain.config.ProviderConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;

public class InboxReader {



}