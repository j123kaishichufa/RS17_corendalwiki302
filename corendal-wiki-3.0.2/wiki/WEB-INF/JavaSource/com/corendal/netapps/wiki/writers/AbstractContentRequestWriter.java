/*   
 * Corendal NetApps Wiki - Web application for content management   
 * Copyright (C) Thierry Danard   
 *   
 * This program is free software; you can redistribute it and|or   
 * modify it under the terms of the GNU General Public License   
 * as published by the Free Software Foundation; either version 2   
 * of the License, or (at your option) any later version.   
 *   
 * This program is distributed in the hope that it will be useful,   
 * but WITHOUT ANY WARRANTY; without even the implied warranty of   
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the   
 * GNU General Public License for more details.   
 *   
 * You should have received a copy of the GNU General Public License   
 * along with this program; if not, write to the Free Software   
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.   
 */
package com.corendal.netapps.wiki.writers;

import java.io.PrintWriter;

import com.corendal.netapps.framework.core.dictionaries.IconsDictionary;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.MessageWriter;
import com.corendal.netapps.framework.core.interfaces.StandardBlock;
import com.corendal.netapps.framework.core.interfaces.StandardEntity;
import com.corendal.netapps.framework.core.interfaces.StandardEntityFactory;
import com.corendal.netapps.framework.core.interfaces.StandardIcon;
import com.corendal.netapps.framework.core.interfaces.StandardIconFactory;
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardStyle;
import com.corendal.netapps.framework.core.interfaces.StandardStyleFactory;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardIconFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardStyleFactory;
import com.corendal.netapps.framework.core.writers.AbstractWriter;
import com.corendal.netapps.framework.core.writers.DefaultMessageWriter;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentRequestWriter;
import com.corendal.netapps.wiki.interfaces.StandardContentRequest;

/**
 * AbstractArticleWriter is the abstract class printing content requests.
 * 
 * @version $Id: AbstractContentRequestWriter.java,v 1.1 2005/09/06 21:25:35
 *          tdanard Exp $
 */
public abstract class AbstractContentRequestWriter extends AbstractWriter
        implements Cloneable, ContentRequestWriter {
    /**
     * Default class constructor.
     */
    protected AbstractContentRequestWriter() {
        // parent class constructor is called
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentRequestWriter#printBodyHTML(com.corendal.netapps.wiki.interfaces.StandardContentRequest,
     *      com.corendal.netapps.framework.core.interfaces.StandardBlock,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public void printBodyHTML(StandardContentRequest contentRequest,
            StandardBlock sb) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardIconFactory sif = (StandardIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardIconFactory.class);
        StandardEntityFactory sef = (StandardEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntityFactory.class);
        StandardStyleFactory ssf = (StandardStyleFactory) pfs
                .getStandardBeanFactory(DefaultStandardStyleFactory.class);
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);

        /*
         * get the writers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        MessageWriter mw = (MessageWriter) pws
                .getWriter(DefaultMessageWriter.class);

        /*
         * get the print writer
         */
        PrintWriter out = AnyLogicContextGlobal.get().getPrintWriter();

        /*
         * get the content title style
         */
        PrimaryKey contentTitlePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.StylesDictionary.CONTENT_TITLE);
        StandardStyle contentTitle = (StandardStyle) ssf
                .findByPrimaryKey(contentTitlePk);

        /*
         * get the content body style
         */
        PrimaryKey contentBodyPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.StylesDictionary.CONTENT_BODY);
        StandardStyle contentBody = (StandardStyle) ssf
                .findByPrimaryKey(contentBodyPk);

        /*
         * get the spacer icon
         */
        PrimaryKey spacerPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.SPACER);
        StandardIcon spacer = (StandardIcon) sif.findByPrimaryKey(spacerPk);

        /*
         * print this contentRequest
         */
        if ((contentRequest != null) && (contentRequest.getIsAccessible())) {
            /*
             * start the table
             */
            out
                    .println("<table summary=\"\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
            out.println("<tr>");
            out.print("<td>");

            /*
             * print the spacer
             */
            spacer.printHTMLMaxWidth(3);

            /*
             * end the table
             */
            out.println("</td>");
            out.println("</tr>");
            out.print("</table>");

            /*
             * start the table
             */
            out
                    .println("<table summary=\"\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
            out.println("<tr>");
            out.print("<td class=\"" + contentTitle.getName() + "\">");

            /*
             * print the body
             */
            out.print(contentRequest.getNameHTML());

            /*
             * end the table
             */
            out.println("</td>");
            out.println("</tr>");
            out.print("</table>");

            /*
             * start the table
             */
            out
                    .println("<table summary=\"\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
            out.println("<tr>");
            out.print("<td>");

            /*
             * print the spacer
             */
            spacer.printHTMLMaxWidth(7);

            /*
             * end the table
             */
            out.println("</td>");
            out.println("</tr>");
            out.print("</table>");

            /*
             * start the table
             */
            out
                    .println("<table summary=\"\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
            out.println("<tr>");
            out.print("<td class=\"" + contentBody.getName() + "\">");

            /*
             * print the body
             */
            out.print(contentRequest.getBodyHTML());

            /*
             * end the table
             */
            out.println("</td>");
            out.println("</tr>");
            out.print("</table>");
        } else if (contentRequest != null) {
            /*
             * get the message
             */
            PrimaryKey cannotAccessContentRequestPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.MessagesDictionary.ERR_CANNOT_ACCESS_CONTENT_REQUEST);
            StandardMessage cannotAccessContentRequest = (StandardMessage) smf
                    .findByPrimaryKey(cannotAccessContentRequestPk);

            /*
             * print the message
             */;
            mw.printStartHTML();
            mw.printWithIconHTMLNoTable(cannotAccessContentRequest);
            mw.printEndHTML();
        } else {
            /*
             * set the entity
             */
            PrimaryKey contentRequestsPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENT_REQUESTS);
            StandardEntity contentRequests = (StandardEntity) sef
                    .findByPrimaryKey(contentRequestsPk);

            /*
             * print the message
             */
            mw.printStartHTML();
            mw.printWithIconHTMLNoTable(contentRequests.getNoRecordFound());
            mw.printEndHTML();
        }

    }

}

// end AbstractArticleWriter
