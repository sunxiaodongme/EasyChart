package com.example.sunxiaodong.library.base.formatter;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by sunxiaodong on 16/6/25.
 */
public class ValueFormatterHelper {

    public static final int DEFAULT_DIGITS_NUMBER = 0;
    private static final String TAG = "ValueFormatterHelper";
    private int decimalDigitsNumber = Integer.MIN_VALUE;
    private char[] appendedText = new char[0];
    private char[] prependedText = new char[0];
    private char decimalSeparator = '.';

    public void determineDecimalSeparator() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        if (numberFormat instanceof DecimalFormat) {
            decimalSeparator = ((DecimalFormat) numberFormat).getDecimalFormatSymbols().getDecimalSeparator();
        }
    }

    public int getDecimalDigitsNumber() {
        return decimalDigitsNumber;
    }

    public ValueFormatterHelper setDecimalDigitsNumber(int decimalDigitsNumber) {
        this.decimalDigitsNumber = decimalDigitsNumber;
        return this;
    }

    public char[] getAppendedText() {
        return appendedText;
    }

    public ValueFormatterHelper setAppendedText(char[] appendedText) {
        if (null != appendedText) {
            this.appendedText = appendedText;
        }
        return this;
    }

    public char[] getPrependedText() {
        return prependedText;
    }

    public ValueFormatterHelper setPrependedText(char[] prependedText) {
        if (null != prependedText) {
            this.prependedText = prependedText;
        }
        return this;
    }

    public char getDecimalSeparator() {
        return decimalSeparator;
    }

    public ValueFormatterHelper setDecimalSeparator(char decimalSeparator) {
        char nullChar = '\0';
        if (nullChar != decimalSeparator) {
            this.decimalSeparator = decimalSeparator;
        }
        return this;
    }

    /**
     * Formats float value. Result is stored in (output) formattedValue array. Method
     * returns number of chars of formatted value. The formatted value starts at index [formattedValue.length -
     * charsNumber] and ends at index [formattedValue.length-1].
     * Note: If label is not null it will be used as formattedValue instead of float value.
     * Note: Parameter defaultDigitsNumber is used only if you didn't change decimalDigintsNumber value using
     * method {@link #setDecimalDigitsNumber(int)}.
     */
    public int formatFloatValueWithPrependedAndAppendedText(char[] formattedValue, float value, int
            defaultDigitsNumber, char[] label) {
        if (null != label) {
            // If custom label is not null use only name characters as formatted value.
            // Copy label into formatted value array.
            int labelLength = label.length;
            if (labelLength > formattedValue.length) {
                Log.w(TAG, "Label length is larger than buffer size(64chars), some chars will be skipped!");
                labelLength = formattedValue.length;
            }
            System.arraycopy(label, 0, formattedValue, formattedValue.length - labelLength, labelLength);
            return labelLength;
        }

        final int appliedDigitsNumber = getAppliedDecimalDigitsNumber(defaultDigitsNumber);
        final int charsNumber = formatFloatValue(formattedValue, value, appliedDigitsNumber);
        appendText(formattedValue);
        prependText(formattedValue, charsNumber);
        return charsNumber + getPrependedText().length + getAppendedText().length;
    }

    /**
     * @see #formatFloatValueWithPrependedAndAppendedText(char[], float, int, char[])
     */
    public int formatFloatValueWithPrependedAndAppendedText(char[] formattedValue, float value, char[] label) {
        return formatFloatValueWithPrependedAndAppendedText(formattedValue, value, DEFAULT_DIGITS_NUMBER, label);
    }

    /**
     * @see #formatFloatValueWithPrependedAndAppendedText(char[], float, int, char[])
     */
    public int formatFloatValueWithPrependedAndAppendedText(char[] formattedValue, float value, int
            defaultDigitsNumber) {
        return formatFloatValueWithPrependedAndAppendedText(formattedValue, value, defaultDigitsNumber, null);
    }

    public int formatFloatValue(char[] formattedValue, float value, int decimalDigitsNumber) {
        return formatFloat(formattedValue, value, formattedValue.length - appendedText.length,
                decimalDigitsNumber,
                decimalSeparator);
    }

    public void appendText(char[] formattedValue) {
        if (appendedText.length > 0) {
            System.arraycopy(appendedText, 0, formattedValue, formattedValue.length - appendedText.length,
                    appendedText.length);
        }
    }

    public void prependText(char[] formattedValue, int charsNumber) {
        if (prependedText.length > 0) {
            System.arraycopy(prependedText, 0, formattedValue, formattedValue.length - charsNumber - appendedText.length
                    - prependedText.length, prependedText.length);
        }
    }

    public int getAppliedDecimalDigitsNumber(int defaultDigitsNumber) {
        final int appliedDecimalDigitsNumber;
        if (decimalDigitsNumber < 0) {
            //When decimalDigitsNumber < 0 that means that user didn't set that value and defaultDigitsNumber should
            // be used.
            appliedDecimalDigitsNumber = defaultDigitsNumber;
        } else {
            appliedDecimalDigitsNumber = decimalDigitsNumber;
        }
        return appliedDecimalDigitsNumber;
    }

    public static final int POW10[] = {1, 10, 100, 1000, 10000, 100000, 1000000};

    public int formatFloat(final char[] formattedValue, float value, int endIndex, int digits, char separator) {
        if (digits >= POW10.length) {
            formattedValue[endIndex - 1] = '.';
            return 1;
        }
        boolean negative = false;
        if (value == 0) {
            formattedValue[endIndex - 1] = '0';
            return 1;
        }
        if (value < 0) {
            negative = true;
            value = -value;
        }
        if (digits > POW10.length) {
            digits = POW10.length - 1;
        }
        value *= POW10[digits];
        long lval = Math.round(value);
        int index = endIndex - 1;
        int charsNumber = 0;
        while (lval != 0 || charsNumber < (digits + 1)) {
            int digit = (int) (lval % 10);
            lval = lval / 10;
            formattedValue[index--] = (char) (digit + '0');
            charsNumber++;
            if (charsNumber == digits) {
                formattedValue[index--] = separator;
                charsNumber++;
            }
        }
        if (formattedValue[index + 1] == separator) {
            formattedValue[index--] = '0';
            charsNumber++;
        }
        if (negative) {
            formattedValue[index--] = '-';
            charsNumber++;
        }
        return charsNumber;
    }

}
