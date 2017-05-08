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
import com.corendal.netapps.framework.core.interfaces.StandardStoredFile;
import com.corendal.netapps.framework.core.interfaces.StandardStoredFileFactory;
import com.corendal.netapps.framework.core.managers.DefaultAccessManager;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardEntityFactory;
import com.corendal.netapps.framework.core.readstandardfactories.DefaultStandardMessageFactory;
import com.corendal.netapps.framework.core.utils.ConcatUtil;
import com.corendal.netapps.framework.core.utils.DateFormatUtil;
import com.corendal.netapps.framework.core.utils.HTTPUtil;
import com.corendal.netapps.framework.core.utils.MessageUtil;
import com.corendal.netapps.framework.core.utils.TextFormatUtil;
import com.corendal.netapps.framework.core.writestandardfactories.DefaultStandardStoredFileFactory;
import com.corendal.netapps.framework.modelaccess.interfaces.PrimaryKey;
import com.corendal.netapps.framework.modelaccess.utils.PrimaryKeyUtil;
import com.corendal.netapps.wiki.dictionaries.BlocksDictionary;
import com.corendal.netapps.wiki.dictionaries.ButtonsDictionary;
import com.corendal.netapps.wiki.dictionaries.EntitiesDictionary;
import com.corendal.netapps.wiki.dictionaries.FieldsDictionary;
import com.corendal.netapps.wiki.dictionaries.MessagesDictionary;
import com.corendal.netapps.wiki.interfaces.ContentManager;
import com.corendal.netapps.wiki.interfaces.StandardArticle;
import com.corendal.netapps.wiki.interfaces.StandardArticleFactory;
import com.corendal.netapps.wiki.interfaces.StandardContentRequest;
import com.corendal.netapps.wiki.interfaces.StandardContentRequestFactory;
import com.corendal.netapps.wiki.interfaces.StandardImage;
import com.corendal.netapps.wiki.interfaces.StandardImageVersion;
import com.corendal.netapps.wiki.interfaces.StandardImageVersionFactory;
import com.corendal.netapps.wiki.managers.DefaultContentManager;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardArticleFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardContentRequestFactory;
import com.corendal.netapps.wiki.writestandardfactories.DefaultStandardImageVersionFactory;

/**
 * AbstractImageVersionEntryBlock is the parent block common to all reference
 * viewing blocks.
 * 
 * @version $Id: AbstractImageVersionEntryBlock.java,v 1.1 2005/09/06 21:25:28
 *          tdanard Exp $
 */
