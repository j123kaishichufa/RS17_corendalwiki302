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
import java.util.Date;

import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.dictionaries.ColorsDictionary;
import com.corendal.netapps.framework.core.dictionaries.MessagesDictionary;
import com.corendal.netapps.framework.core.dictionaries.StylesDictionary;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.AccessManager;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.IconWriter;
import com.corendal.netapps.framework.core.interfaces.LinkWriter;
import com.corendal.netapps.framework.core.interfaces.LocaleManager;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.RequestManager;
import com.corendal.netapps.framework.core.interfaces.ResultsBodyCell;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.interfaces.StandardColor;
import com.corendal.netapps.framework.core.interfaces.StandardColorFactory;
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
import com.corendal.netapps.framework.core.managers.DefaultAccessManager;
import com.corendal.netapps.framework.core.managers.DefaultLocaleManager;
import com.corendal.netapps.framework.core.managers.DefaultRequestManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardColorFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardIconFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardStyleFactory;
import com.corendal.netapps.framework.core.utils.AppendUtil;
import com.corendal.netapps.framework.core.utils.ConcatUtil;
import com.corendal.netapps.framework.core.utils.DateFormatUtil;
import com.corendal.netapps.framework.core.utils.DateUtil;
import com.corendal.netapps.framework.core.utils.HTMLFormatUtil;
import com.corendal.netapps.framework.core.utils.TextFormatUtil;
import com.corendal.netapps.framework.core.writers.AbstractWriter;
import com.corendal.netapps.framework.core.writers.DefaultIconWriter;
import com.corendal.netapps.framework.core.writers.DefaultLinkWriter;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.ContentClassificationTypesDictionary;
import com.corendal.netapps.wiki.dictionaries.IconsDictionary;
import com.corendal.netapps.wiki.interfaces.ContentWriter;
import com.corendal.netapps.wiki.interfaces.Searched;
import com.corendal.netapps.wiki.interfaces.StandardContent;
import com.corendal.netapps.wiki.interfaces.StandardContentClassificationType;
import com.corendal.netapps.wiki.interfaces.StandardContentClassificationTypeFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentFactory;
import com.corendal.netapps.wiki.readstandardfactories.DefaultStandardContentClassificationTypeFactory;
import com.corendal.netapps.wiki.utils.ContentUtil;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentFactory;

/**
 * AbstractContentWriter is the abstract class printing content.
 * 
 * @version $Id: AbstractContentWriter.java,v 1.1 2005/09/06 21:25:35 tdanard
 *          Exp $
 */
