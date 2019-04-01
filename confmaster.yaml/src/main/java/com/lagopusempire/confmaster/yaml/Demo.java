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

import com.lagopusempire.confmaster.core.IListNode;
import com.lagopusempire.confmaster.core.IObjectNode;
import com.lagopusempire.confmaster.core.IValueNode;
import com.lagopusempire.confmaster.core.ListNode;
import com.lagopusempire.confmaster.core.ObjectNode;
import com.lagopusempire.confmaster.core.ValueNode;
import com.lagopusempire.confmaster.core.serialization.IDeserializableObject;
import com.lagopusempire.confmaster.core.serialization.IObjectDeserializer;
import com.lagopusempire.confmaster.core.serialization.IObjectSerializer;
import com.lagopusempire.confmaster.core.serialization.ISerializableObject;
import java.util.Set;

/**
 *
 * @author Foomf
 */
public class Demo {

    public static void main(String[] args) {
        // This is a proof of concept for a configuration system. It's a bit 
        // rough around the edges (error handling, etc), but the core idea
        // works. This can be considered a "minimum viable product."

        // Confmaster is designed to be backend agnostic, but for the purposes
        // of this demonstration I have the yaml backend implemented.
        
        // The primary concept behind confmaster is that the user asserts what
        // their data model is as they use it. Confmaster doesn't try to do
        // any sort of fancy abstraction or reflection. There are three node
        // types: ValueNode, ListNode and ObjectNodes.
        
        // ValueNodes contain java primitive types or strings.
        
        // ListNodes contain lists of other nodes.
        
        // ObjectNodes can also be considered maps. They are key value mappings,
        // the key being a member field of the object, and the value being the
        // actual data.
        
        // All three nodes implement the INode interface. This interface is
        // quite meaningless, other than to learn what type the node is.
        // However, it also declares several methods that all nodes share.
        // However, not all node types will implement all of these methods.
        // Calling any of the get methods specified in INode is an assertion
        // that the node is of that type. If it is a node of that type, then
        // all goes well. If it doesn't, it throws an exception. They are
        // basically thin wrappers around casting to the desired node type.
        
        // This is the example yaml string from the snakeyaml documentation.
        String yamlString
                = "a: 1"
                + "\nb: 2"
                + "\nc:"
                + "\n  - aaa"
                + "\n  - bbb";
        
        // Deserializing it is quite simple. We see that the top level "node"
        // is an object. It's an object with the key/fields being "a", "b", and
        // "c". We then call the parse function that gets us an object.
        IObjectNode root = YamlBackend.DeserializeObject(yamlString);
        
        // This has given us an object node. No casting is needed on our end to
        // do common operations on our object.
        Set<String> keys = root.keys();
        assert(keys.size() == 3);
        
        // We know that "a" is a value node. Its value is 1. Thus, we can call
        // INode's getValue() to retrieve it.
        IValueNode aNode = root.getValue("a");
        
        // From here we can extract the actual integer value from a.
        int a = aNode.intValue();
        assert(a == 1);
        
        // Confmaster has provided a shortcut on ObjectNode classes because this
        // is such a common operation.
        a = root.getInt("a");
        assert(a == 1);
        
        // The two operations are semantically identical.
        assert(root.getInt("a") == root.getValue("a").intValue());
        
        // c is a list. That is also straightforward to retrieve:
        IListNode cNode = root.getList("c");
        
        // from here, cNode can be iterated over.
        assert(cNode.length() == 2);
        
        // Retrieving a value is the same as the ObjectNode:
        String aaa = cNode.getValue(0).stringValue();
        assert(aaa.equals("aaa"));
        
        // But ListNode also provides some convenience methods
        aaa = cNode.getString(0);
        assert(aaa.equals("aaa"));
        
        // The two operations are semantically identical.
        assert(cNode.getString(0).equals(cNode.getValue(0).stringValue()));
        
        // There is no reflection magic that happens with serialization and
        // deserialization. In fact, Confmaster only provides convenience
        // methods. All of confmaster's serialization tools you could have
        // written yourself, since they don't access any private member
        // variables.
        
        // Confmaster assumes that the objects you might be trying to
        // (de)serialize might not be yours, thus you only have access to their
        // public methods. If those objects have a public interface that allows
        // you to break them down into primitive components and reconstruct them
        // from such, then you can have Confmaster work with them.
        Main2();
    }
    
    // This is a sample class. It's "Not my" foo because this  demonstration 
    // assumes we cannot change it. That is, add behavior to it.
    static class NotMyFoo {
        public NotMyFoo(int someValue, String someString) {
            SomeValue = someValue;
            SomeString = someString;
        }
        
        int SomeValue;
        
        String SomeString;
    }
    
