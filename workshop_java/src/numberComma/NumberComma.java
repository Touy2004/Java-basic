package numberComma;

public class NumberComma {

    public static String stringPaserCommafy(String inputNum) {
        String commafiedNum = "";
        Character firstChar = inputNum.charAt(0);

        if (firstChar == '+' || firstChar == '-') {
            commafiedNum = commafiedNum + Character.toString(firstChar);
            inputNum = inputNum.replaceAll("[-\\+]", "");
        }

        String[] splittedNum = inputNum.split("\\.");
        String decimalNum = "";
        if (splittedNum.length == 2) {
            inputNum = splittedNum[0];
            decimalNum = "." + splittedNum[1];
        }

        int numLength = inputNum.length();
        for (int i = 0; i < numLength; i++) {
            if ((numLength - i) % 3 == 0 && i != 0) {
                commafiedNum += ",";
            }
            commafiedNum += inputNum.charAt(i);
        }

        return commafiedNum + decimalNum;
    }
}
