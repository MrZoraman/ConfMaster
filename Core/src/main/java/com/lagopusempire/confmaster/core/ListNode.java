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

import com.lagopusempire.confmaster.core.serialization.IObjectSerializer;
import com.lagopusempire.confmaster.core.serialization.ISerializableObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Foomf
 */
public class ListNode implements IListNode {
    private List<INode> children = new ArrayList<>();

    @Override
    public int length() {
        return children.size();
    }

    @Override
    public INode get(int index) {
        return children.get(index);
    }

    @Override
    public IListNode getList(int index) {
        INode node = children.get(index);
        if (node.getType() == NodeType.LIST) {
            return (IListNode)node;
        } else {
            throw new UnsupportedOperationException("Not a list!");
        }
    }

    @Override
    public IObjectNode getObject(int index) {
        INode node = children.get(index);
        if (node.getType() == NodeType.OBJECT) {
            return (IObjectNode)node;
        } else {
            throw new UnsupportedOperationException("Not an object!");
        }
    }

    @Override
    public IValueNode getValue(int index) {
        INode node = children.get(index);
        if (node.getType() == NodeType.VALUE) {
            return (IValueNode) node;
        } else {
            throw new UnsupportedOperationException("Not a value!");
        }
    }

    @Override
    public IListNode deepClone() {
        ListNode clone = new ListNode();
        children.forEach(child -> {
            clone.children.add(child.deepClone());
        });
        return clone;
    }

    @Override
    public IListNode add(INode value) {
        children.add(value);
        return this;
    }
    
    public static <T> ListNode from(Iterable<T> data, IObjectSerializer<T> serializer) {
        ListNode node = new ListNode();
        for(T datum : data) {
            node.add(ObjectNode.from(datum, serializer));
        }
        return node;
    }

    public static  ListNode from(Iterable<ISerializableObject> data) {
        ListNode node = new ListNode();
        for(ISerializableObject datum : data) {
            node.add(ObjectNode.from(datum));
        }
        return node;
    }

    @Override
    public IListNode add(byte value) {
        return add(new ValueNode(value));
    }

    @Override
    public IListNode add(short value) {
        return add(new ValueNode(value));
    }

    @Override
    public IListNode add(int value) {
        return add(new ValueNode(value));
    }

    @Override
    public IListNode add(long value) {
        return add(new ValueNode(value));
    }

    @Override
    public IListNode add(float value) {
        return add(new ValueNode(value));
    }

    @Override
    public IListNode add(double value) {
        return add(new ValueNode(value));
    }

    @Override
    public IListNode add(String value) {
        return add(new ValueNode(value));
    }

    @Override
    public IListNode add(boolean value) {
        return add(new ValueNode(value));
    }

    @Override
    public IListNode add(char value) {
        return add(new ValueNode(value));
    }

    @Override
    public byte getByte(int index) {
        return getValue(index).byteValue();
    }

    @Override
    public short getShort(int index) {
        return getValue(index).shortValue();
    }

    @Override
    public int getInt(int index) {
        return getValue(index).intValue();
    }

    @Override
    public long getLong(int index) {
        return getValue(index).longValue();
    }

    @Override
    public float getFloat(int index) {
        return getValue(index).floatValue();
    }

    @Override
    public double getDouble(int index) {
        return getValue(index).doubleValue();
    }

    @Override
    public boolean getBoolean(int index) {
        return getValue(index).booleanValue();
    }

    @Override
    public char getChar(int index) {
        return getValue(index).charValue();
    }

    @Override
    public String getString(int index) {
        return getValue(index).stringValue();
    }
}
