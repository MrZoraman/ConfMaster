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
public interface IListNode extends INode {
    @Override
    default NodeType getType() {
        return NodeType.LIST;
    }

    int length();

    INode get(int index);

    IListNode add(INode value) ;

    IListNode add(byte value);

    IListNode add(short value);

    IListNode add(int value);

    IListNode add(long value);

    IListNode add(float value);

    IListNode add(double value);

    IListNode add(String value);

    IListNode add(boolean value);

    IListNode add(char value);

    byte getByte(int index);

    short getShort(int index);

    int getInt(int index);

    long getLong(int index);

    float getFloat(int index);

    double getDouble(int index);

    boolean getBoolean(int index);

    char getChar(int index);

    String getString(int index);
}
