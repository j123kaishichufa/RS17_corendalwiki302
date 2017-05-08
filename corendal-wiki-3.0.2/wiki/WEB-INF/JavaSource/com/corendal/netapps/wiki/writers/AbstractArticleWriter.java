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
import java.util.List;

import com.corendal.netapps.framework.core.dictionaries.IconsDictionary;
import com.corendal.netapps.framework.core.dictionaries.MessagesDictionary;
import com.corendal.netapps.framework.core.dictionaries.StylesDictionary;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.IconWriter;
import com.corendal.netapps.framework.core.interfaces.LinkWriter;
import com.corendal.netapps.framework.core.interfaces.LocaleManager;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.MessageWriter;
import com.corendal.netapps.framework.core.interfaces.StandardBlock;
import com.corendal.netapps.framework.core.interfaces.StandardEntity;
import com.corendal.netapps.framework.core.interfaces.StandardEntityFactory;
import com.corendal.netapps.framework.core.interfaces.StandardIcon;
import com.corendal.netapps.framework.core.interfaces.StandardIconFactory;
import com.corendal.netapps.framework.core.interfaces.StandardLocale;
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardStyle;
import com.corendal.netapps.framework.core.interfaces.StandardStyleFactory;
import com.corendal.netapps.framework.core.interfaces.WriterSet;
import com.corendal.netapps.framework.core.managers.DefaultLocaleManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardIconFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardStyleFactory;
import com.corendal.netapps.framework.core.utils.HTMLFormatUtil;
import com.corendal.netapps.framework.core.writers.AbstractWriter;
import com.corendal.netapps.framework.core.writers.DefaultIconWriter;
import com.corendal.netapps.framework.core.writers.DefaultLinkWriter;
import com.corendal.netapps.framework.core.writers.DefaultMessageWriter;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.interfaces.ArticleWriter;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;

/**
 * AbstractArticleWriter is the abstract class printing articles.
 * 
 * @version $Id: AbstractArticleWriter.java,v 1.1 2005/09/06 21:25:35 tdanard
 *          Exp $
 */
