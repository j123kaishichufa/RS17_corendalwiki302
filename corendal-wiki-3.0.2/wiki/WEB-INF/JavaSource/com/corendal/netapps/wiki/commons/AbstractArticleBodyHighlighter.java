/*   
 * Corendal NetApps Framework - Java framework for web applications   
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
package com.corendal.netapps.wiki.commons;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.parser.ParserDelegator;

import com.corendal.netapps.framework.configuration.utils.ReplaceUtil;
import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.StandardPage;
import com.corendal.netapps.framework.core.interfaces.StandardPageFactory;
import com.corendal.netapps.framework.core.parsercallbacks.HTMLAnchorParserCallback;
import com.corendal.netapps.framework.core.parsercallbacks.HTMLImageParserCallback;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardPageFactory;
import com.corendal.netapps.framework.core.utils.HTMLFormatUtil;
import com.corendal.netapps.framework.core.utils.HTTPUtil;
import com.corendal.netapps.framework.core.utils.UserSessionUtil;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.constants.HTTPParameterConstants;
import com.corendal.netapps.wiki.dictionaries.ArticlesDictionary;
import com.corendal.netapps.wiki.dictionaries.PagesDictionary;
import com.corendal.netapps.wiki.interfaces.ArticleBodyHighlighter;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.interfaces.StandardImageFactory;
import com.corendal.netapps.wiki.utils.ArticleUtil;
import com.corendal.netapps.wiki.utils.ImageUtil;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageFactory;

/**
 * AbstractBuffer is the abstract class storing output to be printed later in
 * the current page or in another page.
 * 
 * @version $Id: AbstractArticleBodyHighlighter.java,v 1.1 2006/04/23 02:42:26
 *          tdanard Exp $
 */
