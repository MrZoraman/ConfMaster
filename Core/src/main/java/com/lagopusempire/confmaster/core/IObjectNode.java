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
import java.util.Deque;
import java.util.Set;

/**
 *
 * @author Foomf
 */
public interface IObjectNode extends INode {
    @Override
    default NodeType getType() {
        return NodeType.OBJECT;
    }

    Set<String> keys();

    INode get(String key);

    ObjectNode resolve(Deque<String> paths);

    IObjectNode resolve(String path);

    IObjectNode unset(String key);

    IObjectNode set(String key, INode value);
    
    <T> T to(T instance, IObjectDeserializer<T> deserializer);

    <T extends IDeserializableObject> T to(T instance);

    IObjectNode set(String key, byte value);

    IObjectNode set(String key, short value);

    IObjectNode set(String key, int value);

    IObjectNode set(String key, long value);

    IObjectNode set(String key, float value);

    IObjectNode set(String key, double value);

    IObjectNode set(String key, String value);

    IObjectNode set(String key, boolean value);

    IObjectNode set(String key, char value);

    byte getByte(String key);

    short getShort(String key);

    int getInt(String key);

    long getLong(String key);

    float getFloat(String key);

    double getDouble(String key);

    boolean getBoolean(String key);

    char getChar(String key);

    String getString(String key);
    
    IListNode resolveList(String path);

    byte resolveByte(String path);

    short resolveShort(String path);

    int resolveInt(String path);

    long resolveLong(String path);

    float resolveFloat(String path);

    double resolveDouble(String path);

    char resolveChar(String path);

    String resolveString(String path);
}