public abstract class AbstractArticleWriter extends AbstractWriter implements
        Cloneable, ArticleWriter {
    /**
     * Default class constructor.
     */
    protected AbstractArticleWriter() {
        // parent class constructor is called
    }

    /**
     * Returns the children of an article in the HTML format.
     * 
     * @param sd
     *            parent article
     * @param isOnlineHelp
     *            indicates whether the article is part of the online help
     * 
     * 
     * @return a StrinBuffer object
     */
    public StringBuilder getChildrenHTML(StandardArticle sd,
            boolean isOnlineHelp) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardIconFactory sif = (StandardIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardIconFactory.class);
        StandardStyleFactory ssf = (StandardStyleFactory) pfs
                .getStandardBeanFactory(DefaultStandardStyleFactory.class);

        /*
         * get the writers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        LinkWriter lw = (LinkWriter) pws.getWriter(DefaultLinkWriter.class);
        IconWriter iw = (IconWriter) pws.getWriter(DefaultIconWriter.class);

        /*
         * initialize the result
         */
        StringBuilder result = new StringBuilder();

        /*
         * get the "regular" style
         */
        PrimaryKey regularPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(StylesDictionary.REGULAR_TEXT);
        StandardStyle regular = (StandardStyle) ssf.findByPrimaryKey(regularPk);

        /*
         * get the spacer icon
         */
        PrimaryKey spacerPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.SPACER);
        StandardIcon spacer = (StandardIcon) sif.findByPrimaryKey(spacerPk);

        /*
         * get the list of direct children
         */
        List<StandardArticle> children = sdf
                .findDirectEnabledByParentArticlePrimaryKey(sd.getPrimaryKey());

        /*
         * print these children
         */
        if (children.size() > 0) {
            /*
             * start the row
             */
            result.append("<tr>");
            result.append("<td>");

            /*
             * start a table
             */
            result
                    .append("<table summary=\"\" cellpadding=\"0\" cellspacing=\"0\">");
            result.append("<tr>");
            result.append("<td>");
            result.append(iw.getHTML(spacer, 10, 10));
            result.append("</td>");
            result.append("<td>");
            result
                    .append("<table summary=\"\" cellpadding=\"0\" cellspacing=\"0\">");

            /*
             * add each child
             */
            for (/*
                     * get the current article
                     */
            StandardArticle currentArticle : children) {

                /*
                 * get the article location
                 */
                String location = null;

                if (isOnlineHelp) {
                    location = currentArticle.getOnlineHelpDefaultLocation();
                } else {
                    location = currentArticle.getDefaultLocation();
                }

                /*
                 * append the link
                 */
                result.append("<tr>");
                result.append("<td class=\"" + regular.getName() + "\">");
                result.append(lw.getNonBreakableLinkHTML(location,
                        currentArticle.getShortDescriptionEncoded(),
                        currentArticle.getNameHTML(), currentArticle
                                .getAccessKeyEncoded()));
                result.append("</td>");
                result.append("</tr>");
            }

            /*
             * end the table
             */
            result.append("</table>");
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");

            /*
             * end the row
             */
            result.append("</td>");
            result.append("</tr>");
        }

        /*
         * return
         */
        return result;
    }

    /**
     * Returns the recursive parent hierarchy of an article in the HTML format.
     * 
     * @param sd
     *            parent article
     * @param homeArticlePk
     *            primary key of the home article
     * @param currentHierarchy
     *            current hierarchy stored as a StringBuilder object
     * @param isOnlineHelp
     *            indicates whether the article is part of the online help
     * 
     * 
     * @return a StrinBuffer object
     */
    public StringBuilder getRecursiveParentHierarchyHTML(StandardArticle sd,
            PrimaryKey homeArticlePk, StringBuilder currentHierarchy,
            boolean isOnlineHelp) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardIconFactory sif = (StandardIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardIconFactory.class);
        StandardStyleFactory ssf = (StandardStyleFactory) pfs
                .getStandardBeanFactory(DefaultStandardStyleFactory.class);

        /*
         * get the writers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        LinkWriter lw = (LinkWriter) pws.getWriter(DefaultLinkWriter.class);
        IconWriter iw = (IconWriter) pws.getWriter(DefaultIconWriter.class);

        /*
         * initialize the result
         */
        StringBuilder result = new StringBuilder();

        /*
         * get the parent article
         */
        PrimaryKey mainParentPrimaryKey = sd.getMainParentPrimaryKey();

        /*
         * verify that the parent article exists
         */
        if ((mainParentPrimaryKey != null)
                && (!(sd.getPrimaryKey().equals(homeArticlePk)))) {
            StandardArticle parentArticle = (StandardArticle) sdf
                    .findByPrimaryKey(mainParentPrimaryKey);

            /*
             * get the "regular" style
             */
            PrimaryKey regularPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(StylesDictionary.REGULAR_TEXT);
            StandardStyle regular = (StandardStyle) ssf
                    .findByPrimaryKey(regularPk);

            /*
             * get the spacer icon
             */
            PrimaryKey spacerPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(IconsDictionary.SPACER);
            StandardIcon spacer = (StandardIcon) sif.findByPrimaryKey(spacerPk);

            /*
             * get the article location
             */
            String location = null;

            if (isOnlineHelp) {
                location = parentArticle.getOnlineHelpDefaultLocation();
            } else {
                location = parentArticle.getDefaultLocation();
            }

            /*
             * append the link
             */
            result.append("<span class=\"" + regular.getName() + "\">");
            result.append(lw.getNonBreakableLinkHTML(location, parentArticle
                    .getShortDescriptionEncoded(), parentArticle.getNameHTML(),
                    parentArticle.getAccessKeyEncoded()));
            result.append("</span>");

            /*
             * append the current hierarchy
             */
            result
                    .append("<table summary=\"\" cellpadding=\"0\" cellspacing=\"0\">");
            result.append("<tr>");
            result.append("<td>");
            result.append(iw.getHTML(spacer, 10, 10));
            result.append("</td>");
            result.append("<td>");
            result.append(currentHierarchy);
            result.append("</td>");
            result.append("</tr>");
            result.append("</table>");

            /*
             * append the parent's hierarchy
             */
            return this.getRecursiveParentHierarchyHTML(parentArticle,
                    homeArticlePk, result, isOnlineHelp);
        } else {
            return currentHierarchy;
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ArticleWriter#printHierarchyHTML(com.corendal.netapps.wiki.interfaces.StandardArticle,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.StandardBlock, boolean,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public void printHierarchyHTML(StandardArticle sd,
            PrimaryKey homeArticlePk, StandardBlock sb, boolean isOnlineHelp) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardIconFactory sif = (StandardIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardIconFactory.class);
        StandardStyleFactory ssf = (StandardStyleFactory) pfs
                .getStandardBeanFactory(DefaultStandardStyleFactory.class);

        /*
         * get the writers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        LinkWriter lw = (LinkWriter) pws.getWriter(DefaultLinkWriter.class);

        /*
         * get the "regular" style
         */
        PrimaryKey regularPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(StylesDictionary.REGULAR_TEXT);
        StandardStyle regular = (StandardStyle) ssf.findByPrimaryKey(regularPk);

        /*
         * get the spacer icon
         */
        PrimaryKey spacerPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.SPACER);
        StandardIcon spacer = (StandardIcon) sif.findByPrimaryKey(spacerPk);

        /*
         * get the home article
         */
        boolean isRecursiveDirectParentOf = false;

        if (homeArticlePk != null) {
            StandardArticle home = (StandardArticle) sdf
                    .findByPrimaryKey(homeArticlePk);
            isRecursiveDirectParentOf = home.getIsRecursiveDirectParentOf(sd);
        }

        /*
         * build the siblings (including article being viewed)
         */
        StringBuilder siblingsHTML = new StringBuilder();
        siblingsHTML
                .append("<table summary=\"\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">");

        if ((homeArticlePk != null)
                && (!(sd.getPrimaryKey().equals(homeArticlePk)))
                && (isRecursiveDirectParentOf)) {
            List<StandardArticle> siblings = sd.getSiblings();

            for (/*
                     * get the current article
                     */
            StandardArticle currentArticle : siblings) {

                /*
                 * get the article location
                 */
                String location = null;

                if (isOnlineHelp) {
                    location = currentArticle.getOnlineHelpDefaultLocation();
                } else {
                    location = currentArticle.getDefaultLocation();
                }

                /*
                 * append the current article
                 */
                siblingsHTML.append("<tr>");
                siblingsHTML.append("<td class=\"" + regular.getName() + "\">");
                siblingsHTML.append(lw.getNonBreakableLinkHTML(location,
                        currentArticle.getShortDescriptionEncoded(),
                        currentArticle.getNameHTML(), currentArticle
                                .getAccessKeyEncoded()));
                siblingsHTML.append("</td>");
                siblingsHTML.append("</tr>");

                /*
                 * append the children of the article
                 */
                if (currentArticle.equals(sd)) {
                    siblingsHTML.append(this.getChildrenHTML(currentArticle,
                            isOnlineHelp));
                }
            }
        } else {
            /*
             * get the article location
             */
            String location = null;

            if (isOnlineHelp) {
                location = sd.getOnlineHelpDefaultLocation();
            } else {
                location = sd.getDefaultLocation();
            }

            /*
             * append the article
             */
            siblingsHTML.append("<tr>");
            siblingsHTML.append("<td class=\"" + regular.getName() + "\">");
            siblingsHTML.append(lw.getNonBreakableLinkHTML(location, sd
                    .getShortDescriptionEncoded(), sd.getNameHTML(), sd
                    .getAccessKeyEncoded()));
            siblingsHTML.append("</td>");
            siblingsHTML.append("</tr>");
            siblingsHTML.append(this.getChildrenHTML(sd, isOnlineHelp));
        }

        siblingsHTML.append("</table>");

        /*
         * append the parent hierarchy
         */
        String hierarchyHTML = null;

        if (isRecursiveDirectParentOf) {
            hierarchyHTML = this.getRecursiveParentHierarchyHTML(sd,
                    homeArticlePk, siblingsHTML, isOnlineHelp).toString();
        } else {
            hierarchyHTML = siblingsHTML.toString();
        }

        /*
         * get the print writer
         */
        PrintWriter out = AnyLogicContextGlobal.get().getPrintWriter();

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
        spacer.printHTMLMaxWidth(5);

        /*
         * end the table
         */
        out.println("</td>");
        out.println("</tr>");
        out.print("</table>");

        /*
         * print the result
         */
        out.println(hierarchyHTML);
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ArticleWriter#printFirstPreviousNextLastHTML(com.corendal.netapps.wiki.interfaces.StandardArticle,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public void printFirstPreviousNextLastHTML(StandardArticle sd) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardStyleFactory ssf = (StandardStyleFactory) pfs
                .getStandardBeanFactory(DefaultStandardStyleFactory.class);
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);

        /*
         * get the writers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        LinkWriter lw = (LinkWriter) pws.getWriter(DefaultLinkWriter.class);

        /*
         * get the styles
         */
        PrimaryKey regularTextPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(StylesDictionary.REGULAR_TEXT);
        StandardStyle regularText = (StandardStyle) ssf
                .findByPrimaryKey(regularTextPk);

        /*
         * get the message
         */
        PrimaryKey firstPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(MessagesDictionary.DSP_FIRST);
        StandardMessage first = (StandardMessage) smf.findByPrimaryKey(firstPk);
        PrimaryKey previousPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(MessagesDictionary.DSP_PREVIOUS);
        StandardMessage previous = (StandardMessage) smf
                .findByPrimaryKey(previousPk);
        PrimaryKey nextPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(MessagesDictionary.DSP_NEXT);
        StandardMessage next = (StandardMessage) smf.findByPrimaryKey(nextPk);
        PrimaryKey lastPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(MessagesDictionary.DSP_LAST);
        StandardMessage last = (StandardMessage) smf.findByPrimaryKey(lastPk);

        /*
         * get the first link
         */
        StandardArticle firstArticle = sd.getFirstSiblingStandardArticle();
        String firstLinkHTML = null;

        if (firstArticle != null) {
            firstLinkHTML = lw.getNonBreakableLinkHTML(firstArticle
                    .getDefaultLocation(), firstArticle.getNameEncoded(), first
                    .getMessageHTML(), null);
        } else {
            firstLinkHTML = HTMLFormatUtil.getNonBreakableHTMLWithTag(first
                    .getMessageHTML());
        }

        /*
         * get the previous link
         */
        StandardArticle previousArticle = sd
                .getPreviousSiblingStandardArticle();
        String previousLinkHTML = null;

        if (previousArticle != null) {
            previousLinkHTML = lw.getNonBreakableLinkHTML(previousArticle
                    .getDefaultLocation(), previousArticle.getNameEncoded(),
                    previous.getMessageHTML(), null);
        } else {
            previousLinkHTML = HTMLFormatUtil
                    .getNonBreakableHTMLWithTag(previous.getMessageHTML());
        }

        /*
         * get the next link
         */
        StandardArticle nextArticle = sd.getNextSiblingStandardArticle();
        String nextLinkHTML = null;

        if (nextArticle != null) {
            nextLinkHTML = lw.getNonBreakableLinkHTML(nextArticle
                    .getDefaultLocation(), nextArticle.getNameEncoded(), next
                    .getMessageHTML(), null);
        } else {
            nextLinkHTML = HTMLFormatUtil.getNonBreakableHTMLWithTag(next
                    .getMessageHTML());
        }

        /*
         * get the last link
         */
        StandardArticle lastArticle = sd.getLastSiblingStandardArticle();
        String lastLinkHTML = null;

        if (lastArticle != null) {
            lastLinkHTML = lw.getNonBreakableLinkHTML(lastArticle
                    .getDefaultLocation(), lastArticle.getNameEncoded(), last
                    .getMessageHTML(), null);
        } else {
            lastLinkHTML = HTMLFormatUtil.getNonBreakableHTMLWithTag(last
                    .getMessageHTML());
        }

        /*
         * get the print writer
         */
        PrintWriter out = AnyLogicContextGlobal.get().getPrintWriter();

        /*
         * start the table
         */
        out
                .println("<table summary=\"\" border=\"0\" cellpadding=\"2\" cellspacing=\"0\">");
        out.println("<tr>");

        /*
         * print the first link
         */
        out.print("<td class=\"" + regularText.getName() + "\">");
        out.print(firstLinkHTML);
        out.println("</td>");

        /*
         * print the previous link
         */
        out.print("<td class=\"" + regularText.getName() + "\">");
        out.print(previousLinkHTML);
        out.println("</td>");

        /*
         * print the next link
         */
        out.print("<td class=\"" + regularText.getName() + "\">");
        out.print(nextLinkHTML);
        out.println("</td>");

        /*
         * print the last link
         */
        out.print("<td class=\"" + regularText.getName() + "\">");
        out.print(lastLinkHTML);
        out.println("</td>");

        /*
         * end the table
         */
        out.println("</tr>");
        out.print("</table>");
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ArticleWriter#printBodyHTMLWithoutName(com.corendal.netapps.wiki.interfaces.StandardArticle,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.StandardBlock, boolean,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public void printBodyHTMLWithoutName(StandardArticle sd,
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
         * print the article
         */
        if (sd != null) {
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
            spacer.printHTMLMaxWidth(5);

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
        spacer.printHTMLMaxWidth(9);

        /*
         * end the table
         */
        out.println("</td>");
        out.println("</tr>");
        out.print("</table>");
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ArticleWriter#printBodyHTML(com.corendal.netapps.wiki.interfaces.StandardArticle,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      com.corendal.netapps.framework.core.interfaces.StandardBlock, boolean,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public void printBodyHTML(StandardArticle sd, PrimaryKey homeArticlePk,
            StandardBlock sb, boolean isOnlineHelp) {
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
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        LocaleManager lm = (LocaleManager) pms
                .getManager(DefaultLocaleManager.class);

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
         * get the current locale
         */
        StandardLocale currentLocale = lm.getPrimaryStandardLocale();

        /*
         * get the spacer icon
         */
        PrimaryKey spacerPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.SPACER);
        StandardIcon spacer = (StandardIcon) sif.findByPrimaryKey(spacerPk);

        /*
         * print the article
         */
        if ((sd != null) && (sd.getIsAccessible())) {
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
             * split the cell
             */
            out.println("</td>");
            out.print("<td align=\"" + currentLocale.getRightAlign() + "\">");

            /*
             * print the navigation
             */
            this.printFirstPreviousNextLastHTML(sd);

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

// end AbstractArticleWriter
