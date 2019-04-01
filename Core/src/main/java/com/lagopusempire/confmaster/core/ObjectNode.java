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

import com.lagopusempire.confmaster.core.serialization.IDeserializableObject;
import com.lagopusempire.confmaster.core.serialization.IObjectDeserializer;
import com.lagopusempire.confmaster.core.serialization.IObjectSerializer;
import com.lagopusempire.confmaster.core.serialization.ISerializableObject;
import java.util.*;

/**
 *
 * @author Foomf
 */
public class ObjectNode implements IObjectNode {
    private final Map<String, INode> children = new TreeMap<>();

    @Override
    public INode get(String key) {
        return children.get(key);
    }

    @Override
    public Set<String> keys() {
        return children.keySet();
    }

    @Override
    public ObjectNode resolve(Deque<String> paths) {
        if (paths.isEmpty()) {
            return this;
        }

        IObjectNode next = getObject(paths.removeFirst());
        return next.resolve(paths);
    }

    @Override
    public ObjectNode resolve(String path) {
        String[] parts = path.split("\\.");
        Deque<String> list = new LinkedList<>(Arrays.asList(parts));
        return resolve(list);
    }

    @Override
    public IListNode getList(String key) {
        INode node = children.get(key);
        if (node.getType() == NodeType.LIST) {
            return (IListNode)node;
        } else {
            throw new UnsupportedOperationException("Not a list!");
        }
    }

    @Override
    public IObjectNode getObject(String key) {
        INode node = children.get(key);
        if (node.getType() == NodeType.OBJECT) {
            return (IObjectNode)node;
        } else {
            throw new UnsupportedOperationException("Not an object!");
        }
    }

    @Override
    public IValueNode getValue(String key) {
        INode node = children.get(key);
        if (node.getType() == NodeType.VALUE) {
            return (IValueNode)node;
        } else {
            throw new UnsupportedOperationException("Not a value!");
        }
    }

    @Override
    public IObjectNode deepClone() {
        ObjectNode clone = new ObjectNode();
        children.entrySet().forEach(child -> {
            clone.children.put(child.getKey(), child.getValue().deepClone());
        });
        return clone;
    }

    @Override
    public IObjectNode unset(String key) {
        children.remove(key);
        return this;
    }

    @Override
    public IObjectNode set(String key, INode value) {
        if (value == null) {
            return unset(key);
        }

        children.put(key, value);
        return this;
    }
    
    public static <T> ObjectNode from(T data, IObjectSerializer<T> serializer) {
        ObjectNode node = new ObjectNode();
        serializer.Serialize(node, data);
        return node;
    }

    public static ObjectNode from(ISerializableObject data) {
        ObjectNode node = new ObjectNode();
        data.Serialize(node);
        return node;
    }
    
    @Override
    public <T> T to(T instance, IObjectDeserializer<T> deserializer) {
        deserializer.Deserialize(instance, this);
        return instance;
    }

    @Override
    public <T extends IDeserializableObject> T to(T instance) {
        instance.Deserialize(this);
        return instance;
    }

    @Override
    public IObjectNode set(String key, byte value) {
        return set(key, new ValueNode(value));
    }

    @Override
    public IObjectNode set(String key, short value) {
        return set(key, new ValueNode(value));
    }

    @Override
    public IObjectNode set(String key, int value) {
        return set(key, new ValueNode(value));
    }

    @Override
    public IObjectNode set(String key, long value) {
        return set(key, new ValueNode(value));
    }

    @Override
    public IObjectNode set(String key, float value) {
        return set(key, new ValueNode(value));
    }

    @Override
    public IObjectNode set(String key, double value) {
        return set(key, new ValueNode(value));
    }

    @Override
    public IObjectNode set(String key, String value) {
        return set(key, new ValueNode(value));
    }

    @Override
    public IObjectNode set(String key, boolean value) {
        return set(key, new ValueNode(value));
    }

    @Override
    public IObjectNode set(String key, char value) {
        return set(key, new ValueNode(value));
    }

    @Override
    public byte getByte(String key) {
        return getValue(key).byteValue();
    }

    @Override
    public short getShort(String key) {
        return getValue(key).shortValue();
    }

    @Override
    public int getInt(String key) {
        return getValue(key).intValue();
    }

    @Override
    public long getLong(String key) {
        return getValue(key).longValue();
    }

    @Override
    public float getFloat(String key) {
        return getValue(key).floatValue();
    }

    @Override
    public double getDouble(String key) {
        return getValue(key).doubleValue();
    }

    @Override
    public boolean getBoolean(String key) {
        return getValue(key).booleanValue();
    }

    @Override
    public char getChar(String key) {
        return getValue(key).charValue();
    }

    @Override
    public String getString(String key) {
        return getValue(key).stringValue();
    }
    
    @Override
    public IListNode resolveList(String path) {
        String[] parts = path.split("\\.");
        Deque<String> list = new LinkedList<>(Arrays.asList(parts));
        String key = list.removeLast();
        IObjectNode node = resolve(list);
        return node.getList(key);
    }

    @Override
    public byte resolveByte(String path) {
        String[] parts = path.split("\\.");
        Deque<String> list = new LinkedList<>(Arrays.asList(parts));
        String key = list.removeLast();
        IObjectNode node = resolve(list);
        return node.getByte(key);
    }

    @Override
    public short resolveShort(String path) {
        String[] parts = path.split("\\.");
        Deque<String> list = new LinkedList<>(Arrays.asList(parts));
        String key = list.removeLast();
        IObjectNode node = resolve(list);
        return node.getShort(key);
    }

    @Override
    public int resolveInt(String path) {
        String[] parts = path.split("\\.");
        Deque<String> list = new LinkedList<>(Arrays.asList(parts));
        String key = list.removeLast();
        IObjectNode node = resolve(list);
        return node.getInt(key);
    }

    @Override
    public long resolveLong(String path) {
        String[] parts = path.split("\\.");
        Deque<String> list = new LinkedList<>(Arrays.asList(parts));
        String key = list.removeLast();
        IObjectNode node = resolve(list);
        return node.getLong(key);
    }

    @Override
    public float resolveFloat(String path) {
        String[] parts = path.split("\\.");
        Deque<String> list = new LinkedList<>(Arrays.asList(parts));
        String key = list.removeLast();
        IObjectNode node = resolve(list);
        return node.getFloat(key);
    }

    @Override
    public double resolveDouble(String path) {
        String[] parts = path.split("\\.");
        Deque<String> list = new LinkedList<>(Arrays.asList(parts));
        String key = list.removeLast();
        IObjectNode node = resolve(list);
        return node.getDouble(key);
    }

    @Override
    public char resolveChar(String path) {
        String[] parts = path.split("\\.");
        Deque<String> list = new LinkedList<>(Arrays.asList(parts));
        String key = list.removeLast();
        IObjectNode node = resolve(list);
        return node.getChar(key);
    }

    @Override
    public String resolveString(String path) {
        String[] parts = path.split("\\.");
        Deque<String> list = new LinkedList<>(Arrays.asList(parts));
        String key = list.removeLast();
        IObjectNode node = resolve(list);
        return node.getString(key);
    }
}
