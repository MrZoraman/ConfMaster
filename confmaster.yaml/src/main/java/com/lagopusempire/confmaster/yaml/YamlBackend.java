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
package com.lagopusempire.confmaster.yaml;

import com.lagopusempire.confmaster.core.*;
import java.util.*;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author Foomf
 */
public class YamlBackend {
    public static Object SerializeNode(INode root) {
        switch (root.getType()) {
            case VALUE:
                IValueNode valueNode = (IValueNode)root;
                if (valueNode.getNumber() == null) {
                    return valueNode.stringValue();
                } else {
                    return valueNode.getNumber();
                }
            case OBJECT:
                IObjectNode objectNode = (IObjectNode)root;
                Map<String, Object> map = new TreeMap<>();
                for(String key : objectNode.keys()) {
                    map.put(key, SerializeNode(objectNode.get(key)));
                }
                return map;
            case LIST:
                IListNode listNode = (IListNode)root;
                List<Object> list = new ArrayList<>();
                for (int ii = 0; ii < listNode.length(); ++ii) {
                    list.add(SerializeNode(listNode.get(ii)));
                }
                return list;
            default:
                throw new UnsupportedOperationException("unknown type");
        }
    }

    public static String Serialize(INode root) {
        Object data = SerializeNode(root);
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        return yaml.dump(data);
    }

    public static IObjectNode DeserializeObject(String yaml) {
        Object rootObject = Parse(yaml);
        INode root = Deserialize(rootObject);
        if (root.getType() != NodeType.OBJECT) {
            throw new UnsupportedOperationException("Didn't read an object");
        }
        return (IObjectNode)root;
    }

    private static INode Deserialize(Object object) {
        if (object instanceof AbstractMap) {
            @SuppressWarnings("unchecked")
            AbstractMap<String, Object> map = (AbstractMap<String, Object>)object;

            ObjectNode node = new ObjectNode();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                INode child = Deserialize(entry.getValue());
                node.set(entry.getKey(), child);
            }

            return node;
        } else if (object instanceof AbstractList) {
            @SuppressWarnings("unchecked")
            AbstractList<Object> list = (AbstractList<Object>)object;

            ListNode node = new ListNode();
            for (Object o : list) {
                INode child = Deserialize(o);
                node.add(child);
            }
            return node;
        } else if (object instanceof String) {
            String str = (String)object;
            return new ValueNode(str);
        } else if (object instanceof Number) {
            Number num = (Number)object;
            return new ValueNode(num.longValue());
        } else {
            throw new UnsupportedOperationException("I dunno what I found");
        }
    }

    private static Object Parse(String yamlString) {
        Yaml yaml = new Yaml();
        return yaml.load(yamlString);
    }
}
