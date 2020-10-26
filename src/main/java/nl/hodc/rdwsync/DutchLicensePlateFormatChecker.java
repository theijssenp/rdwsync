package nl.hodc.rdwsync;

public class DutchLicensePlateFormatChecker {
    public static boolean matchValid(String platenumber) {
        // Vooralsnog checken we alleen de eerste vier karakters
        String checkValue = platenumber.substring(0, 4);
        boolean returnValue = false;

        // XX-99-99
        if (checkValue.matches("[A-Z][A-Z][0-9][0-9]")) {
            returnValue = true;
        }
        // 99-99-XX
        if (checkValue.matches("[0-9][0-9][0-9][0-9]")) {
            returnValue = true;
        }
        // 99-XX-99
        if (checkValue.matches("[0-9][0-9][A-Z][A-Z]")) {
            returnValue = true;
        }
        // XX-99-XX
        if (checkValue.matches("[A-Z][A-Z][0-9][0-9]")) {
            returnValue = true;
        }
        // XX-XX-99
        if (checkValue.matches("[A-Z][A-Z][A-Z][A-Z]")) {
            returnValue = true;
        }
        // 99-XX-XX
        if (checkValue.matches("[0-9][0-9][A-Z][A-Z]")) {
            returnValue = true;
        }
        // 99-XXX-9
        if (checkValue.matches("[0-9][0-9][A-Z][A-Z]")) {
            returnValue = true;
        }
        // 9-XXX-99
        if (checkValue.matches("[0-9][A-Z][A-Z][A-Z]")) {
            returnValue = true;
        }
        // XX-999-X
        if (checkValue.matches("[A-Z][A-Z][0-9][0-9]")) {
            returnValue = true;
        }
        // X-999-XX
        if (checkValue.matches("[A-Z][0-9][0-9][0-9]")) {
            returnValue = true;
        }
        // XXX-99-X
        if (checkValue.matches("[A-Z][A-Z][A-Z][0-9]")) {
            returnValue = true;
        }
        // Toekomstige combi
        // X-99-XXX
        if (checkValue.matches("[A-Z][0-9][0-9][A-Z]")) {
            returnValue = true;
        }
        // 9-XX-999
        if (checkValue.matches("[0-9][A-Z][A-Z][0-9]")) {
            returnValue = true;
        }
        // 999-XX-9
        if (checkValue.matches("[0-9][0-9][0-9][A-Z]")) {
            returnValue = true;
        }



        // // XX-99-99
        // if (checkValue.matches("[A-Z][A-Z][0-9][0-9][0-9]")) {
        //     returnValue = true;
        // }
        // // 99-99-XX
        // if (checkValue.matches("[0-9][0-9][0-9][0-9][A-Z]")) {
        //     returnValue = true;
        // }
        // // 99-XX-99
        // if (checkValue.matches("[0-9][0-9][A-Z][A-Z][0-9]")) {
        //     returnValue = true;
        // }
        // // XX-99-XX
        // if (checkValue.matches("[A-Z][A-Z][0-9][0-9][A-Z]")) {
        //     returnValue = true;
        // }
        // // XX-XX-99
        // if (checkValue.matches("[A-Z][A-Z][A-Z][A-Z][0-9]")) {
        //     returnValue = true;
        // }
        // // 99-XX-XX
        // if (checkValue.matches("[0-9][0-9][A-Z][A-Z][A-Z]")) {
        //     returnValue = true;
        // }
        // // 99-XXX-9
        // if (checkValue.matches("[0-9][0-9][A-Z][A-Z][A-Z]")) {
        //     returnValue = true;
        // }
        // // 9-XXX-99
        // if (checkValue.matches("[0-9][A-Z][A-Z][A-Z][0-9]")) {
        //     returnValue = true;
        // }
        // // XX-999-X
        // if (checkValue.matches("[A-Z][A-Z][0-9][0-9][0-9]")) {
        //     returnValue = true;
        // }
        // // X-999-XX
        // if (checkValue.matches("[A-Z][0-9][0-9][0-9][A-Z]")) {
        //     returnValue = true;
        // }
        // // XXX-99-X
        // if (checkValue.matches("[A-Z][A-Z][A-Z][0-9][0-9]")) {
        //     returnValue = true;
        // }
        // // Toekomstige combi
        // // X-99-XXX
        // if (checkValue.matches("[A-Z][0-9][0-9][A-Z][A-Z]")) {
        //     returnValue = true;
        // }
        // // 9-XX-999
        // if (checkValue.matches("[0-9][A-Z][A-Z][0-9][0-9]")) {
        //     returnValue = true;
        // }
        // // 999-XX-9
        // if (checkValue.matches("[0-9][0-9][0-9][A-Z][A-Z]")) {
        //     returnValue = true;
        // }
        return returnValue;
    }
}