public abstract class AbstractImageVersionEntryBlock extends
        AbstractStandardEntryFormBlock implements Cloneable {
    /** Fully qualified name of this class. */
    private static final String ABSTRACT_CLASS_NAME = "com.corendal.netapps.wiki.blocks.AbstractImageVersionEntryBlock";

    /** Id of the image versions selected by the user */
    private String[] imageVersionIds;

    /** Image viewed through this form */
    private StandardImage image;

    /**
     * Default class constructor this procedure is protected to force the usage
     * of the findBy methods. The creation of new instances is too consuming
     * when caching and cloning technics can be used instead
     */
    protected AbstractImageVersionEntryBlock() {
        this.image = null;
    }

    /**
     * Returns a clone. Overrides AbstractStandardEntryFormBlock.clone.
     */
    @Override
    public Object clone() {
        AbstractImageVersionEntryBlock result = (AbstractImageVersionEntryBlock) super
                .clone();

        if (this.image != null) {
            result.image = (StandardImage) this.image.clone();
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
        PrimaryKey imageVersionsPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGE_VERSIONS);
        StandardEntity entity = (StandardEntity) sef
                .findByPrimaryKey(imageVersionsPk);
        this.setStandardEntity(entity);
    }

    /**
     * Builds the layout of the form. This procedure is used when removing a
     * image.
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
        StandardArticleFactory sdf = (StandardArticleFactory) pfs
                .getStandardBeanFactory(DefaultStandardArticleFactory.class);
        StandardImageVersionFactory sdvf = (StandardImageVersionFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageVersionFactory.class);
        StandardFormMessageFactory sfmf = (StandardFormMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormMessageFactory.class);
        StandardFormIconFactory sfif = (StandardFormIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormIconFactory.class);
        StandardFormIconFactory sficonf = (StandardFormIconFactory) pfs
                .getStandardBeanFactory(DefaultStandardFormIconFactory.class);
        StandardContentRequestFactory srrf = (StandardContentRequestFactory) pfs
                .getStandardBeanFactory(DefaultStandardContentRequestFactory.class);
        StandardStoredFileFactory ssff = (StandardStoredFileFactory) pfs
                .getStandardBeanFactory(DefaultStandardStoredFileFactory.class);

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
        PrimaryKey imageVersionsPk = PrimaryKeyUtil
                .getAlphanumericSingleKey(EntitiesDictionary.IMAGE_VERSIONS);
        StandardEntity entity = (StandardEntity) sef
                .findByPrimaryKey(imageVersionsPk);
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
         * get the parent article
         */
        StandardArticle parentArticle = (StandardArticle) sdf
                .findByPrimaryKey(this.image.getMainParentPrimaryKey());

        /*
         * get the list of image versions for the image
         */
        PrimaryKey imagePk = this.image.getPrimaryKey();
        List<StandardImageVersion> imageVersions = sdvf
                .findEnabledByImagePrimaryKey(imagePk);
        Collections.reverse(imageVersions);

        /*
         * check whether image versions could be found
         */
        if ((this.image.getIsFound())
                && (this.image.getIsVisible())
                && (!(resm.getIsRequestRequirementWaived(sa.getPrimaryKey(),
                        parentArticle.getPrimaryKey())))) {
            /*
             * get the error message
             */
            PrimaryKey cannotRemoveImageVersionsPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.ERR_CANNOT_REMOVE_IMAGE_VERSIONS);
            StandardFormMessage cannotRemoveImageVersionsMessage = (StandardFormMessage) sfmf
                    .findByPrimaryKey(cannotRemoveImageVersionsPk);

            /*
             * set the max sizes of the form
             */
            this.setMaxSizes(1, 1, CanvasTypeConstants.CENTER);

            /*
             * add those objects
             */
            this.setCurrentCanvasType(CanvasTypeConstants.CENTER);
            this.addStandardFormObject(cannotRemoveImageVersionsMessage, 1, 1);
        } else if ((this.image.getIsFound()) && (this.image.getIsVisible())
                && (imageVersions.size() > 1)) {
            /*
             * set the layout type to multiple to provide a tabbing where the
             * submit button is the last object to reach
             */
            this.setLayoutType(LayoutTypeConstants.BORDER);

            /*
             * set the max sizes of the form
             */
            this.setMaxSizes(4, imageVersions.size(),
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
                    .getAlphanumericSingleKey(FieldsDictionary.IMAGE_VERSION);
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

            for (int i = 0; i < imageVersions.size(); i++) {
                /*
                 * get the image
                 */
                StandardImageVersion currentImageVersion = (StandardImageVersion) imageVersions
                        .get(i);
                PrimaryKey currentImageVersionPk = currentImageVersion
                        .getPrimaryKey();

                /*
                 * verify that the image version still exists
                 */
                if (currentImageVersion.getIsFound()) {
                    /*
                     * get the preview icon
                     */
                    PrimaryKey previewIconPk = currentImageVersion
                            .getDirectPreviewIconPrimaryKey();
                    StandardFormIcon previewFormIcon = (StandardFormIcon) sfif
                            .findByPrimaryKey(previewIconPk);

                    /*
                     * clone the field and modify the field clone
                     */
                    StandardEntryFormField nameFieldClone = (StandardEntryFormField) nameField
                            .clone();
                    nameFieldClone.setOptionValueText(currentImageVersionPk
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
                            + currentImageVersion.getVersionNum();
                    PrimaryKey filePk = currentImageVersion.getFilePrimaryKey();

                    if (filePk != null) {
                        StandardStoredFile storedFile = (StandardStoredFile) ssff
                                .findByPrimaryKey(filePk);

                        if (storedFile.getIsFound()) {
                            /*
                             * add the file name to the version number
                             */
                            String fileNameText = storedFile.getName();
                            versionNumS = ConcatUtil.getConcatWithColumn(
                                    versionNumS, fileNameText, versionNumS,
                                    fileNameText);
                        }
                    }

                    /*
                     * initialize the short description
                     */
                    String shortDescriptionText = "";

                    /*
                     * add the author
                     */
                    StandardAccount author = currentImageVersion
                            .getAuthorStandardAccount();

                    if ((author != null) && (author.getIsFound())) {
                        shortDescriptionText = author.getFullNameAndLoginText();
                    }

                    /*
                     * add the created date
                     */
                    PrimaryKey contentRequestPk = currentImageVersion
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

            PrimaryKey removeImageVersionPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(ButtonsDictionary.REMOVE_IMAGE_VERSION);
            StandardEntryFormButton removeImageVersion = (StandardEntryFormButton) sefbf
                    .findByPrimaryKey(removeImageVersionPk);
            this.addStandardFormObject(removeImageVersion, 1, 1);

            /*
             * extract the data entries from the form for validation
             */
            this.imageVersionIds = nameField.getRequestValues();
        } else if ((this.image.getIsFound()) && (this.image.getIsVisible())
                && (imageVersions.size() == 1)) {
            /*
             * get the error message
             */
            PrimaryKey onlyOneImageVersionPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.ERR_ONLY_ONE_IMAGE_VERSION);
            StandardFormMessage onlyOneImageVersionMessage = (StandardFormMessage) sfmf
                    .findByPrimaryKey(onlyOneImageVersionPk);

            /*
             * set the max sizes of the form
             */
            this.setMaxSizes(1, 1, CanvasTypeConstants.CENTER);

            /*
             * add those objects
             */
            this.setCurrentCanvasType(CanvasTypeConstants.CENTER);
            this.addStandardFormObject(onlyOneImageVersionMessage, 1, 1);
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
        StandardImageVersionFactory sdvf = (StandardImageVersionFactory) pfs
                .getStandardBeanFactory(DefaultStandardImageVersionFactory.class);
        StandardMessageFactory smf = (StandardMessageFactory) pfs
                .getStandardBeanFactory(DefaultStandardMessageFactory.class);

        /*
         * save the name of the image
         */
        String[] saveImageVersionsNameText = new String[this.imageVersionIds.length];
        String[] saveImageVersionsNameEncoded = new String[this.imageVersionIds.length];
        String[] saveImageVersionsNameHTML = new String[this.imageVersionIds.length];

        /*
         * check if the at least one image version id is checked. if no image id
         * is selected then set the stat to be invalid so no subsequent
         * processings will proceed.
         */
        if (this.imageVersionIds.length == 0) {
            PrimaryKey errorPk = PrimaryKeyUtil
                    .getAlphanumericSingleKey(MessagesDictionary.ERR_SELECT_ONE_IMAGE_VERSION_TO_REMOVE);
            StandardMessage error = (StandardMessage) smf
                    .findByPrimaryKey(errorPk);
            error.printBufferWithIconHTML();

            this.setIsValid(false);
        }

        /*
         * check each image version selected. Validate each image version
         * selected before processing.
         */
        if (this.getIsValid()) {
            for (int i = 0; i < this.imageVersionIds.length; i++) {
                StandardImageVersion imageVersion = null;
                PrimaryKey imageVersionPk = null;

                /*
                 * verify at least one image version was selected
                 */
                if (this.getIsValid()) {
                    if (StringUtil.getIsEmpty(this.imageVersionIds[i])) {
                        this.setIsValid(false);

                        PrimaryKey errorPk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(MessagesDictionary.ERR_SELECT_ONE_IMAGE_VERSION_TO_REMOVE);
                        StandardMessage error = (StandardMessage) smf
                                .findByPrimaryKey(errorPk);
                        error.printBufferWithIconHTML();
                    } else {
                        imageVersionPk = PrimaryKeyUtil
                                .getAlphanumericSingleKey(this.imageVersionIds[i]);
                        imageVersion = (StandardImageVersion) sdvf
                                .findByPrimaryKey(imageVersionPk);

                        if (!(imageVersion.getIsFound())) {
                            this.setIsValid(false);

                            PrimaryKey errorPk = PrimaryKeyUtil
                                    .getAlphanumericSingleKey(MessagesDictionary.ERR_SELECT_ONE_IMAGE_VERSION_TO_REMOVE);
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
            List<StandardImageVersion> imageVersions = sdvf
                    .findEnabledByImagePrimaryKey(this.image.getPrimaryKey());

            if (imageVersions.size() == this.imageVersionIds.length) {
                this.setIsValid(false);

                PrimaryKey errorPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.ERR_LEAVE_AT_LEAST_ONE_IMAGE_VERSION);
                StandardMessage error = (StandardMessage) smf
                        .findByPrimaryKey(errorPk);
                error.printBufferWithIconHTML();
            }
        }

        /*
         * remove each image version
         */
        if (this.getIsValid()) {
            for (int i = 0; (this.imageVersionIds != null)
                    && (i < this.imageVersionIds.length); i++) {
                if (this.getIsValid()) {
                    /*
                     * get the image version to remove
                     */
                    PrimaryKey imageVersionPk = PrimaryKeyUtil
                            .getAlphanumericSingleKey(this.imageVersionIds[i]);
                    StandardImageVersion imageVersion = (StandardImageVersion) sdvf
                            .findByPrimaryKey(imageVersionPk);

                    /*
                     * save the name of the image version and save it to be used
                     * in generating browser message
                     */
                    String versionNumS = String.valueOf(imageVersion
                            .getVersionNum());
                    saveImageVersionsNameText[i] = versionNumS;
                    saveImageVersionsNameEncoded[i] = TextFormatUtil
                            .getTextToEncoded(versionNumS);
                    saveImageVersionsNameHTML[i] = TextFormatUtil
                            .getTextToHTML(versionNumS);

                    /*
                     * remove the current image version
                     */
                    imageVersion.remove();
                    this.setIsValid(imageVersion.getIsDone());

                    if (!(imageVersion.getIsDone())) {
                        MessageUtil.printBufferInternalErrorHTML(
                                ABSTRACT_CLASS_NAME, imageVersion
                                        .getStoreTrace());
                    }
                }
            }
        }

        /*
         * create ONE validation message for the browser. If only one image
         * version is selected, print message specific for that image. If more
         * than one image version is selected, print a general message for the
         * processing.
         */
        if (this.getIsValid()) {
            StandardMessage validation = null;
            PrimaryKey validationPk = null;

            /*
             * immediate image removal
             */
            if (this.imageVersionIds.length == 1) { // immediate removal 1

                // image

                /*
                 * get the validation message
                 */
                validationPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.VLD_IMAGE_VERSION_REMOVED);
                validation = (StandardMessage) smf
                        .findByPrimaryKey(validationPk);

                /*
                 * add the name of the link to the message
                 */
                validation.replaceMessageText(saveImageVersionsNameText[0], 1);
                validation.replaceMessageEncoded(
                        saveImageVersionsNameEncoded[0], 1);
                validation.replaceMessageHTML(saveImageVersionsNameHTML[0], 1);
            } else { // immediate removal of multiple images

                /*
                 * get the validation message
                 */
                validationPk = PrimaryKeyUtil
                        .getAlphanumericSingleKey(MessagesDictionary.VLD_MULTIPLE_IMAGE_VERSIONS_REMOVED);
                validation = (StandardMessage) smf
                        .findByPrimaryKey(validationPk);

                /*
                 * insert the number (quantity) of image versions removed.
                 */
                String numberOfImageVersions = String
                        .valueOf(this.imageVersionIds.length);
                validation.replaceMessageText(numberOfImageVersions, 1);
                validation.replaceMessageEncoded(numberOfImageVersions, 1);
                validation.replaceMessageHTML(numberOfImageVersions, 1);
            }

            /*
             * print the message
             */
            validation.printBufferWithIconHTML();
        }

        if (this.getIsValid()) {
            /*
             * redirect to the image properties page for the current image
             */
            String redirectURL = this.image.getPropertiesAbsoluteLocation();
            redirectURL = HTTPUtil.getAddedParameterURL(redirectURL,
                    HTTPParameterConstants.SELECTED_BLOCK_ID,
                    BlocksDictionary.IMAGE_VERSIONS);
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
        if (this.getStandardImage() != null) {
            this.build();
        } else {
            this.buildNoRecordFound();
        }
    }

    /**
     * Returns the image viewed by this form.
     * 
     * @return a com.corendal.netapps.wiki.interfaces.StandardImage object
     * @see #setStandardImage
     */
    public StandardImage getStandardImage() {
        return this.image;
    }

    /**
     * Sets the image viewed by this form.
     * 
     * @param sr
     *            standard image viewed by this form
     * @see #getStandardImage
     */
    public void setStandardImage(StandardImage sr) {
        this.image = sr;
    }
}

// end AbstractImageVersionEntryBlock
