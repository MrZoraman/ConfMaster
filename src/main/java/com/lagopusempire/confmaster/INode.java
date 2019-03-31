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
package com.lagopusempire.confmaster;

/**
 *
 * @author Foomf
 */
public interface INode {
        default RuntimeException Unsupported(String methodName) {
        return new UnsupportedOperationException("This is as " 
                + GetType().toString().toLowerCase() + " node! Thus " 
                + methodName + " is not supported.");
    }

    NodeType GetType();

    INode DeepClone();

    default IListNode GetList(int index) {
        throw Unsupported("GetList(int)");
    }

    default IListNode GetList(String key) {
        throw Unsupported("GetList(String)");
    }

    default IObjectNode GetObject(int key) {
        throw Unsupported("GetObject(int)");
    }

    default IObjectNode GetObject(String key) {
        throw Unsupported("GetObject(String)");
    }

    default IValueNode GetValue(int key) {
        throw Unsupported("GetValue(int)");
    }

    default IValueNode GetValue(String key) {
        throw Unsupported("GetValue(String)");
    }
}