    public static void Main2() {
        // Here we will make an instance of a NotMyFoo. It provides everything
        // we need in its public interface.
        NotMyFoo notMyFoo = new NotMyFoo(5, "test");
        
        // Since this is an object (that is, it's not a primitive type/string or
        // a list), we will want to convert this to an ObjectNode instance.
        IObjectNode notMyFooNode = ObjectNode.from(notMyFoo, (node, foo) -> {
            node.set("SomeValue", foo.SomeValue);
            node.set("SomeString", foo.SomeString);
        });
        
        // The deserialize method takes in a lambda that contains all of the
        // instructions needed for deserializing NotMyFoo. It creates a new
        // empty ObjectNode instance and provides the lambda taht instance and
        // the object you are serializing. The body is then expected to call the
        // ObjectNode's functions to fill it with foo's data. After that, it
        // returns to you that IObjectNode instance for you to attached to
        // another node, or go straight to converting back to yaml/whatever
        // backend.
        
        // The instance has been serialized as we'd expect it too.
        assert(notMyFooNode.getInt("SomeValue") == 5);
        assert(notMyFooNode.getString("SomeString").equals("test"));
        
        Main3();
    }
    
    // Notice how this has the exact same signature as the lambda above. It can
    // have the same code because the code is context independent. Thus, you can
    // create standalone methods like this:
    public static void NotMyFooSerializer(IObjectNode node, NotMyFoo foo) {
        node.set("SomeValue", foo.SomeValue);
        node.set("SomeString", foo.SomeString);
    }
    
    static void Main3() {
        // We can then use some of java's syntactic sugar to reuse that function
        // whenever we need to serialize a NotMyFoo instance:
        NotMyFoo notMyFoo = new NotMyFoo(5, "test");
        IObjectNode notMyFooNode = ObjectNode.from(notMyFoo, Demo::NotMyFooSerializer);
        
        // The instance has been serialized as we'd expect it too.
        assert(notMyFooNode.getInt("SomeValue") == 5);
        assert(notMyFooNode.getString("SomeString").equals("test"));
        
        Main4();
    }
    
    // Let's say we have a class called MyFoo that we did create. We are free
    // to add all the behavior we want to it. Confmaster provides utilities to
    // allow you to put the (de)serialization logic with the class itself by
    // implementing certain interfaces.
    static class MyFoo implements ISerializableObject, IDeserializableObject {
        public MyFoo(int someValue, String someString) {
            SomeValue = someValue;
            SomeString = someString;
        }
        
        // This empty constructor will be explained in the deserialization
        // section.
        public MyFoo() {
        }
        
        int SomeValue;
        
        String SomeString;

        // This method looks quite familiar, only instead of passing in a MyFoo
        // instance, we just use "this".
        @Override
        public void Serialize(IObjectNode node) {
            node.set("SomeValue", this.SomeValue);
            node.set("SomeString", this.SomeString);
        }

        // This will be explained in the deserialization section as well.
        @Override
        public void Deserialize(IObjectNode node) {
            this.SomeValue = node.getInt("SomeValue");
            this.SomeString = node.getString("SomeString");
        }
    }
    
