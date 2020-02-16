package com.dknapik.flowershop.constants;

public enum AccountMessage {
    /* Error */
    NEW_ENTITY_CREATION_ERROR("Couldn't create new account, because provided details are not valid"),
    ALREADY_EXISTS("Account with provided details already exists"),
    ENTITY_DETAILS_RETRIEVAL_ERROR("Error, couldn't retrieve currently logged user details"),
    ENTITY_UPDATE_ERROR("Couldn't update account because provided details are not valid"),
    PASSWORD_UPDATE_ERROR(
            "Couldn't update account password because provided new password doesn't match confirmation password"),
    ENTITY_DELETE_ERROR("Couldn't delete account because provided password doesn't match"),
    /* Info */
    ENTITY_CREATION_SUCCESSFUL("Account created successfully !"),
    ENTITY_UPDATE_SUCCESSFUL("Account details updated successfully !"),
    ENTITY_DELETE_SUCCESSFUL("Account deleted succesfully !");

    final String infoMessage;

    AccountMessage(String str) {
        infoMessage = str;
    }

    @Override
    public String toString() {
        return infoMessage;
    }
}