public abstract class AbstractContentWriter extends AbstractWriter implements
        Cloneable, ContentWriter {
    /** Time gap limit in days for a new or updated content */
    private static final int GENERAL_TIME_GAP_LIMIT = 15;

    /**
     * Time gap limit in days for a new content to show "new" instead of
     * "updated"
     */
    private static final int UPDATED_TIME_GAP_LIMIT = 1;

    /**
     * Default class constructor.
     */
    protected AbstractContentWriter() {
        // parent class constructor is called
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentWriter#printPathHTMLNoLastLink(com.corendal.netapps.wiki.interfaces.Searched,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public void printPathHTMLNoLastLink(Searched sd) {
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
         * get the associated properties icon
         */
        PrimaryKey propertiesIconPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.PROPERTIES);
        StandardIcon propertiesIcon = (StandardIcon) sif
                .findByPrimaryKey(propertiesIconPk);
        String propertiesLinkHTML = lw.getBreakableLinkHTML(sd
                .getPropertiesDefaultLocation(), null, iw
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
        out.println(sd.getPathHTMLNoLastLink());

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
     * @see com.corendal.netapps.wiki.interfaces.ContentWriter#printPathHTML(com.corendal.netapps.wiki.interfaces.Searched,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public void printPathHTML(Searched sd) {
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
         * get the associated properties icon
         */
        PrimaryKey propertiesIconPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.PROPERTIES);
        StandardIcon propertiesIcon = (StandardIcon) sif
                .findByPrimaryKey(propertiesIconPk);
        String propertiesLinkHTML = lw.getBreakableLinkHTML(sd
                .getPropertiesDefaultLocation(), null, iw
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
        out.println(sd.getPathHTMLWithLink());

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

    /**
     * Prints a content inside an article and inside a cell.
     * 
     * @param idCell
     *            cell containing the id of the content to print
     * @param nameCell
     *            cell containing the name of the content to print
     * @param descriptionCell
     *            cell containing the description of the content to print
     * @param addressCell
     *            cell containing the address of the content to print
     * @param createdCell
     *            cell containing the created date of the content to print
     * @param modifiedCell
     *            cell containing the modified date of the content to print
     * @param iconSizeCell
     *            cell containing the icon size of the content to print
     * @param iconFileNameCell
     *            cell containing the icon file name of the content to print
     * @param iconClassificationCell
     *            cell containing the icon classification of the content to
     *            print
     * @param propertiesAddressCell
     *            cell containing the properties address of the content to print
     * @param typeCell
     *            cell containing the type of the content to print
     * @param articleSizeCell
     *            cell containing the article size of the content to print
     * 
     * 
     */
    public void printHTMLNoCell(ResultsBodyCell idCell,
            ResultsBodyCell nameCell, ResultsBodyCell descriptionCell,
            ResultsBodyCell addressCell, ResultsBodyCell createdCell,
            ResultsBodyCell modifiedCell, ResultsBodyCell iconSizeCell,
            ResultsBodyCell iconFileNameCell,
            ResultsBodyCell iconClassificationCell,
            ResultsBodyCell propertiesAddressCell, ResultsBodyCell typeCell,
            ResultsBodyCell articleSizeCell, boolean isLongNameUsed) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardStyleFactory ssf = (StandardStyleFactory) pfs
                .getStandardBeanFactory(DefaultStandardStyleFactory.class);
        StandardIconFactory sif = (StandardIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardIconFactory.class);
        StandardContentClassificationTypeFactory srctf = (StandardContentClassificationTypeFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentClassificationTypeFactory.class);
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        RequestManager rm = (RequestManager) pms
                .getManager(DefaultRequestManager.class);

        /*
         * get the writers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        IconWriter iw = (IconWriter) pws.getWriter(DefaultIconWriter.class);

        /*
         * get the current account
         */
        StandardAccount sa = am.getProxyStandardAccount();

        /*
         * get the print writer
         */
        PrintWriter out = AnyLogicContextGlobal.get().getPrintWriter();

        /*
         * get the term name and the regular text styles
         */
        PrimaryKey termPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(StylesDictionary.TERM_NAME);
        StandardStyle term = (StandardStyle) ssf.findByPrimaryKey(termPk);
        PrimaryKey regularTextPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(StylesDictionary.REGULAR_TEXT);
        StandardStyle regularText = (StandardStyle) ssf
                .findByPrimaryKey(regularTextPk);

        /*
         * get the name and description
         */
        String nameText = nameCell.getValueText();
        String nameHTML = nameCell.getValueHTML();

        if (nameHTML == null) {
            nameHTML = TextFormatUtil.getTextToHTML(nameText);
        }

        String descriptionText = descriptionCell.getValueText();
        String descriptionHTML = descriptionCell.getValueHTML();

        if (descriptionHTML == null) {
            descriptionHTML = TextFormatUtil.getTextToHTML(descriptionText);
        }

        /*
         * add a sign-in message when login is required and not already done
         */
        if (sa == null) {
            /*
             * get the primary key of the "no login required" classification
             */
            PrimaryKey noLoginRequiredPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ContentClassificationTypesDictionary.NO_LOGIN_REQUIRED);

            /*
             * get the classification type
             */
            StandardContentClassificationType classificationType = (StandardContentClassificationType) srctf
                    .findByPrimaryKey(noLoginRequiredPk);

            /*
             * get the name of the no login required classification type
             */
            String noLoginRequiredNameText = classificationType.getNameText();

            /*
             * add the message when login is required
             */
            String classificationValueText = iconClassificationCell
                    .getValueText();

            if ((!(StringUtil.getIsEmpty(classificationValueText)))
                    && (!(classificationValueText
                            .equals(noLoginRequiredNameText)))) {
                PrimaryKey signInRequiredPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.DSP_SIGN_IN_REQUIRED_FOR_LINK);
                StandardMessage signInRequired = (StandardMessage) smf
                        .findByPrimaryKey(signInRequiredPk);
                descriptionText = ConcatUtil
                        .getConcatWithSpace(descriptionText, signInRequired
                                .getCurrentMessageText(), descriptionText,
                                signInRequired.getCurrentMessageText());
                descriptionHTML = ConcatUtil
                        .getConcatWithSpace(descriptionText, signInRequired
                                .getCurrentMessageText(), descriptionHTML,
                                signInRequired.getCurrentMessageHTML());
            }
        }

        /*
         * add a span to the HTML versions
         */
        nameHTML = "<span class=\"" + term.getName() + "\">" + nameHTML
                + "</span>";
        descriptionHTML = "<span class=\"" + regularText.getName() + "\">"
                + descriptionHTML + "</span>";

        /*
         * get the current date
         */
        Date currentDate = DateUtil.getTruncated(new Date());

        /*
         * verify the creation date
         */
        boolean isNew = false;
        boolean isModified = false;
        String created = createdCell.getValueText();
        String modified = modifiedCell.getValueText();
        Date creationDate = null;
        Date modificationDate = null;

        if (!(StringUtil.getIsEmpty(created))) {
            creationDate = DateFormatUtil
                    .getParsedInternalFormattedDate(created);

            if (DateUtil.getTimeGapInDays(creationDate, currentDate) < GENERAL_TIME_GAP_LIMIT) {
                isNew = true;
            }
        }

        if (!(StringUtil.getIsEmpty(modified))) {
            modificationDate = DateFormatUtil
                    .getParsedInternalFormattedDate(modified);

            if (DateUtil.getTimeGapInDays(modificationDate, currentDate) < GENERAL_TIME_GAP_LIMIT) {
                if (creationDate == null) {
                    isModified = true;
                } else {
                    if (DateUtil.getTimeGapInDays(creationDate,
                            modificationDate) >= UPDATED_TIME_GAP_LIMIT) {
                        isModified = true;
                    }
                }
            }
        }

        /*
         * print the term
         */
        String nameAndDescriptionText = ConcatUtil.getConcat(nameText,
                descriptionText, nameHTML, descriptionHTML, ": ", "", " :", "");

        if (isLongNameUsed) {
            String nameAndDescriptionHTML = null;

            if (rm.getIsUserAgentCompatibleWithXHTML()) {
                nameAndDescriptionHTML = ConcatUtil.getConcat(nameText,
                        descriptionText, nameHTML, descriptionHTML,
                        "<span class=\"" + regularText.getName() + "\">"
                                + "<br />" + "</span>", "", "<span class=\""
                                + regularText.getName() + "\">" + "<br />"
                                + "</span>", "");
            } else {
                nameAndDescriptionHTML = ConcatUtil.getConcat(nameText,
                        descriptionText, nameHTML, descriptionHTML,
                        "<span class=\"" + regularText.getName() + "\">"
                                + "<br>" + "</span>", "", "<span class=\""
                                + regularText.getName() + "\">" + "<br>"
                                + "</span>", "");
            }

            out.print(AppendUtil.getAppend(nameAndDescriptionText, ".",
                    nameAndDescriptionHTML, "<span class=\""
                            + regularText.getName() + "\">" + "." + "</span>"));
        } else {
            String nameAndDescriptionHTML = ConcatUtil.getConcat(nameText,
                    descriptionText, nameHTML, descriptionHTML,
                    "<span class=\"" + regularText.getName() + "\">" + ": "
                            + "</span>", "", "<span class=\""
                            + regularText.getName() + "\">" + " :" + "</span>",
                    null);
            out.print(AppendUtil.getAppend(nameAndDescriptionText, ".",
                    nameAndDescriptionHTML, "<span class=\""
                            + regularText.getName() + "\">" + "." + "</span>"));
        }

        /*
         * add the new icon when applicable
         */
        if (isModified) {
            /*
             * get the "updated content" icon
             */
            PrimaryKey updatedContentIconPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.IconsDictionary.UPDATED_RESOURCE);
            StandardIcon updatedContentIcon = (StandardIcon) sif
                    .findByPrimaryKey(updatedContentIconPk);

            /*
             * convert the date into a user readable format
             */
            String iconTitle = DateFormatUtil
                    .getShortFormattedDateText(modificationDate);

            /*
             * associate the creation date
             */
            updatedContentIcon.setTitleText(iconTitle);
            updatedContentIcon.setTitleEncoded(TextFormatUtil
                    .getTextToEncoded(iconTitle));

            /*
             * add this icon
             */
            out.print("<span class=\"" + regularText.getName() + "\"> "
                    + iw.getHTML(updatedContentIcon) + "</span>");
        } else if (isNew) {
            /*
             * get the "new content" icon
             */
            PrimaryKey newContentIconPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.IconsDictionary.NEW_RESOURCE);
            StandardIcon newContentIcon = (StandardIcon) sif
                    .findByPrimaryKey(newContentIconPk);

            /*
             * convert the date into a user readable format
             */
            String iconTitle = DateFormatUtil
                    .getShortFormattedDateText(creationDate);

            /*
             * associate the creation date
             */
            newContentIcon.setTitleText(iconTitle);
            newContentIcon.setTitleEncoded(TextFormatUtil
                    .getTextToEncoded(iconTitle));

            /*
             * add this icon
             */
            out.print("<span class=\"" + regularText.getName() + "\"> "
                    + iw.getHTML(newContentIcon) + "</span>");
        }
    }

    /**
     * Prints a content inside an article.
     * 
     * @param idCell
     *            cell containing the id of the content to print
     * @param nameCell
     *            cell containing the name of the content to print
     * @param descriptionCell
     *            cell containing the description of the content to print
     * @param addressCell
     *            cell containing the address of the content to print
     * @param createdCell
     *            cell containing the created date of the content to print
     * @param modifiedCell
     *            cell containing the modified date of the content to print
     * @param iconSizeCell
     *            cell containing the icon size of the content to print
     * @param iconFileNameCell
     *            cell containing the icon file name of the content to print
     * @param iconClassificationCell
     *            cell containing the icon classification of the content to
     *            print
     * @param propertiesAddressCell
     *            cell containing the properties address of the content to print
     * @param typeCell
     *            cell containing the type of the content to print
     * @param articleSizeCell
     *            cell containing the article size of the content to print
     * 
     * 
     */
    public void printHTML(ResultsBodyCell idCell, ResultsBodyCell nameCell,
            ResultsBodyCell descriptionCell, ResultsBodyCell addressCell,
            ResultsBodyCell createdCell, ResultsBodyCell modifiedCell,
            ResultsBodyCell iconSizeCell, ResultsBodyCell iconFileNameCell,
            ResultsBodyCell iconClassificationCell,
            ResultsBodyCell propertiesAddressCell, ResultsBodyCell typeCell,
            ResultsBodyCell articleSizeCell, boolean isLongNameUsed) {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardContentFactory srf = (StandardContentFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentFactory.class);
        StandardStyleFactory ssf = (StandardStyleFactory) pfs
                .getStandardBeanFactory(DefaultStandardStyleFactory.class);
        StandardIconFactory sif = (StandardIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardIconFactory.class);
        StandardEntityFactory sef = (StandardEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntityFactory.class);

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
         * get the regular text style
         */
        PrimaryKey regularTextPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(StylesDictionary.REGULAR_TEXT);
        StandardStyle regularText = (StandardStyle) ssf
                .findByPrimaryKey(regularTextPk);

        /*
         * get the current locale
         */
        StandardLocale currentLocale = lm.getPrimaryStandardLocale();

        /*
         * get the content
         */
        String contentId = idCell.getValueText();

        /*
         * get the associated preview icon
         */
        StandardContent currentContent = null;
        StandardIcon previewIcon = null;

        if (StringUtil.getIsEmpty(contentId)) {
            PrimaryKey screenIconPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(IconsDictionary.SCREEN);
            previewIcon = (StandardIcon) sif.findByPrimaryKey(screenIconPk);

            PrimaryKey screensPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.EntitiesDictionary.SCREENS);
            StandardEntity screens = (StandardEntity) sef
                    .findByPrimaryKey(screensPk);

            previewIcon.setTitleText(screens.getFirstUpperSingularNameText());
            previewIcon.setTitleEncoded(screens
                    .getFirstUpperSingularNameEncoded());
            previewIcon.setTitleHTML(screens.getFirstUpperSingularNameHTML());
        } else {
            PrimaryKey contentPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(contentId);
            currentContent = (StandardContent) srf.findByPrimaryKey(contentPk);
            previewIcon = ContentUtil.getPreviewIcon(currentContent,
                    iconFileNameCell.getValueText(), articleSizeCell
                            .getValueText(), iconSizeCell.getValueText(),
                    modifiedCell.getValueText());
        }

        /*
         * get the associated properties icon
         */
        String propertiesLinkHTML = null;

        if (currentContent != null) {
            PrimaryKey propertiesIconPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(IconsDictionary.PROPERTIES);
            StandardIcon propertiesIcon = (StandardIcon) sif
                    .findByPrimaryKey(propertiesIconPk);
            propertiesLinkHTML = lw.getBreakableLinkHTML(currentContent
                    .getPropertiesDefaultLocation(), null, iw
                    .getHTML(propertiesIcon), null);
        }

        /*
         * get the print writer
         */
        PrintWriter out = AnyLogicContextGlobal.get().getPrintWriter();

        /*
         * start the cell
         */
        out.print("<td>");

        /*
         * print the anchor
         */
        if (idCell.getRecordNumber() != 0) {
            out.print(lw.getSkipRecordsAnchorHTML(idCell));
        }

        /*
         * print the link
         */
        if (idCell.getSkipToRecordNumber() != 0) {
            out.print(lw.getSkipRecordsLinkHTML(idCell));
        }

        /*
         * start the table
         */
        out
                .println("<table summary=\"\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
        out.println("<tr>");

        /*
         * print the preview
         */
        out.print("<td valign=\"top\">");
        out.print(lw.getBreakableLinkHTML(addressCell.getValueText(), "", iw
                .getHTML(previewIcon), null));
        out.println("</td>");

        /*
         * print a space
         */
        out.print("<td class=\"" + regularText.getName() + "\">");
        out.print(HTMLFormatUtil.getNonBreakableHTMLWithTag(" "));
        out.println("</td>");

        /*
         * print the inside cell and the properties link
         */
        if (currentContent != null) {
            out.print("<td width=\"100%\" valign=\"top\">");
            this.printHTMLNoCell(idCell, nameCell, descriptionCell,
                    addressCell, createdCell, modifiedCell, iconSizeCell,
                    iconFileNameCell, iconClassificationCell,
                    propertiesAddressCell, typeCell, articleSizeCell,
                    isLongNameUsed);
            out.println("</td>");
            out.print("<td align=\"" + currentLocale.getRightAlign()
                    + "\" valign=\"top\">");
            out.print(propertiesLinkHTML);
            out.println("</td>");
        } else {
            out.print("<td width=\"100%\" valign=\"top\" colspan=\"2\">");
            this.printHTMLNoCell(idCell, nameCell, descriptionCell,
                    addressCell, createdCell, modifiedCell, iconSizeCell,
                    iconFileNameCell, iconClassificationCell,
                    propertiesAddressCell, typeCell, articleSizeCell,
                    isLongNameUsed);
            out.println("</td>");
        }

        /*
         * end the table
         */
        out.println("</tr>");
        out.print("</table>");

        /*
         * end the cell
         */
        out.println("</td>");
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ContentWriter#printEmptyHTML(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public void printEmptyHTML() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardIconFactory sif = (StandardIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardIconFactory.class);

        /*
         * get the writers used in this procedure
         */
        WriterSet pws = AnyLogicContextGlobal.get().getWriterSet();
        IconWriter iw = (IconWriter) pws.getWriter(DefaultIconWriter.class);

        /*
         * get the spacer icon
         */
        PrimaryKey spacerPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(com.corendal.netapps.framework.core.dictionaries.IconsDictionary.SPACER);
        StandardIcon spacer = (StandardIcon) sif.findByPrimaryKey(spacerPk);

        /*
         * get the print writer
         */
        PrintWriter out = AnyLogicContextGlobal.get().getPrintWriter();

        /*
         * start the cell
         */
        out.print("<td>");

        /*
         * print a space
         */
        iw.printHTML(spacer, 10, 10);

        /*
         * end the cell
         */
        out.println("</td>");
    }
}

// end AbstractContentWriter
