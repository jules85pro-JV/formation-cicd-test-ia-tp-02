package com.devops.cicd.user;

public final class EmailValidator {

    private EmailValidator() {}

    public static boolean isValid(String email) {
        if (email == null){ return false; }

        // Règle 1 : Exactement un seul caractère '@'
        int firstAt = email.indexOf('@');
        int lastAt = email.lastIndexOf('@');
        
        // Si pas de '@', ou plusieurs '@', ou '@' au début/fin
        if (firstAt == -1 || firstAt != lastAt || firstAt == 0 || firstAt == email.length() - 1) {
            return false;
        }

        // Règle 2 : Au moins un '.' après le '@'
        String domainPart = email.substring(firstAt + 1);
        int firstDotInDomain = domainPart.indexOf('.');

        // Le point doit exister, ne pas être juste après le @ (ex: a@.com), 
        // et ne pas être le dernier caractère (ex: a@com.)
        return firstDotInDomain > 0 && firstDotInDomain < domainPart.length() - 1;
    }
}