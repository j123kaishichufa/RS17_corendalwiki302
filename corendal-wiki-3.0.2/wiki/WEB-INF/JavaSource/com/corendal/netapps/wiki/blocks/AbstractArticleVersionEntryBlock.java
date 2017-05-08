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
package com.corendal.netapps.wiki.blocks;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.corendal.netapps.framework.configuration.utils.StringUtil;
import com.corendal.netapps.framework.core.blocks.AbstractStandardEntryFormBlock;
import com.corendal.netapps.framework.core.constants.CanvasTypeConstants;
import com.corendal.netapps.framework.core.constants.HTTPParameterConstants;
import com.corendal.netapps.framework.core.constants.LayoutTypeConstants;
import com.corendal.netapps.framework.core.dictionaries.IconsDictionary;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormButtonFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormFieldFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardEntryFormLabelFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardFormIconFactory;
import com.corendal.netapps.framework.core.formfactories.DefaultStandardFormMessageFactory;
import com.corendal.netapps.framework.core.globals.AnyLogicContextGlobal;
import com.corendal.netapps.framework.core.interfaces.AccessManager;
import com.corendal.netapps.framework.core.interfaces.FactorySet;
import com.corendal.netapps.framework.core.interfaces.ManagerSet;
import com.corendal.netapps.framework.core.interfaces.PortletOrServletLogicContext;
import com.corendal.netapps.framework.core.interfaces.StandardAccount;
import com.corendal.netapps.framework.core.interfaces.StandardEntity;
import com.corendal.netapps.framework.core.interfaces.StandardEntityFactory;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormButton;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormButtonFactory;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormField;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormFieldFactory;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormLabel;
import com.corendal.netapps.framework.core.interfaces.StandardEntryFormLabelFactory;
import com.corendal.netapps.framework.core.interfaces.StandardFormIcon;
import com.corendal.netapps.framework.core.interfaces.StandardFormIconFactory;
import com.corendal.netapps.framework.core.interfaces.StandardFormMessage;
import com.corendal.netapps.framework.core.interfaces.StandardFormMessageFactory;
import com.corendal.netapps.framework.core.interfaces.StandardLabel;
import com.corendal.netapps.framework.core.interfaces.StandardMessage;
import com.corendal.netapps.framework.core.interfaces.StandardMessageFactory;
import com.corendal.netapps.framework.core.managers.DefaultAccessManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.utils.ConcatUtil;
import com.corendal.netapps.framework.core.utils.DateFormatUtil;
import com.corendal.netapps.framework.core.utils.HTTPUtil;
import com.corendal.netapps.framework.core.utils.MessageUtil;
import com.corendal.netapps.framework.core.utils.TextFormatUtil;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.BlocksDictionary;
import com.corendal.netapps.wiki.dictionaries.ButtonsDictionary;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.FieldsDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentManager;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleVersion;
import com.corendal.netapps.wiki.interfaces.StandardArticleVersionFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentRequest;
import com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory;
import com.corendal.netapps.wiki.managers.DefaultContentManager;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleVersionFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentRequestFactory;

/**
 * AbstractArticleVersionEntryBlock is the parent block common to all reference
 * viewing blocks.
 * 
 * @version $Id: AbstractArticleVersionEntryBlock.java,v 1.1 2005/09/06 21:25:28
 *          tdanard Exp $
 */
