function openWin(windowURL, windowName, windowFeatures) {
    mywindow = window.open(windowURL, windowName, windowFeatures);
    mywindow.location.href = windowURL;
    if (mywindow.opener == null) mywindow.opener = self;
    return mywindow;
}
function popOneField(formName, fieldName, fieldValue) {
    if (self) {
        if (self.opener) {
            if (self.opener.document) {
                var form = eval("self.opener.document." + formName);
                if (form) {
                    var field = eval("self.opener.document." + formName + "." + fieldName);
                    if (field) {
                        field.value=fieldValue;
                        field.focus();
                        self.close();
                    }
                }
            }
        }
    }
}
function appendOneField(formName, fieldName, fieldValue) {
    if (self) {
        if (self.opener) {
            if (self.opener.document) {
                var form = eval("self.opener.document." + formName);
                if (form) {
                    var field = eval("self.opener.document." + formName + "." + fieldName);
                    if (field) {
                        currentValue = field.value;
                        currentValue = trim(currentValue);
                        currentValueLength = currentValue.length;
                        if (currentValueLength > 0) {
                            lastCharacter = currentValue.charAt(currentValueLength - 1);
                            if (lastCharacter == ",") {
                                currentValue = currentValue.substring(0, currentValueLength - 1);
                                currentValueLength = currentValue.length;
                            }
                        }
                        if (currentValueLength >0) { 
                            field.value=currentValue + ", " + fieldValue;
                        } else {
                            field.value=fieldValue;
                        }
                        field.focus();
                        self.close();
                    }
                }
            }
        }
    }
}
function appendOneFieldUsingCheckboxes(formName, fieldName, checkboxFormName, checkboxFieldName) {
    if (self) {
        if (self.opener) {
            if (self.opener.document) {
                var form = eval("self.opener.document." + formName);
                if (form) {
                    var field = eval("self.opener.document." + formName + "." + fieldName);
                    if (field) {
                        var checkboxes = eval("document." + checkboxFormName + "." + checkboxFieldName);
                        if (checkboxes) {
                            resultToAppend = "";
                            numberOfCheckboxesChecked = 0;
                            if (checkboxes.length) {
                                len = checkboxes.length;
                                for (i=0; i<len; i++) {
                                     if (checkboxes[i].checked) {
                                         numberOfCheckboxesChecked = numberOfCheckboxesChecked + 1;
                                         if (resultToAppend.length == 0) {
                                             resultToAppend = checkboxes[i].value;
                                         } else {
                                             resultToAppend = resultToAppend + ", " + checkboxes[i].value;
                                        }
                                     }
                                 }
                            } else {
                               if (checkboxes.checked) {
                                    numberOfCheckboxesChecked = numberOfCheckboxesChecked + 1;
                                    if (resultToAppend.length == 0) {
                                        resultToAppend = checkboxes.value;
                                    } else {
                                        resultToAppend = resultToAppend + ", " + checkboxes.value;
                                    }
                                }
                            }
                            if (numberOfCheckboxesChecked == 0) {
                                alert("Please check at least one checkbox.");
                            }
                            if (resultToAppend.length > 0) {
                                appendOneField(formName, fieldName, resultToAppend);
                            }
                        }
                    }
                }
            }
        }
    }
}
