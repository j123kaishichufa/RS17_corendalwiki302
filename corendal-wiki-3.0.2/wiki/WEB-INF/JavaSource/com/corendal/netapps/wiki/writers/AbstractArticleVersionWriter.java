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

import com.corendal.netapps.framework.core.dictionaries.ColorsDictionary;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.IconWriter;
import com.corendal.netapps.framework.core.interfaces.LinkWriter;
import com.corendal.netapps.framework.core.interfaces.LocaleManager;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.MessageWriter;
import com.corendal.netapps.framework.core.interfaces.StandardBlock;
import com.corendal.netapps.framework.core.interfaces.StandardColor;
import com.corendal.netapps.framework.core.interfaces.StandardColorFactory;
import com.corendal.netapps.framework.core.interfaces.StandardEntity;
import com.corendal.netapps.framework.core.interfaces.StandardEntityFactory;
import com.corendal.netapps.framework.core.interfaces.StandardIcon;
import com.corendal.netapps.framework.core.interfaces.StandardIconFactory;
import com.corendal.netapps.framework.core.interfaces.StandardLabel;
import com.corendal.netapps.framework.core.interfaces.StandardLabelFactory;
import com.corendal.netapps.framework.core.interfaces.StandardLocale;
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardStyle;
import com.corendal.netapps.framework.core.interfaces.StandardStyleFactory;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.managers.DefaultLocaleManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardColorFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardIconFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardLabelFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardStyleFactory;
import com.corendal.netapps.framework.core.utils.AppendUtil;
import com.corendal.netapps.framework.core.utils.ConcatUtil;
import com.corendal.netapps.framework.core.writers.AbstractWriter;
import com.corendal.netapps.framework.core.writers.DefaultIconWriter;
import com.corendal.netapps.framework.core.writers.DefaultLinkWriter;
import com.corendal.netapps.framework.core.writers.DefaultMessageWriter;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.FieldsDictionary;
import com.corendal.netapps.wiki.dictionaries.IconsDictionary;
import com.corendal.netapps.wiki.interfaces.ArticleVersionWriter;
import com.corendal.netapps.wiki.interfaces.StandardArticleVersion;

/**
 * AbstractArticleVersionWriter is the abstract class printing article versions.
 * 
 * @version $Id: AbstractArticleVersionWriter.java,v 1.1 2005/09/06 21:25:35
 *          tdanard Exp $
 */
