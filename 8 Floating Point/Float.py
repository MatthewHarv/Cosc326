"""""
COSC326, Etude 8: Floating Point

Reads a file of IBM System/360-format floating point numbers
and writes them to a new file in IEEE standard format.
 
Author: Gus, James, Matthew, Theo
"""""

import struct
import sys
import math

"""""
Calculates the floating point components of the IBM floating point.

Single precision

Parameters:
- ibm32: The 32 bit IBM floating point integer.

Returns:
- The floating point value.
"""""


def convertIBM32(ibm32):
    # Retrieve the sign
    sign = (ibm32 >> 31)

    # Retrieve the exponent
    binExponent = bin((ibm32 >> 23))
    if sign == 1:
        string_binExponent = "0b" + binExponent[3:10]
    else:
        string_binExponent = "0b" + binExponent[2:9]

    # Retrieve the fraction
    binFraction = bin(ibm32)
    if sign == 1:
        string_binFraction = "0b" + binFraction[10:]
    else:
        string_binFraction = "0b" + binFraction[9:]

    exponent = int(bin(int(string_binExponent, 2)), 2)

    # IEEE uses 2**exponent, so it converts the exponent for IEEE format
    exponent -= 64
    exponent *= 4

    return makeIEEE32(sign, exponent, string_binFraction)


"""""
Takes the IBM components and transforms them to IEEE format.

Single Precision.

Parameters:
- sign The sign value of the floating point.
- exponent The exponent value of the floating point.
- string_fraction The binary string of the fraction component
of the floating point.

Returns:
- The floating point value.
"""""


def makeIEEE32(sign, exponent, string_fraction):
    # Floating point is 0
    if exponent < 0:
        return 0
    exponent += 127  # The IEEE exponent
    # Gets the mantissa by normalising the fraction
    temp = string_fraction
    string_mantissa = "0." + temp[2:]
    # Normalising the fraction
    while string_mantissa[0] != "1" and exponent > 0:
        string_mantissa = string_mantissa[2] + "." + string_mantissa[3:] + "0"
        exponent -= 1

    # The floating point is normal, otherwise it is zero
    if exponent > 0:
        true_fraction = getValue(string_mantissa)  # The value of the mantissa
        return convertIEEE32(sign, exponent, true_fraction)
    else:
        return 0


"""""
Calculates the floating point value from the IEEE components.

Single precision.

Parameters:
- sign The sign value for the floating point.
- exponent The exponent value for the floating point.
- fraction The mantissa value for the floating point.

Returns:
- The value of the floating point.
"""""


def convertIEEE32(sign, exponent, fraction):
    if exponent == 255 and fraction != 0:
        # NaN
        return 0
    elif exponent > 255:
        # infinite
        return (-1)**sign * math.inf
    elif 0 < exponent < 255:
        # normal
        return (-1) ** sign * 2 ** (exponent - 127) * fraction
    else:
        return 0


"""""
Calculates the floating point components of the IBM floating point.

Double precision

Parameters:
- ibm64: The 64 bit IBM floating point integer.

Returns:
- The floating point value.
"""""


def convertIBM64(ibm64):
    # Retrieve sign
    sign = (ibm64 >> 63)

    # Retrieve exponent
    binExponent = bin((ibm64 >> 56))
    if sign == 1:
        string_binExponent = "0b" + binExponent[3:10]
    else:
        string_binExponent = "0b" + binExponent[2:9]

    # Retrieve fraction
    binFraction = bin(ibm64)
    if sign == 1:
        string_binFraction = "0b" + binFraction[10:]
    else:
        string_binFraction = "0b" + binFraction[9:]

    exponent = int(bin(int(string_binExponent, 2)), 2)

    return makeIEEE64(sign, exponent, string_binFraction)


"""""
Takes the IBM components and transforms them to IEEE format.

Double Precision.

Parameters:
- sign The sign value of the floating point.
- exponent The exponent value of the floating point.
- string_fraction The binary string of the fraction component
of the floating point.

Returns:
- The floating point value.
"""""


def makeIEEE64(sign, exponent, string_fraction):
    # Checks if exponent is infinite
    string_exponent = str(bin(exponent)) + string_fraction[2:6]
    if int(string_exponent, 2) == 2047:
        return (-1)**sign * math.inf

    # IEEE uses 2**exponent, so it converts the exponent for IEEE format
    exponent -= 64
    exponent *= 4
    # Floating point is too small so zero
    if exponent < 0:
        return 0

    exponent += 1023  # The IEEE exponent
    temp = string_fraction
    string_mantissa = "0." + temp[2:]

    # Normalising the fraction
    while string_mantissa[0] != "1" and exponent > 0:
        string_mantissa = string_mantissa[2] + "." + string_mantissa[3:] + "0"
        exponent -= 1

    # The floating point is normal, otherwise it is zero
    if exponent > 0:
        true_fraction = getValue(string_mantissa)  # The value of the mantissa
        return convertIEEE64(sign, exponent, true_fraction)
    else:
        return 0


"""""
Calculates the floating point value from the IEEE components.

Double precision.

Parameters:
- sign The sign value for the floating point.
- exponent The exponent value for the floating point.
- fraction The mantissa value for the floating point.

Returns:
- The value of the floating point.
"""""