public abstract class AbstractArticleVersionEntryBlock extends
        AbstractStandardEntryFormBlock implements Cloneable {
    /** Fully qualified name of this class. */
    private static final String ABSTRACT_CLASS_NAME = "com.corendal.netapps.wiki.blocks.AbstractArticleVersionEntryBlock";

    /** Id of the article versions selected by the user */
    private String[] articleVersionIds;

    /** Article viewed through this form */
    private StandardArticle article;

    /**
     * Default class constructor this procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead
     */
    protected AbstractArticleVersionEntryBlock() {
        this.article = null;
    }

    /**
     * Returns a clone. Overrides AbstractStandardEntryFormBlock.clone.
     */
    @Override
    public Object clone() {
        AbstractArticleVersionEntryBlock result = (AbstractArticleVersionEntryBlock) super
                .clone();

        if (this.article != null) {
            result.article = (StandardArticle) this.article.clone();
        }

        return result;
    }

    /*
     * @see com.corendal.netapps.framework.core.interfaces.StandardBlock#initStandardBlock(com.corendal.netapps.framework.core.interfaces.AnyLogicContext)
     */
    @Override
    public void initStandardBlock() {
        /*
         * init the entry form block
         */
        super.initStandardBlock();

        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardEntityFactory sef = (StandardEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntityFactory.class);

        /*
         * set the entity
         */
        PrimaryKey articleVersionsPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLE_VERSIONS);
        StandardEntity entity = (StandardEntity) sef
                .findByPrimaryKey(articleVersionsPk);
        this.setStandardEntity(entity);
    }

    /**
     * Builds the layout of the form. This procedure is used when removing a
     * article.
     * 
     * 
     * 
     */
    protected void build() {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardEntryFormLabelFactory seflf = (StandardEntryFormLabelFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormLabelFactory.class);
        StandardEntryFormButtonFactory sefbf = (StandardEntryFormButtonFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormButtonFactory.class);
        StandardEntryFormFieldFactory sefff = (StandardEntryFormFieldFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntryFormFieldFactory.class);
        StandardEntityFactory sef = (StandardEntityFactory) pfs
                .getStandardBeanFactory(DefaultStandardEntityFactory.class);
        StandardArticleVersionFactory sdvf = (StandardArticleVersionFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleVersionFactory.class);
        StandardFormMessageFactory sfmf = (StandardFormMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormMessageFactory.class);
        StandardFormIconFactory sfif = (StandardFormIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormIconFactory.class);
        StandardFormIconFactory sficonf = (StandardFormIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormIconFactory.class);
        StandardContentRequestFactory srrf = (StandardContentRequestFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentRequestFactory.class);

        /*
         * get the managers used in this procedure
         */
        ManagerSet pms = AnyLogicContextGlobal.get().getManagerSet();
        AccessManager am = (AccessManager) pms
                .getManager(DefaultAccessManager.class);
        ContentManager resm = (ContentManager) pms
                .getManager(DefaultContentManager.class);

        /*
         * set the entity
         */
        PrimaryKey articleVersionsPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.ARTICLE_VERSIONS);
        StandardEntity entity = (StandardEntity) sef
                .findByPrimaryKey(articleVersionsPk);
        this.setStandardEntity(entity);

        /*
         * get the current account
         */
        StandardAccount sa = am.getProxyStandardAccount();

        /*
         * instantiate all icons
         */
        PrimaryKey spacerPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(IconsDictionary.SPACER);
        StandardFormIcon spacer = (StandardFormIcon) sficonf
                .findByPrimaryKey(spacerPk);
        spacer.setSize(4, 1);

        /*
         * get the list of article versions for the article
         */
        PrimaryKey articlePk = this.article.getPrimaryKey();
        List<StandardArticleVersion> articleVersions = sdvf
                .findEnabledByArticlePrimaryKey(articlePk);
        Collections.reverse(articleVersions);

        /*
         * check whether article versions could be found
         */
        if ((this.article.getIsFound())
                && (this.article.getIsVisible())
                && (!(resm.getIsRecursiveProxyOrAssociateEditor(sa
                        .getPrimaryKey(), this.article.getPrimaryKey())))) {
            /*
             * get the error message
             */
            PrimaryKey cannotRemoveArticleVersionsPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.ERR_CANNOT_REMOVE_ARTICLE_VERSIONS);
            StandardFormMessage cannotRemoveArticleVersionsMessage = (StandardFormMessage) sfmf
                    .findByPrimaryKey(cannotRemoveArticleVersionsPk);

            /*
             * set the max sizes of the form
             */
            this.setMaxSizes(1, 1, CanvasTypeConstants.CENTER);

            /*
             * add those objects
             */
            this.setCurrentCanvasType(CanvasTypeConstants.CENTER);
            this
                    .addStandardFormObject(cannotRemoveArticleVersionsMessage,
                            1, 1);
        } else if ((this.article.getIsFound()) && (this.article.getIsVisible())
                && (articleVersions.size() > 1)) {
            /*
             * set the layout type to multiple to provide a tabbing where the
             * submit button is the last object to reach
             */
            this.setLayoutType(LayoutTypeConstants.BORDER);

            /*
             * set the max sizes of the form
             */
            this.setMaxSizes(4, articleVersions.size(),
                    CanvasTypeConstants.CENTER); // 4 columns, ?
            // lines
            this.setMaxSizes(3, 1, CanvasTypeConstants.SOUTH); // 3

            // columns,
            // 1
            // line

            /*
             * get the primary keys of the required instances
             */
            PrimaryKey entityPk = entity.getPrimaryKey();
            PrimaryKey nameFieldPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(FieldsDictionary.ARTICLE_VERSION);
            PrimaryKey nameLabelPk = seflf.getPrimaryKey(entityPk, nameFieldPk);

            /*
             * instantiate all entry fields
             */
            StandardEntryFormField nameField = (StandardEntryFormField) sefff
                    .findByPrimaryKey(nameFieldPk);
            nameField.setCheckBoxType();

            /*
             * instantiate all entry labels
             */
            StandardEntryFormLabel nameLabel = (StandardEntryFormLabel) seflf
                    .findByPrimaryKey(nameLabelPk);
            nameLabel.setIsNameBreakable(true);

            /*
             * add those objects
             */
            this.setCurrentCanvasType(CanvasTypeConstants.CENTER);

            for (int i = 0; i < articleVersions.size(); i++) {
                /*
                 * get the article
                 */
                StandardArticleVersion currentArticleVersion = (StandardArticleVersion) articleVersions
                        .get(i);
                PrimaryKey currentArticleVersionPk = currentArticleVersion
                        .getPrimaryKey();

                /*
                 * verify that the article version still exists
                 */
                if (currentArticleVersion.getIsFound()) {
                    /*
                     * get the preview icon
                     */
                    PrimaryKey previewIconPk = currentArticleVersion
                            .getDirectPreviewIconPrimaryKey();
                    StandardFormIcon previewFormIcon = (StandardFormIcon) sfif
                            .findByPrimaryKey(previewIconPk);

                    /*
                     * clone the field and modify the field clone
                     */
                    StandardEntryFormField nameFieldClone = (StandardEntryFormField) nameField
                            .clone();
                    nameFieldClone.setOptionValueText(currentArticleVersionPk
                            .toString());

                    /*
                     * clone the label and modify the name of the label
                     */
                    StandardEntryFormLabel nameLabelClone = (StandardEntryFormLabel) nameLabel
                            .clone();
                    StandardLabel sl3 = nameLabelClone.getStandardLabel();

                    /*
                     * get the version number
                     */
                    String versionNumS = sl3.getContextualNameText()
                            + currentArticleVersion.getVersionNum();
                    String bodyText = currentArticleVersion.getBodyText();

                    if (!(StringUtil.getIsEmpty(bodyText))) {
                        String modifiedBodyText = StringUtil
                                .getSubstringWithThreeDots(bodyText, 40);
                        versionNumS = ConcatUtil.getConcatWithColumn(
                                versionNumS, modifiedBodyText, versionNumS,
                                modifiedBodyText);
                    }

                    /*
                     * initialize the short description
                     */
                    String shortDescriptionText = "";

                    /*
                     * add the author
                     */
                    StandardAccount author = currentArticleVersion
                            .getAuthorStandardAccount();

                    if ((author != null) && (author.getIsFound())) {
                        shortDescriptionText = author.getFullNameAndLoginText();
                    }

                    /*
                     * add the created date
                     */
                    PrimaryKey contentRequestPk = currentArticleVersion
                            .getRequestPrimaryKey();

                    if (contentRequestPk != null) {
                        StandardContentRequest request = (StandardContentRequest) srrf
                                .findByPrimaryKey(contentRequestPk);

                        if (request.getIsFound()) {
                            String dateText = DateFormatUtil
                                    .getLongFormattedDateText(request
                                            .getLastEntryLogTimestamp());
                            shortDescriptionText = ConcatUtil
                                    .getConcatWithComma(shortDescriptionText,
                                            dateText, shortDescriptionText,
                                            dateText);
                        }
                    }

                    /*
                     * set the name and short description
                     */
                    sl3.setCurrentNameText(versionNumS);
                    sl3.setCurrentNameEncoded(TextFormatUtil
                            .getTextToEncoded(versionNumS));
                    sl3.setCurrentNameHTML(TextFormatUtil
                            .getTextToHTML(versionNumS));

                    sl3.setCurrentShortDescriptionText(shortDescriptionText);
                    sl3.setCurrentShortDescriptionEncoded(TextFormatUtil
                            .getTextToEncoded(shortDescriptionText));
                    sl3.setCurrentShortDescriptionHTML(TextFormatUtil
                            .getTextToHTML(shortDescriptionText));

                    /*
                     * associate the field with the label
                     */
                    nameLabelClone.associateStandardFormField(nameFieldClone);

                    /*
                     * add these three objects
                     */
                    this.addStandardFormObject(nameFieldClone, 1, i + 1);
                    this.addStandardFormObject(previewFormIcon, 2, i + 1);
                    this.addStandardFormObject(spacer, 3, i + 1);
                    this.addStandardFormObject(nameLabelClone, 4, i + 1);
                }
            }

            /*
             * instantiate and add the submit button
             */
            this.setCurrentCanvasType(CanvasTypeConstants.SOUTH);

            PrimaryKey removeArticleVersionPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ButtonsDictionary.REMOVE_ARTICLE_VERSION);
            StandardEntryFormButton removeArticleVersion = (StandardEntryFormButton) sefbf
                    .findByPrimaryKey(removeArticleVersionPk);
            this.addStandardFormObject(removeArticleVersion, 1, 1);

            /*
             * extract the data entries from the form for validation
             */
            this.articleVersionIds = nameField.getRequestValues();
        } else if ((this.article.getIsFound()) && (this.article.getIsVisible())
                && (articleVersions.size() == 1)) {
            /*
             * get the error message
             */
            PrimaryKey onlyOneArticleVersionPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.ERR_ONLY_ONE_ARTICLE_VERSION);
            StandardFormMessage onlyOneArticleVersionMessage = (StandardFormMessage) sfmf
                    .findByPrimaryKey(onlyOneArticleVersionPk);

            /*
             * set the max sizes of the form
             */
            this.setMaxSizes(1, 1, CanvasTypeConstants.CENTER);

            /*
             * add those objects
             */
            this.setCurrentCanvasType(CanvasTypeConstants.CENTER);
            this.addStandardFormObject(onlyOneArticleVersionMessage, 1, 1);
        } else {
            this.buildNoRecordFound();
        }
    }

    /**
     * Prints the validation specific to this form.
     * 
     * 
     * 
     * @throws IOException
     *             when a redirect occurs
     */
    public void validateExtraRemoveRecord() throws IOException {
        /*
         * get the factories of the instances used in this procedure
         */
        FactorySet pfs = AnyLogicContextGlobal.get().getFactorySet();
        StandardArticleVersionFactory sdvf = (StandardArticleVersionFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleVersionFactory.class);
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);

        /*
         * save the name of the article
         */
        String[] saveArticleVersionsNameText = new String[this.articleVersionIds.length];
        String[] saveArticleVersionsNameEncoded = new String[this.articleVersionIds.length];
        String[] saveArticleVersionsNameHTML = new String[this.articleVersionIds.length];

        /*
         * check if the at least one article version id is checked. if no
         * article id is selected then set the stat to be invalid so no
         * subsequent processings will proceed.
         */
        if (this.articleVersionIds.length == 0) {
            PrimaryKey errorPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.ERR_SELECT_ONE_ARTICLE_VERSION_TO_REMOVE);
            StandardMessage error = (StandardMessage) smf
                    .findByPrimaryKey(errorPk);
            error.printBufferWithIconHTML();

            this.setIsValid(false);
        }

        /*
         * check each article version selected. Validate each article version
         * selected before processing.
         */
        if (this.getIsValid()) {
            for (int i = 0; i < this.articleVersionIds.length; i++) {
                StandardArticleVersion articleVersion = null;
                PrimaryKey articleVersionPk = null;

                /*
                 * verify at least one article version was selected
                 */
                if (this.getIsValid()) {
                    if (StringUtil.getIsEmpty(this.articleVersionIds[i])) {
                        this.setIsValid(false);

                        PrimaryKey errorPk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.ERR_SELECT_ONE_ARTICLE_VERSION_TO_REMOVE);
                        StandardMessage error = (StandardMessage) smf
                                .findByPrimaryKey(errorPk);
                        error.printBufferWithIconHTML();
                    } else {
                        articleVersionPk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(this.articleVersionIds[i]);
                        articleVersion = (StandardArticleVersion) sdvf
                                .findByPrimaryKey(articleVersionPk);

                        if (!(articleVersion.getIsFound())) {
                            this.setIsValid(false);

                            PrimaryKey errorPk = PrimaryKeyUtil
                                    .getAlphanumericSingleKey(MessagesDictionary.ERR_SELECT_ONE_ARTICLE_VERSION_TO_REMOVE);
                            StandardMessage error = (StandardMessage) smf
                                    .findByPrimaryKey(errorPk);
                            error.printBufferWithIconHTML();
                        }
                    }
                }
            }
        }

        /*
         * verify that at least one version remains
         */
        if (this.getIsValid()) {
            List<StandardArticleVersion> articleVersions = sdvf
                    .findEnabledByArticlePrimaryKey(this.article
                            .getPrimaryKey());

            if (articleVersions.size() == this.articleVersionIds.length) {
                this.setIsValid(false);

                PrimaryKey errorPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_LEAVE_AT_LEAST_ONE_ARTICLE_VERSION);
                StandardMessage error = (StandardMessage) smf
                        .findByPrimaryKey(errorPk);
                error.printBufferWithIconHTML();
            }
        }

        /*
         * remove each article version
         */
        if (this.getIsValid()) {
            for (int i = 0; (this.articleVersionIds != null)
                    && (i < this.articleVersionIds.length); i++) {
                if (this.getIsValid()) {
                    /*
                     * get the article version to remove
                     */
                    PrimaryKey articleVersionPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(this.articleVersionIds[i]);
                    StandardArticleVersion articleVersion = (StandardArticleVersion) sdvf
                            .findByPrimaryKey(articleVersionPk);

                    /*
                     * save the name of the article version and save it to be
                     * used in generating browser message
                     */
                    String versionNumS = String.valueOf(articleVersion
                            .getVersionNum());
                    saveArticleVersionsNameText[i] = versionNumS;
                    saveArticleVersionsNameEncoded[i] = TextFormatUtil
                            .getTextToEncoded(versionNumS);
                    saveArticleVersionsNameHTML[i] = TextFormatUtil
                            .getTextToHTML(versionNumS);

                    /*
                     * remove the current article version
                     */
                    articleVersion.remove();
                    this.setIsValid(articleVersion.getIsDone());

                    if (!(articleVersion.getIsDone())) {
                        MessageUtil.printBufferInternalErrorHTML(
                                ABSTRACT_CLASS_NAME, articleVersion
                                        .getStoreTrace());
                    }
                }
            }
        }

        /*
         * create ONE validation message for the browser. If only one article
         * version is selected, print message specific for that article. If more
         * than one article version is selected, print a general message for the
         * processing.
         */
        if (this.getIsValid()) {
            StandardMessage validation = null;
            PrimaryKey validationPk = null;

            /*
             * immediate article removal
             */
            if (this.articleVersionIds.length == 1) { // immediate removal 1

                // article

                /*
                 * get the validation message
                 */
                validationPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.VLD_ARTICLE_VERSION_REMOVED);
                validation = (StandardMessage) smf
                        .findByPrimaryKey(validationPk);

                /*
                 * add the name of the link to the message
                 */
                validation
                        .replaceMessageText(saveArticleVersionsNameText[0], 1);
                validation.replaceMessageEncoded(
                        saveArticleVersionsNameEncoded[0], 1);
                validation
                        .replaceMessageHTML(saveArticleVersionsNameHTML[0], 1);
            } else { // immediate removal of multiple articles

                /*
                 * get the validation message
                 */
                validationPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.VLD_MULTIPLE_ARTICLE_VERSIONS_REMOVED);
                validation = (StandardMessage) smf
                        .findByPrimaryKey(validationPk);

                /*
                 * insert the number (quantity) of article versions removed.
                 */
                String numberOfArticleVersions = String
                        .valueOf(this.articleVersionIds.length);
                validation.replaceMessageText(numberOfArticleVersions, 1);
                validation.replaceMessageEncoded(numberOfArticleVersions, 1);
                validation.replaceMessageHTML(numberOfArticleVersions, 1);
            }

            /*
             * print the message
             */
            validation.printBufferWithIconHTML();
        }

        if (this.getIsValid()) {
            /*
             * redirect to the article properties page for the current article
             */
            String redirectURL = this.article.getPropertiesAbsoluteLocation();
            redirectURL = HTTPUtil.getAddedParameterURL(redirectURL,
                    HTTPParameterConstants.SELECTED_BLOCK_ID,
                    BlocksDictionary.ARTICLE_VERSIONS);
            ((PortletOrServletLogicContext) AnyLogicContextGlobal.get())
                    .commitAndSendAbsoluteRedirect(redirectURL);
        } else {
            AnyLogicContextGlobal.get().rollbackAndStartTransaction();
        }
    }

    /**
     * Builds the layout of the form for a version to remove.
     * 
     * 
     * 
     */
    protected void buildRemoveRecord() {
        if (this.getStandardArticle() != null) {
            this.build();
        } else {
            this.buildNoRecordFound();
        }
    }

    /**
     * Returns the article viewed by this form.
     * 
     * @return a com.corendal.netapps.wiki.interfaces.StandardArticle object
     * @see #setStandardArticle
     */
    public StandardArticle getStandardArticle() {
        return this.article;
    }

    /**
     * Sets the article viewed by this form.
     * 
     * @param sr
     *            standard article viewed by this form
     * @see #getStandardArticle
     */
    public void setStandardArticle(StandardArticle sr) {
        this.article = sr;
    }
}

// end AbstractArticleVersionEntryBlock