public abstract class AbstractArticleVersionWriter extends AbstractWriter
        implements Cloneable, ArticleVersionWriter {
    /**
     * Default class constructor.
     */
    protected AbstractArticleVersionWriter() {
        // parent class constructor is called
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ArticleVersionWriter#printPathHTML(com.corendal.netapps.wiki.interfaces.StandardArticleVersion,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public void printPathHTML(StandardArticleVersion sd) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardColorFactory scf = (StandardColorFactory) pfs
                .getStandardBeanFactory(DefaultStandardColorFactory.class);
        StandardStyleFactory ssf = (StandardStyleFactory) pfs
                .getStandardBeanFactory(DefaultStandardStyleFactory.class);
        StandardIconFactory sif = (StandardIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardIconFactory.class);
        StandardLabelFactory slf = (StandardLabelFactory) pfs
                .getStandardBeanFactory(DefaultStandardLabelFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        LocaleManager lm = (LocaleManager) pms
                .getManager(DefaultLocaleManager.class);

        /*
         * get the writers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        LinkWriter lw = (LinkWriter) pws.getWriter(DefaultLinkWriter.class);
        IconWriter iw = (IconWriter) pws.getWriter(DefaultIconWriter.class);

        /*
         * get the primary keys of the required instances
         */
        PrimaryKey labelPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(FieldsDictionary.ARTICLE_VERSION);

        /*
         * get the version num label
         */
        StandardLabel versionNumLabel = (StandardLabel) slf
                .findByPrimaryKey(labelPk);

        /*
         * get the version number string
         */
        String versionNumText = versionNumLabel.getContextualNameText()
                + sd.getVersionNum();
        String versionNumHTML = versionNumLabel.getContextualNameHTML()
                + sd.getVersionNum();

        /*
         * get the color of dotted lines
         */
        PrimaryKey dottedLinePk = PrimaryKeyUtil
                .getAlphanumericSingleKey(ColorsDictionary.DOTTED_LINE);
        StandardColor dottedLine = (StandardColor) scf
                .findByPrimaryKey(dottedLinePk);

        /*
         * get the styles
         */
        PrimaryKey pathPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.StylesDictionary.CONTENT_PATH);
        PrimaryKey descriptionPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.StylesDictionary.CONTENT_DESCRIPTION);
        StandardStyle path = (StandardStyle) ssf.findByPrimaryKey(pathPk);
        StandardStyle description = (StandardStyle) ssf
                .findByPrimaryKey(descriptionPk);

        /*
         * get the current locale
         */
        StandardLocale currentLocale = lm.getPrimaryStandardLocale();

        /*
         * get the associated properties image
         */
        PrimaryKey propertiesIconPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.PROPERTIES);
        StandardIcon propertiesIcon = (StandardIcon) sif
                .findByPrimaryKey(propertiesIconPk);
        String propertiesLinkHTML = lw.getBreakableLinkHTML(sd
                .getStandardArticle().getPropertiesDefaultLocation(), "", iw
                .getHTML(propertiesIcon), null);

        /*
         * get the print writer
         */
        PrintWriter out = AnyLogicContextGlobal.get().getPrintWriter();

        /*
         * start the master table
         */
        out
                .println("<table summary=\"\" style=\"border-bottom: 1px dotted "
                        + dottedLine.getColor()
                        + "\" "
                        + "width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
        out.println("<tr>");
        out.print("<td>");

        /*
         * start the table
         */
        out
                .println("<table summary=\"\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
        out.println("<tr>");
        out.print("<td class=\"" + path.getName() + "\">");

        /*
         * print the path
         */
        out.println(ConcatUtil.getConcatWithParentheses(sd.getNameText(),
                versionNumText, sd.getNameHTML(), versionNumHTML));

        /*
         * start a new row
         */
        out.println("</td>");
        out.println("</tr>");
        out.println("<tr>");
        out.print("<td class=\"" + description.getName() + "\">");

        /*
         * print the description
         */
        out.print(AppendUtil.getAppendWithDot(sd.getDescriptionText(), sd
                .getDescriptionHTML()));

        /*
         * end the table
         */
        out.println("</td>");
        out.println("</tr>");
        out.print("</table>");

        /*
         * print the properties link
         */
        out.println("</td>");
        out.print("<td align=\"" + currentLocale.getRightAlign() + "\">");
        out.print(propertiesLinkHTML);

        /*
         * end the master table
         */
        out.println("</td>");
        out.println("</tr>");
        out.print("</table>");
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ArticleVersionWriter#printBodyHTML(com.corendal.netapps.wiki.interfaces.StandardArticleVersion,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.StandardBlock, boolean,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public void printBodyHTML(StandardArticleVersion sd,
            PrimaryKey homeArticlePk, StandardBlock sb, boolean isOnlineHelp) {
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
                .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.IconsDictionary.SPACER);
        StandardIcon spacer = (StandardIcon) sif.findByPrimaryKey(spacerPk);

        /*
         * print this article version
         */
        if ((sd != null) && (sd.getStandardArticle().getIsAccessible())) {
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
            out.print(sd.getNameHTML());

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
            out.print(sd.getBodyHTML(isOnlineHelp, homeArticlePk));

            /*
             * end the table
             */
            out.println("</td>");
            out.println("</tr>");
            out.print("</table>");
        } else if (sd != null) {
            /*
             * get the message
             */
            PrimaryKey cannotAccessArticlePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.wiki.dictionaries.MessagesDictionary.ERR_CANNOT_ACCESS_ARTICLE);
            StandardMessage cannotAccessArticle = (StandardMessage) smf
                    .findByPrimaryKey(cannotAccessArticlePk);

            /*
             * print the message
             */
            mw.printStartHTML();
            mw.printWithIconHTMLNoTable(cannotAccessArticle);
            mw.printEndHTML();
        } else {
            /*
             * set the entity
             */
            PrimaryKey contentsPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(EntitiesDictionary.CONTENTS);
            StandardEntity contents = (StandardEntity) sef
                    .findByPrimaryKey(contentsPk);

            /*
             * print the message
             */
            mw.printStartHTML();
            mw.printWithIconHTMLNoTable(contents.getNoRecordFound());
            mw.printEndHTML();
        }
    }
}

// end AbstractArticleVersionWriter