def convertIEEE64(sign, exponent, fraction):
    if exponent == 2047 and fraction != 0:
        # NaN
        return 0
    elif exponent > 2047:
        # infinite
        return (-1)**sign * math.inf
    elif 0 < exponent < 2047:
        # normal
        return (-1) ** sign * 2 ** (exponent - 1023) * fraction
    else:
        # zero
        return 0


"""""
Calculates the value of the IBM floating point.

Used for testing.

Parameters:
- sign The sign value of the floating point.
- string_exponent The binary string of the exponent value of the floating point.
- string_fraction The binary string of the fraction value of the floating point.
"""""


def IBM_value(sign, string_exponent, string_fraction):
    binExponent = bin(int(string_exponent, 2))

    exponent = int(binExponent, 2)

    true_fraction = getFractionIBM(string_fraction)

    return (-1) ** sign * 16 ** (exponent - 64) * true_fraction


"""""
Calculates the mantissa value for the IBM floating point number.

Used for testing.

Parameters:
- fraction The binary string of the fraction value of the floating point.

Returns:
- The value of the mantissa.
"""""


def getFractionIBM(fraction):
    result = 0.0
    temp = fraction
    string_fraction = "0." + temp[2:]

    length = len(string_fraction)
    i = 0
    n = 0

    while i < length:
        if string_fraction[i] == ".":
            i += 1
        if string_fraction[i] == "1":
            result += (1 * 2 ** n)
        i += 1
        n -= 1

    return result


"""""
Calculates the mantissa value from the binary
mantissa number.

Parameters:
- string_fraction The binary string representing the mantissa.

Returns:
- The value of the mantissa.
"""""


def getValue(string_fraction):
    result = 0.0
    length = len(string_fraction)
    i = 0
    n = 0

    while i < length:
        if string_fraction[i] == ".":
            i += 1
        if string_fraction[i] == "1":
            result += (1 * 2 ** n)
        i += 1
        n -= 1

    return result


"""""
Reads the input file as 32 bit binary numbers, and writes them
as either 32 bit or 64 bit binary numbers.

Input is IBM/360 floating point numbers, while the output is IEEE 754
floating point numbers.

Parameters:
- in_file The input bin file for reading.
- out_file The output bin file for writing.
- f The precision of the output.
"""""


def readFloat(in_file, out_file, f):
    length = 0
    try:
        with open(in_file, "rb") as in_stream:
            with open(out_file, "wb") as out:
                while True:
                    chunk = in_stream.read(4)
                    if chunk:
                        length += 1
                        B = ""
                        for b in chunk:
                            if b == 0:
                                B += "00"
                            else:
                                B += hex(b)[2:]

                        # Treats input as big endian
                        reverse_B = ""
                        i = len(B) - 1
                        while i >= 0:
                            reverse_B += B[i - 1:i + 1]
                            i -= 2

                        if f == "s":
                            f_point = convertIBM32(int(B, 16))
                            result = struct.pack('f', f_point)
                        else:
                            f_point = convertIBM32(int(B, 16))
                            result = struct.pack('d', f_point)
                        out.write(result)
                    else:
                        break
    except FileNotFoundError:
        print("Input file does not exist")

    if length > 0:
        print("Successfully converted", length, "of IBM/360 floating point numbers to IEEE 754 floating point numbers "
                                                "from", in_file, "to", out_file)


"""""
Reads the input file as 64 bit binary numbers, and writes them
as either 32 bit or 64 bit binary number.

Input is IBM/360 floating point numbers, while output is IEEE 754
floating point numbers.

Parameters:
- in_file The input bin file for reading.
- out_file The output bin file for writing.
- f The precision of the output.
"""""


def readDouble(in_file, out_file, f):
    length = 0
    try:
        with open(in_file, "rb") as in_stream:
            with open(out_file, "wb") as out:
                while True:
                    chunk = in_stream.read(8)
                    if chunk:
                        length += 1
                        B = ""
                        for b in chunk:
                            if b == 0:
                                B += "00"
                            else:
                                if len(hex(b)[2:]) < 2:
                                    B += "0"
                                B += hex(b)[2:]

                        # Treats input as big endian - not used
                        reverse_B = ""
                        i = len(B) - 1
                        while i >= 0:
                            reverse_B += B[i - 1:i + 1]
                            i -= 2

                        if f == "s":
                            f_point = convertIBM64(int(B, 16))
                            result = struct.pack('f', f_point)
                        else:
                            f_point = convertIBM64(int(B, 16))
                            result = struct.pack('d', f_point)
                        out.write(result)
                    else:
                        break
    except FileNotFoundError:
        print("Input file does not exist")

    if length > 0:
        print("Successfully converted", length,
              "IBM/360 floating point numbers to IEEE 754 floating point numbers from",
              in_file, "to", out_file)


# Main, reads arguments and performs conversion.
if len(sys.argv) == 5:
    input_file = str(sys.argv[1])
    input_format = sys.argv[2]
    output_file = str(sys.argv[3])
    output_format = sys.argv[4]

    if input_format not in "sd":
        print("Invalid argument for input precision")
    elif output_format not in "sd":
        print("Invalid argument for output precision")
    else:
        if input_format == "s":
            readFloat(input_file, output_file, output_format)
        elif input_format == "d":
            readDouble(input_file, output_file, output_format)
else:
    print("Invalid number of arguments")
