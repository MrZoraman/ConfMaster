/**
 * Copyright (c) 2019 Foomf
 *
 * This software is provided 'as-is', without any express or implied
 * warranty. In no event will the authors be held liable for any damages
 * arising from the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 *
 * 1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */
package com.lagopusempire.confmaster.core;

/**
 *
 * @author Foomf
 */
public final class ValueNode implements IValueNode {
    private String string;
    private Number number;

    public ValueNode() {
    }

    public ValueNode(byte value) {
        set(value);
    }

    public ValueNode(short value) {
        set(value);
    }

    public ValueNode(int value) {
        set(value);
    }

    public ValueNode(long value) {
        set(value);
    }

    public ValueNode(float value) {
        set(value);
    }

    public ValueNode(double value) {
        set(value);
    }

    public ValueNode(String value) {
        set(value);
    }

    public ValueNode(boolean value) {
        set(value);
    }

    public ValueNode(char value) {
        set(value);
    }

    @Override
    public Number getNumber() {
        return number;
    }

    @Override
    public ValueNode deepClone() {
        ValueNode clone = new ValueNode();
        clone.string = string;
        clone.number = number;
        return clone;
    }

    @Override
    public void set(byte value) {
        number = value;
    }

    @Override
    public void set(short value) {
        number = value;
    }

    @Override
    public void set(int value) {
        number = value;
    }

    @Override
    public void set(long value) {
        number = value;
    }

    @Override
    public void set(float value) {
        number = value;
    }

    @Override
    public void set(double value) {
        number = value;
    }

    @Override
    public void set(String value) {
        string = value;
    }

    @Override
    public void set(boolean value) {
        number = value ? 1 : 0;
    }

    @Override
    public void set(char value) {
        number = (byte)value;
    }

    @Override
    public byte byteValue() {
        if (number != null) {
            return number.byteValue();
        }

        if (string != null) {
            try {
                return Byte.decode(string);
            } catch (NumberFormatException ignored) {
            }
        }

        throw new IllegalStateException("This node is not a byte!");
    }

    @Override
    public short shortValue() {
        if (number != null) {
            return number.shortValue();
        }

        if (string != null) {
            try {
                return Short.decode(string);
            } catch (NumberFormatException ignored) {
            }
        }

        throw new IllegalStateException("This node is not a short!");
    }

    @Override
    public int intValue() {
        if (number != null) {
            return number.intValue();
        }

        if (string != null) {
            try {
                return Integer.decode(string);
            } catch (NumberFormatException ignored) {
            }
        }

        throw new IllegalStateException("This node is not an int!");
    }

    @Override
    public long longValue() {
        if (number != null) {
            return number.longValue();
        }

        if (string != null) {
            try {
                return Long.decode(string);
            } catch (NumberFormatException ignored) {
            }
        }

        throw new IllegalStateException("This node is not a long!");
    }

    @Override
    public float floatValue() {
        if (number != null) {
            return number.floatValue();
        }

        if (string != null) {
            try {
                return Float.valueOf(string);
            } catch (NumberFormatException ignored) {
            }
        }

        throw new IllegalStateException("This node is not a float!");
    }

    @Override
    public double doubleValue() {
        if (number != null) {
            return number.doubleValue();
        }

        if (string != null) {
            try {
                return Double.valueOf(string);
            } catch (NumberFormatException ignored) {
            }
        }

        throw new IllegalStateException("This node is not a double!");
    }

    @Override
    public boolean booleanValue() {
        if (number != null) {
            return number.byteValue() != 0;
        }

        if (string != null) {
            return Boolean.parseBoolean(string);
        }

        throw new IllegalStateException("This node is not a boolean!");
    }

    @Override
    public char charValue() {
        if (number != null) {
            return (char)number.byteValue();
        }

        if (string != null && string.length() > 0) {
            return string.charAt(0);
        }

        throw new IllegalStateException("This node is not a char!");
    }

    @Override
    public String stringValue() {
        if (string != null) {
            return string;
        }

        if (number != null) {
            return number.toString();
        }

        throw new IllegalStateException("This node is not a string!");
    }
}