    static void Main4() {
        // We will now create an instance of our class.
        MyFoo myFoo = new MyFoo(5, "test");
        
        // Serialization is now even more straightforward:
        IObjectNode myFooNode = ObjectNode.from(myFoo);
        
        // That is all that's needed. Again, myFoo is serialized as expected.
        assert(myFooNode.getInt("SomeValue") == 5);
        assert(myFooNode.getString("SomeString").equals("test"));
        
        // Deserializing is just as simple. Note that an instance of MyFoo needs
        // to be passed in, since ConfMaster doesn't know how to construct an
        // instance of MyFoo.
        myFoo = myFooNode.to(new MyFoo(), (foo, node) -> { 
            foo.SomeValue = node.getInt("SomeValue");
            foo.SomeString = node.getString("SomeString");
            return foo;
        });
        
        // again, the lambda interface is what it is so that it can be a static
        // method, using the same syntactic sugar as before.
        
        //myFoo has been deserialized as expected:
        assert(myFoo.SomeValue == 5);
        assert(myFoo.SomeString.equals("test"));
        
        // If we are able to add behavior to our class (like implementing the
        // IDeserializableObject interface) then deserializing is even more
        // straightforward:
        myFoo = myFooNode.to(new MyFoo());
        
        //myFoo has been deserialized as expected:
        assert(myFoo.SomeValue == 5);
        assert(myFoo.SomeString.equals("test"));
        
        // Because ConfMaster just provides a backend-agnostic data model,
        // it is very easy to construct your own data models by hand!
        IObjectNode root = new ObjectNode();
        IValueNode a = new ValueNode(1);
        IValueNode b = new ValueNode(2);
        IListNode c = new ListNode();
        IValueNode aaa = new ValueNode("aaa");
        IValueNode bbb = new ValueNode("bbb");
        c.add(aaa);
        c.add(bbb);
        root.set("a", a);
        root.set("b", b);
        root.set("c", c);
        // root is now identical to the example yaml at the beginning. However
        // this was extremely terse. We can shorten it up a bit.
        root = new ObjectNode();
        c = new ListNode();
        c.add(new ValueNode("aaa"));
        c.add(new ValueNode("bbb"));
        root.set("a", new ValueNode(1));
        root.set("b", new ValueNode(2));
        root.set("c", c);
        // can we go even terser? Why yes we can, thanks to the convenience
        // methods confmaster provides us.
        root = new ObjectNode();
        c = new ListNode();
        c.add("aaa");
        c.add("bbb");
        root.set("a", 1);
        root.set("b", 2);
        root.set("c", c);
        // and in fact, since confmaster supports method chaining we can go
        // even more terse.
        root = new ObjectNode();
        c = new ListNode().add("aaa").add("bbb");
        root.set("a", 1).set("b", 2).set("c", c);
        // And it's now clear we could make this a one liner
        root = new ObjectNode()
                .set("a", 1)
                .set("b", 2)
                .set("c", new ListNode()
                        .add("aaa")
                        .add("bbb"));
        
        // One final misc thing that confmaster provides is "resolution." What
        // that means is it provides some convenience methods for drilling down
        // the object tree to get what you want. Let's create a basic data model
        // to show you:
        root.set(
                "level1", new ObjectNode().set(
                        "level2", new ObjectNode().set(
                                "level3", new ValueNode(33))));
        // This is the equivalent to this yaml document:
        // level1:
        //   level2:
        //     level3: 33
        
        // Now, as we know already we can get to level 33 the standard way:
        int num = root.getObject("level1").getObject("level2").getInt("level3");
        assert(num == 33);
        
        // but this is quite terse! If there are nested objects (that is,
        // adjacent getObject(String) calles like you see above), confMaster
        // can shorten that up for us with the Resolve(String) method.
        num = root.resolve("level1.level2").getInt("level3");
        assert(num == 33);
        // this is semantically identical to above. However, can we get more
        // terse? I wouldn't ask if the answer was no! Confmaster also provides
        // resolve* methods for getting specific value types.
        
        num = root.resolveInt("level1.level2.level3");
        assert(num == 33);
        // This is also semantically the same, but very convenient!
        
        // The main feature I want to add to this library that I haven't
        // demonstrated here is schema auditing. That is, you can give
        // confMaster an expected schema specification and it will audit 
        // the datamodel from an INode. It will return true if the schema is
        // followed, and false if it is not. After the schema is validated,
        // you can gurantee that all the types will be valid. That is,
        // when you call getObject() you will get an object. This wouldn't
        // gurantee that getInt() or any of the other methods not declared in
        // INode wouldn't return null.
        
        // Confmaster is not expected to be used as an internal data model. It's
        // supposed to be a bridge between your own internal data model and
        // a human readable plaintext format. If the user followes that, then
        // a schema auditing feature could be used for data model conversions.
        // Let's say a codebase changes the layout of its data model (its
        // schema). Instead of keeping track of a version number, you could
        // simply go down the line comparing all the previously known schemas.
        // If an older schema returns true, then you read that schema into your
        // internal data model, and then serialize it back using your new
        // schema. I can show a very simple example here:
        
        // old schema. This supposed schema audit feature would tell us that
        // schema_1_Serializer is the right one to use.
        IObjectDeserializer<MyFoo> schema_1_Deserializer = (f, node) -> {
            f.SomeValue = node.getInt("schema1_val");
            f.SomeString = node.getString("schema1_str");
            return f;
        };
        
        // This is our new data model/schema. Note the different names for
        // the keys.
        IObjectSerializer<MyFoo> schema_2_Serializer = (node, f) -> {
            node.set("schema2_value", f.SomeValue);
            node.set("schema2_string", f.SomeString);
        };
        
        root = new ObjectNode() // read old data from yaml or wherever
                .set("schema1_val", 5)
                .set("schema1_str", "test");
        MyFoo foo = root.to(new MyFoo(), schema_1_Deserializer);
        
        // Serialize back to a Confmaster node instance
        IObjectNode node = ObjectNode.from(foo, schema_2_Serializer);
        
        // Node can now be written using whatever backend (yaml, etc). The
        // schema conversion is complete.
        assert(node.getInt("schema2_value") == 5);
        assert(node.getString("schema2_string").equals("test"));
    }
}