public abstract class AbstractArticleBodyHighlighter implements Cloneable,
        ArticleBodyHighlighter {
    /** List of original locations */
    private ArrayList<String> originalLocations;

    /** List of modified locations */
    private ArrayList<String> modifiedLocations;

    /** Modified body */
    private String modifiedBodyHTML;

    /**
     * Class constructor.
     */
    public AbstractArticleBodyHighlighter() {
        this.originalLocations = new ArrayList<String>();
        this.modifiedLocations = new ArrayList<String>();
    }

    /**
     * Returns a clone.
     */
    @Override
    public Object clone() {
        try {
            AbstractArticleBodyHighlighter result = (AbstractArticleBodyHighlighter) super
                    .clone();

            if (this.originalLocations != null) {
                result.originalLocations = (ArrayList<String>) this.originalLocations
                        .clone();
            }

            return result;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ArticleBodyHighlighter#getModifiedBodyHTML()
     */
    public String getModifiedBodyHTML() {
        return this.modifiedBodyHTML;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ArticleBodyHighlighter#getOriginalLocations()
     */
    public List<String> getOriginalLocations() {
        return this.originalLocations;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ArticleBodyHighlighter#getModifiedLocations()
     */
    public List<String> getModifiedLocations() {
        return this.modifiedLocations;
    }

    /*
     * @see com.corendal.netapps.wiki.interfaces.ArticleBodyHighlighter#process(com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      java.lang.String,
     *      com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey,
     *      boolean,
     *      com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    public void process(PrimaryKey parentArticlePk, String bodyHTML,
            PrimaryKey homeArticlePk, boolean isOnlineHelp) {
        if (bodyHTML != null) {
            /*
             * get the factories of the instances used in this procedure
             */
            FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
            StandardArticleFactory saf = (StandardArticleFactory) pfs
                    .getStandardBeanFactory(DefaultStandardArticleFactory.class);
            StandardImageFactory sif = (StandardImageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardImageFactory.class);
            StandardPageFactory spf = (StandardPageFactory) pfs
                    .getStandardBeanFactory(DefaultStandardPageFactory.class);

            /*
             * get the home article
             */
            PrimaryKey homePk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ArticlesDictionary.HOME);
            StandardArticle homeArticle = (StandardArticle) saf
                    .findByPrimaryKey(homeArticlePk);

            /*
             * get the article goto page
             */
            PrimaryKey gotoPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(PagesDictionary.GOTO_ARTICLE);
            StandardPage gotoPage = (StandardPage) spf.findByPrimaryKey(gotoPk);

            /*
             * initialize the anchors
             */
            List<Integer> startAnchorTagPositions = new ArrayList<Integer>();
            List<Integer> endAnchorTagPositions = new ArrayList<Integer>();
            List<String> anchorLocations = new ArrayList<String>();
            List<String> originalCompleteAnchorTags = new ArrayList<String>();
            List<String> modifiedCompleteAnchorTags = new ArrayList<String>();

            /*
             * initialize the images
             */
            List<Integer> simpleImageTagPositions = new ArrayList<Integer>();
            List<String> imageLocations = new ArrayList<String>();
            List<String> originalCompleteImageTags = new ArrayList<String>();
            List<String> modifiedCompleteImageTags = new ArrayList<String>();

            /*
             * parse the body looking for anchors
             */
            try {
                /*
                 * parse the text
                 */
                Reader r = new StringReader(bodyHTML);
                ParserDelegator parser = new ParserDelegator();
                HTMLAnchorParserCallback callback = new HTMLAnchorParserCallback();
                parser.parse(r, callback, true);

                /*
                 * return
                 */
                startAnchorTagPositions = callback.getStartTagPositions();
                endAnchorTagPositions = callback.getEndTagPositions();
                anchorLocations = callback.getLocations();
            } catch (IOException e) {
                // nothing to do
            }

            /*
             * parse the body looking for images
             */
            try {
                /*
                 * parse the text
                 */
                Reader r = new StringReader(bodyHTML);
                ParserDelegator parser = new ParserDelegator();
                HTMLImageParserCallback callback = new HTMLImageParserCallback();
                parser.parse(r, callback, true);

                /*
                 * return
                 */
                simpleImageTagPositions = callback.getSimpleTagPositions();
                imageLocations = callback.getLocations();
            } catch (IOException e) {
                // nothing to do
            }

            /*
             * modify all anchors
             */
            for (int i = 0; i < startAnchorTagPositions.size(); i++) {
                Integer currentStartAnchorTagPosition = startAnchorTagPositions
                        .get(i);
                Integer currentEndAnchorTagPosition = endAnchorTagPositions
                        .get(i);
                int currentStartAnchorTagPositionI = currentStartAnchorTagPosition
                        .intValue();
                int currentEndAnchorTagPositionI = currentEndAnchorTagPosition
                        .intValue();

                if (currentEndAnchorTagPositionI > currentStartAnchorTagPositionI) {
                    String currentLocation = anchorLocations.get(i);

                    if (!(StringUtil.getIsEmpty(currentLocation))) {
                        if (!(originalLocations.contains(currentLocation))) {
                            originalLocations.add(currentLocation);
                        }
                        if (ArticleUtil.getIsArticleLocation(currentLocation)) {

                            /*
                             * rebuild the complete a tag (4 is the length of
                             * </a>)
                             */
                            String currentCompleteAnchorTag = bodyHTML
                                    .substring(currentStartAnchorTagPositionI,
                                            currentEndAnchorTagPositionI + 4);

                            /*
                             * verify that this tag was not already met
                             */
                            if (!(originalCompleteAnchorTags
                                    .contains(currentCompleteAnchorTag))) {
                                /*
                                 * get the href as it is entered in the body. do
                                 * not use the current location as it can be
                                 * formatted different ways.
                                 */
                                String actualHref = null;
                                int posStartHref = currentCompleteAnchorTag
                                        .indexOf("href=\"");

                                if (posStartHref >= 0) {
                                    int posEndHref = currentCompleteAnchorTag
                                            .indexOf("\"", posStartHref + 6); // is

                                    // the
                                    // length
                                    // of
                                    // href
                                    if (posEndHref >= 0) {
                                        actualHref = currentCompleteAnchorTag
                                                .substring(posStartHref,
                                                        posEndHref + 1);
                                    }
                                }

                                /*
                                 * find the text between the two tags
                                 */
                                String currentText = null;
                                String currentStartAnchorTagAndText = bodyHTML
                                        .substring(
                                                currentStartAnchorTagPositionI,
                                                currentEndAnchorTagPositionI);
                                int posEndAnchorTag = currentStartAnchorTagAndText
                                        .indexOf(">");

                                if (posEndAnchorTag >= 0) {
                                    currentText = currentStartAnchorTagAndText
                                            .substring(posEndAnchorTag + 1);
                                }

                                /*
                                 * verify that a href and a text were found
                                 */
                                if ((actualHref != null)
                                        && (currentText != null)) {
                                    /*
                                     * find the start tag
                                     */
                                    String currentStartAnchorTag = bodyHTML
                                            .substring(
                                                    currentStartAnchorTagPositionI,
                                                    currentEndAnchorTagPositionI
                                                            - currentText
                                                                    .length());
                                    String currentEndAnchorTag = bodyHTML
                                            .substring(
                                                    currentEndAnchorTagPositionI,
                                                    currentEndAnchorTagPositionI + 4);

                                    /*
                                     * add the complete tag
                                     */
                                    originalCompleteAnchorTags
                                            .add(currentCompleteAnchorTag);

                                    /*
                                     * find the associated article
                                     */
                                    StandardArticle currentArticle = saf
                                            .findByAbsoluteOrRelativeLocation(currentLocation);

                                    if ((currentArticle != null)
                                            && (currentArticle.getIsFound())) {

                                        /*
                                         * wrap the location to include the
                                         * session id if cookies are not used
                                         */
                                        String modifiedLocation = currentLocation;

                                        if ((homeArticlePk.equals(homePk))
                                                || (homeArticle
                                                        .getIsRecursiveDirectParentOf(currentArticle))) {
                                            if (isOnlineHelp) {
                                                modifiedLocation = currentArticle
                                                        .getOnlineHelpDefaultLocation();
                                            } else {
                                                modifiedLocation = currentArticle
                                                        .getDefaultLocation();
                                            }
                                        }

                                        /*
                                         * add the anchor if there was one
                                         */
                                        int posSharp = currentLocation
                                                .indexOf("#");

                                        if (posSharp >= 0) {
                                            String sharpAnchor = currentLocation
                                                    .substring(posSharp);
                                            modifiedLocation = modifiedLocation
                                                    + sharpAnchor;
                                        }

                                        if (!(modifiedLocations
                                                .contains(modifiedLocation))) {
                                            modifiedLocations
                                                    .add(modifiedLocation);
                                        }

                                        /*
                                         * put the modified location
                                         */
                                        UserSessionUtil
                                                .getSessionWrappedURL(modifiedLocation);
                                        modifiedLocation = HTMLFormatUtil
                                                .getTranscribedURL(modifiedLocation);

                                        String modifiedHref = ReplaceUtil
                                                .getReplacedWithMatchCase(
                                                        actualHref,
                                                        currentLocation,
                                                        modifiedLocation);

                                        /*
                                         * add a title
                                         */
                                        modifiedHref = "title=\""
                                                + currentArticle
                                                        .getShortDescriptionEncoded()
                                                + "\" " + modifiedHref;

                                        String modifiedStartAnchorTag = ReplaceUtil
                                                .getReplacedWithMatchCase(
                                                        currentStartAnchorTag,
                                                        actualHref,
                                                        modifiedHref);

                                        /*
                                         * make the link bold
                                         */
                                        String modifiedCompleteAnchorTag = modifiedStartAnchorTag
                                                + "<strong>"
                                                + currentText
                                                + "</strong>"
                                                + currentEndAnchorTag;

                                        /*
                                         * add this modified tag
                                         */
                                        modifiedCompleteAnchorTags
                                                .add(modifiedCompleteAnchorTag);
                                    } else {
                                        /*
                                         * get the new article page
                                         */
                                        PrimaryKey newArticlePk = PrimaryKeyUtil
                                                .getAlphanumericSingleKey(PagesDictionary.NEW_ARTICLE);
                                        StandardPage newArticle = (StandardPage) spf
                                                .findByPrimaryKey(newArticlePk);

                                        /*
                                         * get the friendly address
                                         */
                                        String friendlyAddress = ArticleUtil
                                                .getFriendlyAddressFromLocation(currentLocation);

                                        /*
                                         * build the location of the future page
                                         */
                                        String futureLocation = gotoPage
                                                .getRelativeLocation()
                                                + friendlyAddress + "/";
                                        if (!(modifiedLocations
                                                .contains(futureLocation))) {
                                            modifiedLocations
                                                    .add(futureLocation);
                                        }

                                        /*
                                         * get the new article location
                                         */
                                        String newLocation = newArticle
                                                .getDefaultLocation();
                                        newLocation = HTTPUtil
                                                .getAddedParameterURL(
                                                        newLocation,
                                                        HTTPParameterConstants.ARTICLE_ID,
                                                        parentArticlePk
                                                                .toString());
                                        newLocation = HTTPUtil
                                                .getAddedParameterURL(
                                                        newLocation,
                                                        HTTPParameterConstants.FRIENDLY_ADDRESS,
                                                        friendlyAddress);
                                        newLocation = HTTPUtil
                                                .getAddedParameterURL(
                                                        newLocation,
                                                        HTTPParameterConstants.NAME,
                                                        currentText);
                                        newLocation = UserSessionUtil
                                                .getSessionWrappedURL(newLocation);
                                        newLocation = HTMLFormatUtil
                                                .getTranscribedURL(newLocation);

                                        /*
                                         * change the original location
                                         */
                                        String modifiedHref = "href=\""
                                                + newLocation + "\"";
                                        String modifiedStartAnchorTag = ReplaceUtil
                                                .getReplacedWithMatchCase(
                                                        currentStartAnchorTag,
                                                        actualHref,
                                                        modifiedHref);

                                        /*
                                         * make the link bold
                                         */
                                        String modifiedCompleteAnchorTag = modifiedStartAnchorTag
                                                + "<font color=\"#FF0000\">"
                                                + currentText
                                                + "</font>"
                                                + currentEndAnchorTag;

                                        /*
                                         * add this modified tag
                                         */
                                        modifiedCompleteAnchorTags
                                                .add(modifiedCompleteAnchorTag);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            /*
             * modify all images
             */
            for (int i = 0; i < simpleImageTagPositions.size(); i++) {
                Integer currentSimpleImageTagPosition = simpleImageTagPositions
                        .get(i);
                int currentSimpleImageTagPositionI = currentSimpleImageTagPosition
                        .intValue();
                String currentLocation = imageLocations.get(i);

                if (!(StringUtil.getIsEmpty(currentLocation))) {
                    if (ImageUtil.getIsImageLocation(currentLocation)) {
                        /*
                         * find the end of the current image tag
                         */
                        int currentEndImageTagPositionI = bodyHTML.indexOf(">",
                                currentSimpleImageTagPositionI);

                        /*
                         * rebuild the complete img tag
                         */
                        if (currentEndImageTagPositionI >= 0) {
                            String currentCompleteImageTag = bodyHTML
                                    .substring(currentSimpleImageTagPositionI,
                                            currentEndImageTagPositionI + 1);

                            /*
                             * verify that this tag was not already met
                             */
                            if (!(originalCompleteImageTags
                                    .contains(currentCompleteImageTag))) {
                                /*
                                 * get the src as it is entered in the body. do
                                 * not use the current location as it can be
                                 * formatted different ways.
                                 */
                                String actualSrc = null;
                                int posStartSrc = currentCompleteImageTag
                                        .indexOf("src=\"");

                                if (posStartSrc >= 0) {
                                    int posEndSrc = currentCompleteImageTag
                                            .indexOf("\"", posStartSrc + 5); // is

                                    // the
                                    // length
                                    // of
                                    // src
                                    if (posEndSrc >= 0) {
                                        actualSrc = currentCompleteImageTag
                                                .substring(posStartSrc,
                                                        posEndSrc + 1);
                                    }
                                }

                                /*
                                 * verify that a src was found
                                 */
                                if (actualSrc != null) {
                                    /*
                                     * add the complete tag
                                     */
                                    originalCompleteImageTags
                                            .add(currentCompleteImageTag);

                                    /*
                                     * find the associated image
                                     */
                                    StandardImage currentImage = sif
                                            .findByAbsoluteOrRelativeLocation(currentLocation);

                                    if ((currentImage != null)
                                            && (currentImage.getIsFound())) {
                                        /*
                                         * wrap the location to include the
                                         * session id if cookies are not used
                                         */
                                        String modifiedLocation = null;

                                        if (isOnlineHelp) {
                                            modifiedLocation = currentImage
                                                    .getOnlineHelpDefaultLocation();
                                        } else {
                                            modifiedLocation = currentImage
                                                    .getDefaultLocation();
                                        }

                                        modifiedLocation = UserSessionUtil
                                                .getSessionWrappedURL(modifiedLocation);
                                        modifiedLocation = HTMLFormatUtil
                                                .getTranscribedURL(modifiedLocation);

                                        String modifiedSrc = ReplaceUtil
                                                .getReplacedWithMatchCase(
                                                        actualSrc,
                                                        currentLocation,
                                                        modifiedLocation);

                                        /*
                                         * add a title
                                         */
                                        modifiedSrc = "alt=\""
                                                + currentImage
                                                        .getShortDescriptionEncoded()
                                                + "\" " + modifiedSrc;

                                        String modifiedCompleteImageTag = ReplaceUtil
                                                .getReplacedWithMatchCase(
                                                        currentCompleteImageTag,
                                                        actualSrc, modifiedSrc);

                                        /*
                                         * add this modified tag
                                         */
                                        modifiedCompleteImageTags
                                                .add(modifiedCompleteImageTag);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            /*
             * replace the anchor tags by their new value
             */
            String currentBodyHTML = bodyHTML;

            for (int i = 0; i < modifiedCompleteAnchorTags.size(); i++) {
                String originalCompleteAnchorTag = originalCompleteAnchorTags
                        .get(i);
                String modifiedCompleteAnchorTag = modifiedCompleteAnchorTags
                        .get(i);
                currentBodyHTML = ReplaceUtil.getReplacedWithMatchCase(
                        currentBodyHTML, originalCompleteAnchorTag,
                        modifiedCompleteAnchorTag);
            }

            /*
             * replace the image tags by their new value
             */
            for (int i = 0; i < modifiedCompleteImageTags.size(); i++) {
                String originalCompleteImageTag = originalCompleteImageTags
                        .get(i);
                String modifiedCompleteImageTag = modifiedCompleteImageTags
                        .get(i);
                currentBodyHTML = ReplaceUtil.getReplacedWithMatchCase(
                        currentBodyHTML, originalCompleteImageTag,
                        modifiedCompleteImageTag);
            }

            /*
             * set the result
             */
            this.modifiedBodyHTML = currentBodyHTML;
        } else {
            this.modifiedBodyHTML = null;
        }
    }
}
